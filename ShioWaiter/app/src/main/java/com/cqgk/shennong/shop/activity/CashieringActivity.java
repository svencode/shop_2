package com.cqgk.shennong.shop.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqgk.shennong.shop.adapter.CashieringAdapter;
import com.cqgk.shennong.shop.base.AppEnter;
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

    @ViewInject(R.id.captureroot)
    RelativeLayout captureroot;

    @ViewInject(R.id.vipInfoLL)
    LinearLayout vipInfoLL;
    @ViewInject(R.id.nameTV)
    TextView vipNameTV;
    @ViewInject(R.id.phontTV)
    TextView phontTV;


    private boolean hasSurface;

    private ArrayList<GoodListBean.Item> myGood;

    private CashieringAdapter adapter;

    private CardDtlBean vipInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("收银记账");

        myGood = (ArrayList<GoodListBean.Item>) getIntent().getSerializableExtra(MY_GOOD_LIST);


        getVipInfo("010148920000001"/*AppEnter.TestCardid*/);

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
        adapter = new CashieringAdapter(this);
        adapter.setMyGood(myGood);
        listView.setAdapter(adapter);
    }

    private void getVipInfo(String cardId){
        RequestUtils.cardInfo(cardId, new HttpCallBack<CardDtlBean>() {
            @Override
            public void success(CardDtlBean result) {
                vipInfo = result;
                showVipInfo();
            }
        });

    }

    private void showVipInfo(){

    }

    private void delVipInfo(){
        vipInfo = null;
    }


    @Event(R.id.goPayBtn)
    private void goPay(View view) {

    }
}