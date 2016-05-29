package com.cqgk.clerk.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqgk.clerk.BuildConfig;
import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.SearchResultPopAdapter;
import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.MeProductListBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.config.Constant;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.utils.LogUtil;
import com.cqgk.clerk.view.NormalListView;
import com.cqgk.clerk.view.SearchResultPopView;
import com.cqgk.clerk.zxing.CamerBaseActivity;
import com.cqgk.clerk.zxing.decoding.Intents;
import com.google.zxing.Result;
import com.google.zxing.common.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by duke on 16/5/17.
 */
@ContentView(R.layout.barcodefindproduct)
public class BarCodeFindProductActivity extends CamerBaseActivity {


    @ViewInject(R.id.capture_preview)
    SurfaceView capture_preview;

    @ViewInject(R.id.resulttitle)
    LinearLayout resulttitle;

    @ViewInject(R.id.listview)
    NormalListView listview;

    private boolean hasSurface;

    private int showType = 0;//0-编辑商品1-返回商品

    private int search_page; //分页
    private int searchTotal;//

    private String brCode ;

    private SearchResultPopAdapter searchResultPopAdapter;
    private List<ProductDtlBean> returnList;
    private HashMap<String, String> productlist = new HashMap<>();

    private Handler handler = new Handler();//摄像头重启线程
    private Runnable runnable = new Runnable() {//摄像头重启线程方法
        @Override
        public void run() {
            LogUtil.e("camberRestart");
            reScan();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("商品扫描");

        try {
            showType = getIntent().getIntExtra("showtype", 0);
        } catch (NullPointerException e) {

        }

        //多选
        if (showType == 1) {
            returnList = new ArrayList<>();
            getTitleDelegate().setRightText("确定");
            getTitleDelegate().setRightOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < searchResultPopAdapter.getCount(); i++) {
                        if (searchResultPopAdapter.getItem(i).getNum() > 0) {
                            returnList.add(searchResultPopAdapter.getItem(i));
                        }
                    }


                    if (returnList.size() == 0) {
                        showToast("请选择商品");
                        return;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dtllist", (Serializable) returnList);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(1, intent);
                    finish();
                }
            });

            listview.setScrollStateEvent(new NormalListView.ScrollStateEvent() {
                @Override
                public void isBottom() {

                    if(searchResultPopAdapter.getCount()+1>searchTotal){
                        listview.addFooterView("已经到底了");
                        return;
                    }

                    search_page++;
                    searchCode(null==brCode?"":brCode);
                }

                @Override
                public void isOver() {

                }

                @Override
                public void isTop() {

                }
            });
        }

        searchResultPopAdapter = new SearchResultPopAdapter(this);
        searchResultPopAdapter.setShowtype(showType);
        searchResultPopAdapter.setItemListener(new SearchResultPopAdapter.ItemListener() {
            @Override
            public void itemClick(int i) {
                ProductDtlBean productDtlBean = searchResultPopAdapter.getItem(i);
                if (showType == 0) {
                    NavigationHelper.getInstance().startUploadProduct(productDtlBean.getGoodsId());
                }
            }
        });
        listview.setAdapter(searchResultPopAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (hasSurface) {
            initCamera(capture_preview.getHolder());
        } else {
            capture_preview.getHolder().addCallback(this);
            capture_preview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    public void handleDecode(Result result, Bitmap barcode) {
        super.handleDecode(result, barcode);
        String product_bar_code = recode(result.toString());
        brCode = product_bar_code;
        if (1 == showType){
            search_page = 1;
            searchCode(product_bar_code);
        }else {
            getCodeData(product_bar_code);
        }

    }

    private void searchCode(String product_bar_code){
        RequestUtils.searchGood(product_bar_code,search_page,new HttpCallBack<GoodListBean>(){
            @Override
            public void success(GoodListBean result, String msg) {
                handler.postDelayed(runnable, Constant.CameraRestartTime);
                if (result == null) {
                    showLongToast("此编号无商品");
                    return;
                }

                if (result.getList().size() == 0) {
                    showLongToast("此编号无商品");
                    return;
                }

                resulttitle.setVisibility(View.VISIBLE);

                searchTotal = result.getTotal();

                for (int i = 0; i < result.getList().size(); i++) {
                    ProductDtlBean item = result.getList().get(i);
                    if (productlist.containsKey(item.getGoodsId())) {
                        setNumQty(item);
                    } else {
                        searchResultPopAdapter.addItem(item);
                        productlist.put(item.getGoodsId(), item.getGoodsTitle());
                    }
                }
                searchResultPopAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean failure(int state, String msg) {
                showToast(msg);
                handler.postDelayed(runnable, Constant.CameraRestartTime);
                return super.failure(state, msg);
            }
        });
    }

    private void getCodeData(String product_bar_code) {
        RequestUtils.queryClerkGoodsByBarcode(product_bar_code, new HttpCallBack<MeProductListBean>() {
            @Override
            public void success(MeProductListBean result, String msg) {
                handler.postDelayed(runnable, Constant.CameraRestartTime);
                if (result == null) {
                    showLongToast("此编号无商品");
                    return;
                }

                if (result.getList().size() == 0) {
                    showLongToast("此编号无商品");
                    return;
                }

                resulttitle.setVisibility(View.VISIBLE);

                for (int i = 0; i < result.getList().size(); i++) {
                    ProductDtlBean item = result.getList().get(i);
                    if (productlist.containsKey(item.getGoodsId())) {
                        setNumQty(item);
                    } else {
                        searchResultPopAdapter.addItem(item);
                        productlist.put(item.getGoodsId(), item.getGoodsTitle());
                    }
                }
                searchResultPopAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean failure(int state, String msg) {
                showToast(msg);
                handler.postDelayed(runnable, Constant.CameraRestartTime);
                return super.failure(state, msg);
            }
        });
    }

    /**
     * @param bean
     */
    private void setNumQty(ProductDtlBean bean) {
        for (int i = 0; i < searchResultPopAdapter.getValuelist().size(); i++) {
            if (bean.getGoodsId().equals(searchResultPopAdapter.getItem(i).getGoodsId())) {
                searchResultPopAdapter.getItem(i).setNum(searchResultPopAdapter.getItem(i).getNum() + 1);
                //LogUtil.e("__________find_after:%s",String.valueOf(searchResultPopAdapter.getItem(i).getNum()));
            }
        }
        //searchResultPopAdapter.notifyDataSetChanged();
    }

    @Override
    protected void reScan() {
        super.reScan();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }


}
