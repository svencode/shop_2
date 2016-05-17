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
import com.cqgk.shennong.shop.bean.normal.LoginResultBean;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestHelper;
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
public class CashieringActivity extends CamerBaseActivity implements CashieringAdapter.CashieringDelegate{

    public static final String MY_GOOD_LIST = "my_good_list";

    @ViewInject(R.id.listView)
    ListView listView;

    @ViewInject(R.id.capture_preview)
    SurfaceView capture_preview;

    @ViewInject(R.id.captureroot)
    RelativeLayout captureroot;

    //vip信息
    @ViewInject(R.id.vipInfoLL)
    LinearLayout vipInfoLL;
    @ViewInject(R.id.nameTV)
    TextView vipNameTV;
    @ViewInject(R.id.phontTV)
    TextView phontTV;
    @ViewInject(R.id.blanceTV)
    TextView blanceTV;
    @ViewInject(R.id.cardNumberTV)
    TextView cardNumberTV;
    @ViewInject(R.id.couponTV)
    TextView couponTV;



    @ViewInject(R.id.amountTV)
    TextView amountTV;

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

        refreshPrice();
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
        adapter = new CashieringAdapter(this,this);
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
        captureroot.setVisibility(View.GONE);
        vipInfoLL.setVisibility(View.VISIBLE);

        vipNameTV.setText(vipInfo.getCard_name());
        phontTV.setText(vipInfo.getCard_mobile());
        cardNumberTV.setText("NO." + vipInfo.getCard_id());
        blanceTV.setText("余额：￥" + vipInfo.getBalance());

        couponTV.setVisibility(View.GONE);
    }

    private void delVipInfo(){
        vipInfo = null;
    }

    @Event(R.id.couponTV)
    private void coupon(View view){

    }

    @Event(R.id.rechargeBtn)
    private void recharge(View view){
        double price = 0;
        for (GoodListBean.Item item:myGood){
            price += (item.getNum()*item.getPrice());
        }

        NavigationHelper.getInstance().startVipRecharge(vipInfo.getCard_id());
    }

    @Event(R.id.goPayBtn)
    private void goPay(View view) {
        if (null == vipInfo)return;

        double price = 0;
//        android:text="￥0     共0件"
        for (GoodListBean.Item item:myGood){
            price += (item.getNum()*item.getPrice());
        }

        if (price>vipInfo.getBalance()){
            recharge(null);
        }


        RequestUtils.submitOrder(vipInfo.getCard_id(), "", myGood, new HttpCallBack<LoginResultBean>() {
            @Override
            public void success(LoginResultBean result) {

            }
        });

    }

    private void refreshPrice(){
        int num = 0;
        double price = 0;
//        android:text="￥0     共0件"
        for (GoodListBean.Item item:myGood){
            num += item.getNum();
            price += (item.getNum()*item.getPrice());
        }

        amountTV.setText("￥" +price + "     共"+num+"件");

        if (null!=vipInfo){

        }


    }

    @Override
    public void goodPlus(GoodListBean.Item item){
        for (GoodListBean.Item item1:myGood){
            if (item1.equals(item)){
                item1.setNum(item1.getNum()+1);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();

                refreshPrice();
                return;
            }
        }

    }
    @Override
    public void goodMinus(GoodListBean.Item item){
        for (GoodListBean.Item item1:myGood){
            if (item1.equals(item)) {
                item1.setNum(item1.getNum()-1);
                if (item1.getNum()==0)myGood.remove(item1);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();

                refreshPrice();
                return;
            }
        }

    }
}