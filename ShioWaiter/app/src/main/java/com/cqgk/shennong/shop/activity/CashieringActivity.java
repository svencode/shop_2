package com.cqgk.shennong.shop.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ListView;

import com.cqgk.shennong.shop.adapter.CashieringAdapter;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.bean.normal.CardDtlBean;
import com.cqgk.shennong.shop.bean.normal.GoodListBean;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestUtils;
import com.cqgk.shennong.shop.zxing.CamerBaseActivity;
import com.cqgk.shennong.shop.R;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.NavigableSet;

/**
 * Created by sven on 16/5/9.
 */

@ContentView(R.layout.activity_cashiering)
public class CashieringActivity extends CamerBaseActivity {

    public static final String MY_GOOD_LIST = "my_good_list";

    @ViewInject(R.id.listView)
    ListView listView;

    @ViewInject(R.id.capture_preview)
    SurfaceView capture_preview;

    private boolean hasSurface;

    private ArrayList<GoodListBean.Item> myGood;

    private CashieringAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("收银记账");

        myGood = (ArrayList<GoodListBean.Item>) getIntent().getSerializableExtra(MY_GOOD_LIST);



        layoutView();
    }

    @Override
    public void handleDecode(Result result, Bitmap barcode) {
        super.handleDecode(result, barcode);
        String recode = recode(result.toString());
        showToast(recode);
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


    private void layoutView() {
        CashieringAdapter adapter = new CashieringAdapter(this);
        listView.setAdapter(adapter);
    }

    private void getVipInfo(String cardId){
        RequestUtils.cardInfo(cardId, new HttpCallBack<CardDtlBean>() {
            @Override
            public void success(CardDtlBean result) {

            }
        });

    }


    @Event(R.id.goPayBtn)
    private void goPay(View view) {

    }
}