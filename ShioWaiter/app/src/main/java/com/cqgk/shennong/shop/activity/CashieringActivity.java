package com.cqgk.shennong.shop.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqgk.shennong.shop.BuildConfig;
import com.cqgk.shennong.shop.adapter.CashieringAdapter;
import com.cqgk.shennong.shop.base.AppEnter;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.bean.normal.CardDtlBean;
import com.cqgk.shennong.shop.bean.normal.GoodListBean;
import com.cqgk.shennong.shop.bean.normal.JIesuanReturnBean;
import com.cqgk.shennong.shop.bean.normal.LoginResultBean;
import com.cqgk.shennong.shop.bean.normal.OrderSubmitResultBean;
import com.cqgk.shennong.shop.bean.normal.ProductDtlBean;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestHelper;
import com.cqgk.shennong.shop.http.RequestUtils;
import com.cqgk.shennong.shop.utils.CheckUtils;
import com.cqgk.shennong.shop.zxing.CamerBaseActivity;
import com.cqgk.shennong.shop.R;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

    private ArrayList<ProductDtlBean> myGood;

    private CashieringAdapter adapter;

    private JIesuanReturnBean vipInfo;

    private String vipNumber;
    private String couponNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("收银记账");


        myGood = (ArrayList<ProductDtlBean>) getIntent().getSerializableExtra(MY_GOOD_LIST);


        if(BuildConfig.DEBUG)
           getVipInfo(AppEnter.TestCardid,couponNumber);



        layoutView();
        refreshPrice();
    }

    @Override
    public void handleDecode(Result result, Bitmap barcode) {
        super.handleDecode(result, barcode);
        String recode = recode(result.toString());
        if(BuildConfig.DEBUG){
            recode = AppEnter.TestCardid;
        }
        getVipInfo(recode,couponNumber);
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

    private void getVipInfo(String cardId,String couponId){
        vipNumber = cardId;
        couponNumber = couponId;
        RequestUtils.settleReCalculate(cardId, couponId, myGood, new HttpCallBack<JIesuanReturnBean>() {
            @Override
            public void success(JIesuanReturnBean result) {
                vipInfo = result;
                showVipInfo();
            }
        });
    }


    private void showVipInfo(){

        if (null != vipInfo.getMembercard()){
            captureroot.setVisibility(View.GONE);
            vipInfoLL.setVisibility(View.VISIBLE);

            vipNameTV.setText(vipInfo.getMembercard().getName());
            phontTV.setText(vipInfo.getMembercard().getPhoneNumber());
            cardNumberTV.setText("NO." + vipInfo.getMembercard().getBarCode());

            SpannableString blance = new SpannableString("余额：￥" + vipInfo.getMembercard().getBalance());
//            blance.setSpan(new ForegroundColorSpan(Color.parseColor("#ec584e")),"余额：￥".length(),(vipInfo.getMembercard().getBalance()+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            blanceTV.setText(blance);
        }

        if (vipInfo.isIsAvailable()){
            couponTV.setVisibility(View.VISIBLE);
            couponTV.setText("现金券抵扣："+vipInfo.getFaceValue()+"元");
        }





        HashMap<String,String> newPrice = vipInfo.getAmountMap().getNewGoodsPrice();

        for (String key:newPrice.keySet()){


            for (ProductDtlBean good:myGood){
                Log.e("good-----",key+"-----"+good.getId());
                if (good.getId().equals(key)){
                    good.setVipPrice(Double.parseDouble(newPrice.get(key)));
                    Log.e("price-----",good.toString()+"-----"+newPrice.get(key));
                }
            }
        }

        adapter.notifyDataSetChanged();
        refreshPrice();
    }

    private void delVipInfo(){
        vipInfo = null;
    }

    @Event(R.id.couponBtn)
    private void coupon(View view){
        NavigationHelper.getInstance().startCouponScan();
    }

    @Event(R.id.rechargeBtn)
    private void recharge(View view){
        double price = 0;
        for (ProductDtlBean item:myGood){
            price += (item.getNum()*item.getPrice());
        }

        NavigationHelper.getInstance().startVipRecharge(vipInfo.getMembercard().getBarCode());
    }

    @Event(R.id.goPayBtn)
    private void goPay(View view) {
        if (null == vipInfo)return;

        double price = 0;
//        android:text="￥0     共0件"
        for (ProductDtlBean item:myGood){
            price += (item.getNum()*item.getPrice());
        }

        if (price>Double.parseDouble(vipInfo.getMembercard().getBalance())){
            recharge(null);
            return;
        }


        RequestUtils.submitOrder(vipInfo.getMembercard().getId(), couponNumber, myGood, new HttpCallBack<OrderSubmitResultBean>() {
            @Override
            public void success(OrderSubmitResultBean result) {
                NavigationHelper.getInstance().startOrderResult(result);
            }
        });

    }

    private void refreshPrice(){
        double num = 0;
        double price = 0;
//        android:text="￥0     共0件"
        for (ProductDtlBean item:myGood){
            num += item.getNum();
            if (item.getVipPrice()>0){
                price += (item.getNum()*item.getVipPrice());
            }else {
                price += (item.getNum()*item.getRetailPrice());
            }

        }

        if (null != vipInfo && null!= vipInfo.getAmountMap()){
            amountTV.setText("￥" +vipInfo.getAmountMap().getTotalAmount() + "     共"+num+"件");
        }else {
            amountTV.setText("￥" +price + "     共"+num+"件");
        }



        if (null!=vipInfo){

        }


    }

    @Override
    public void goodPlus(ProductDtlBean item){
        for (ProductDtlBean item1:myGood){
            if (item1.equals(item)){
                item1.setNum(item1.getNum()+1);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();

                refreshPrice();

                getVipInfo(vipNumber,couponNumber);
                return;
            }
        }

    }
    @Override
    public void goodMinus(ProductDtlBean item){
        for (ProductDtlBean item1:myGood){
            if (item1.equals(item)) {
                item1.setNum(item1.getNum()-1);
                if (item1.getNum()==0)myGood.remove(item1);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();

                refreshPrice();
                getVipInfo(vipNumber,couponNumber);
                return;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            String couponcode = data.getStringExtra("couponcode");
            getVipInfo(vipNumber,couponcode);
        }
    }
}