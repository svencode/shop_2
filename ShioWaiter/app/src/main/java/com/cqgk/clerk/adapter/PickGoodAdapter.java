package com.cqgk.clerk.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cqgk.clerk.BuildConfig;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.GoodListBean;

import com.cqgk.clerk.R;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.config.Constant;
import com.cqgk.clerk.helper.ImageHelper;
import com.cqgk.clerk.http.RequestHelper;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.utils.LogUtil;

import java.util.ArrayList;


/**
 * Created by sven on 16/5/12.
 */
public class PickGoodAdapter extends BaseAdapter {
    private Context context;
    private PickGoodDelegate delegate;

    private ArrayList<ProductDtlBean> topGoodList;
    private ArrayList<ProductDtlBean> myGood;
    private int handlePosition;

    public PickGoodAdapter(Context context, PickGoodDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
        topGoodList = new ArrayList<>();
        myGood = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    public void stContext(Context context) {
        this.context = context;
    }

    public ArrayList<ProductDtlBean> getTopGoodList() {
        return topGoodList;
    }

    public void setTopGoodList(ArrayList<ProductDtlBean> topGoodList) {
        this.topGoodList = topGoodList;
    }

    public ArrayList<ProductDtlBean> getMyGood() {
        return myGood;
    }

    public void setMyGood(ArrayList<ProductDtlBean> myGood) {
        this.myGood = myGood;
    }

    @Override
    public int getCount() {
        return 2 + myGood.size() + topGoodList.size() / 2 + topGoodList.size() % 2;
    }

    @Override
    public ProductDtlBean getItem(int position) {
        return myGood == null ? null : myGood.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (0 == position) {
            view = LayoutInflater.from(context).inflate(R.layout.cell_title, null);
            TextView name = (TextView) view.findViewById(R.id.nameTV);
            name.setText("本单商品");
            view.setBackgroundResource(R.color.bg_color);
        } else if ((myGood.size() + 1) == position) {
            view = LayoutInflater.from(context).inflate(R.layout.cell_title, null);
            TextView name = (TextView) view.findViewById(R.id.nameTV);
            name.setText("本店热销");
            view.setBackgroundResource(R.color.bg_color);
        } else if (position > 0 && position <= (myGood.size() + 1)) {
            view = LayoutInflater.from(context).inflate(R.layout.cell_good_one, null);

            final ProductDtlBean item1 = myGood.get(position - 1);

            ImageView img = (ImageView) view.findViewById(R.id.imgIV);
            TextView name = (TextView) view.findViewById(R.id.nameTV);
            TextView desc = (TextView) view.findViewById(R.id.descTV);
            TextView price = (TextView) view.findViewById(R.id.priceTV);

            Button plusBtn = (Button) view.findViewById(R.id.plusBtn);
            Button minusBtn = (Button) view.findViewById(R.id.minusBtn);
            final EditText numET = (EditText) view.findViewById(R.id.numET);

            ImageHelper.getInstance().display(img, item1.getLogoImg());
            name.setText(item1.getGoodsTitle());
            desc.setText(item1.getSpecificationDesc());
            price.setText("￥" + item1.getRetailPrice());

            numET.setText(item1.getNum() + "");


            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegate.goodPlus(item1);
                }
            });

            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegate.goodMinus(item1);
                }
            });

            setNumEtListener(numET, position);//文本监听

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.cell_good_two, null);

            LinearLayout good1 = (LinearLayout) view.findViewById(R.id.good1LL);
            LinearLayout good2 = (LinearLayout) view.findViewById(R.id.good2LL);
            good2.setVisibility(View.GONE);

            int index = position - (myGood.size() + 2);


            final ProductDtlBean item1 = topGoodList.get(index * 2);
            ImageView img1 = (ImageView) view.findViewById(R.id.img1IV);
            TextView name1 = (TextView) view.findViewById(R.id.name1TV);
            TextView price1 = (TextView) view.findViewById(R.id.price1TV);

            ImageHelper.getInstance().display(img1, item1.getLogoImg());
            name1.setText(item1.getGoodsTitle());
            price1.setText("￥" + item1.getRetailPrice());
            good1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegate.topGoodClick(item1);
                }
            });

            if (topGoodList.size() > (index * 2 + 1)) {
                final ProductDtlBean item2 = topGoodList.get(index * 2 + 1);
                ImageView img2 = (ImageView) view.findViewById(R.id.img2IV);
                TextView name2 = (TextView) view.findViewById(R.id.name2TV);
                TextView price2 = (TextView) view.findViewById(R.id.price2TV);

                ImageHelper.getInstance().display(img2, item2.getLogoImg());
                name2.setText(item2.getGoodsTitle());
                price2.setText("￥" + item2.getRetailPrice());
                good2.setVisibility(View.VISIBLE);
                good2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delegate.topGoodClick(item2);
                    }
                });
            }
        }

        return view;
    }

    public void setNumEtListener(EditText edit, final int position) {
        handlePosition = position;
        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(runnable);

                if (!CheckUtils.isAvailable(s.toString())){
                    getItem(position).setNum(0);
                }else {
                    getItem(position).setNum(Double.valueOf(s.toString()));
                }


                handler.postDelayed(runnable, 3000);
            }

        });
    }

    public interface PickGoodDelegate {
        void topGoodClick(ProductDtlBean item);

        void goodPlus(ProductDtlBean item);

        void goodMinus(ProductDtlBean item);

        void changQty(ProductDtlBean item);
    }

    private Handler handler = new Handler();//摄像头重启线程
    private Runnable runnable = new Runnable() {//摄像头重启线程方法
        @Override
        public void run() {
            LogUtil.e("afterTextChanged");
            if (delegate != null)
                delegate.changQty(getItem(handlePosition));
        }
    };




}


