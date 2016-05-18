package com.cqgk.shennong.shop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.cqgk.shennong.shop.R;
import com.cqgk.shennong.shop.bean.normal.AdsBean;
import com.cqgk.shennong.shop.helper.ImageHelper;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.http.UrlApi;
import com.cqgk.shennong.shop.utils.AppUtil;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 轮播图组件
 *
 * @author duke
 */
public class SlideShowView extends FrameLayout {


    //轮播图图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 6;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;

    //自定义轮播图的资源
    private List<AdsBean> imageUrls;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;

    public List<AdsBean> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<AdsBean> imageUrls) {
        this.imageUrls = imageUrls;
        imageViewsList = new ArrayList<>();
    }

    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;

    //Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    public SlideShowView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initData();
    }


    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, TIME_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 停止轮播图切换
     */
    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    /**
     * 初始化相关Data
     */
    private void initData() {
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
    }

    /**
     * 初始化Views等UI
     */
    public void initUI(Context context) {
        if (imageUrls == null || imageUrls.size() == 0) {
            imageUrls = new ArrayList<AdsBean>();
        }

        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();

        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();


        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView view = new ImageView(context);
            LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);

            view.setScaleType(ScaleType.FIT_XY);
            //view.setAdjustViewBounds(true);
            //view.setMaxWidth(width);
            //view.setMaxHeight(width*5);
            view.setLayoutParams(imgparams);

            imageViewsList.add(view);

            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 9;
            params.rightMargin = 9;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);

        MyPagerAdapter adapter = new MyPagerAdapter();
        adapter.setImageAdList(imageUrls);

        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new MyPageChangeListener());

        if (isAutoPlay) {
            startPlay();
        }
    }

    /**
     * item视图
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        private List<AdsBean> imageAdList = new ArrayList<AdsBean>();

        public List<AdsBean> getImageAdList() {
            return imageAdList;
        }

        public void setImageAdList(List<AdsBean> imageAdList) {
            this.imageAdList = imageAdList;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {

            AdsBean imagead = imageAdList.get(position);
            final ImageView imageView = imageViewsList.get(position);
            String realPath = imagead.getImgUrl();
            ImageHelper.getInstance().displayZhan(imageView,realPath);
//            ImageHelper.getInstance().displayZhan(imageView, realPath,new SimpleImageLoadingListener(){
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
//                    super.onLoadingComplete(imageUri, view, bitmap);
//                    if (null != bitmap){
//                        imageView.setScaleType(ScaleType.FIT_XY);
//                        final int width =( 0==SlideShowView.this.getWidth()? AppUtil.getDisplayWidth():SlideShowView.this.getWidth() );
//                        final int height = width*bitmap.getHeight()/bitmap.getWidth();
//                        imageView.setImageBitmap(bitmap);
//                        imageView.invalidate();
//                        if (height>SlideShowView.this.getHeight()) {
//                            SlideShowView.this.setLayoutParams(new LinearLayout.LayoutParams(width, height>800?800:height));
//                            invalidate();
//                            Log.e("imGsize", "width" + width + "  height" + height);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (height > SlideShowView.this.getHeight()) {
//                                        SlideShowView.this.setLayoutParams(new LinearLayout.LayoutParams(width,  height>800?800:height));
//                                    }
//                                }
//
//                            }, 1000);
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    super.onLoadingStarted(imageUri, view);
//                    imageView.setScaleType(ScaleType.CENTER_INSIDE);
//                }
//
//            });



            imageView.setOnClickListener(new adToUrl(imagead.getLink()));

            ((ViewPager) container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        class adToUrl implements OnClickListener {
            private String url = "";

            public adToUrl(String url) {
                this.url = url;
            }

            @Override
            public void onClick(View view) {
               // NavigationHelper.getInstance().urlJump(url);
            }
        }

    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    ((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_true);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_false);
                }
            }
        }

    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                //AppLogger.d("------------SlideShowTask");
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }






}