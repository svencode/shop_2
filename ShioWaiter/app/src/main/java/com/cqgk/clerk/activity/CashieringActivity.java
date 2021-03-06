package com.cqgk.clerk.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqgk.clerk.BuildConfig;
import com.cqgk.clerk.adapter.CashieringAdapter;
import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.CardDtlBean;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.JIesuanReturnBean;
import com.cqgk.clerk.bean.normal.LoginResultBean;
import com.cqgk.clerk.bean.normal.OrderSubmitResultBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.config.Constant;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestHelper;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.utils.DisplayUtil;
import com.cqgk.clerk.utils.LogUtil;
import com.cqgk.clerk.view.CommonDialogView;
import com.cqgk.clerk.view.PayPwdDialogView;
import com.cqgk.clerk.zxing.CamerBaseActivity;
import com.cqgk.clerk.R;
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
public class CashieringActivity extends CamerBaseActivity implements CashieringAdapter.CashieringDelegate {

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

    @ViewInject(R.id.product_code_num)
    EditText product_code_num;


    private boolean hasSurface;

    private ArrayList<ProductDtlBean> myGood;

    private CashieringAdapter adapter;

    private JIesuanReturnBean.MembercardBean vipBean;
    private JIesuanReturnBean vipInfo;


    private String couponNumber;

    private Runnable runnable = new Runnable() {//摄像头重启线程方法
        @Override
        public void run() {
            LogUtil.e("camberRestart");
            reScan();
        }
    };

//    private boolean isOpenCamer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("收银记账");


        myGood = (ArrayList<ProductDtlBean>) getIntent().getSerializableExtra(MY_GOOD_LIST);


        if (BuildConfig.DEBUG)
            getVipInfo(AppEnter.TestCardid, couponNumber);

        layoutView();
        refreshPrice();
        beginCamcer();

    }

    @Override
    public void handleDecode(Result result, Bitmap barcode) {
        super.handleDecode(result, barcode);
        if (null != vipBean) {
            return;
        }

        closeCamera();
        String recode = recode(result.toString());
        getVipInfo(recode, couponNumber);
    }


    @Override
    public void onResume() {
        super.onResume();
        beginCamcer();
    }

    private void beginCamcer() {
        if (null != vipBean) return;
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

        product_code_num.requestFocus();
        product_code_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
                    String recode = textView.getText().toString();
                    getVipInfo(recode, couponNumber);
                }
                return false;
            }
        });

        adapter = new CashieringAdapter(this, this);
        adapter.setMyGood(myGood);
        listView.setAdapter(adapter);
    }

    private void getVipInfo(final String cardId, final String couponId) {

        RequestUtils.settleReCalculate(cardId, couponId, myGood, new HttpCallBack<JIesuanReturnBean>() {
            @Override
            public void success(JIesuanReturnBean result, String msg) {

                if (CheckUtils.isAvailable(msg)) {
                    if (!msg.equals("成功")) {
                        showToast(msg);
                    }

                }
                couponNumber = couponId;
                showVipInfo(result);
            }

            @Override
            public boolean failure(int state, String msg) {
                showToast(cardId + " " + msg);
                beginCamcer();
                product_code_num.setText("");
                return super.failure(state, msg);
            }
        });
    }

    @Override
    protected void reScan() {
        super.reScan();
        handler.removeCallbacks(runnable);
    }

    private void showVipInfo(JIesuanReturnBean vipInfo) {
        if (vipInfo == null) return;
        this.vipInfo = vipInfo;
        if (null != vipInfo.getMembercard()) {
            vipBean = vipInfo.getMembercard();
            captureroot.setVisibility(View.GONE);
            vipInfoLL.setVisibility(View.VISIBLE);


//
            vipNameTV.setText(vipInfo.getMembercard().getName());
            phontTV.setText(vipInfo.getMembercard().getPhoneNumber());
            cardNumberTV.setText("NO." + vipInfo.getMembercard().getBarCode());

            SpannableString blance = null;
            if (Double.parseDouble(vipInfo.getMembercard().getBalance()) < Double.parseDouble(vipInfo.getAmountMap().getTotalAmount())) {
                blance = new SpannableString("余额不足：￥" + vipInfo.getMembercard().getBalance());
            } else {
                blance = new SpannableString("余额：￥" + vipInfo.getMembercard().getBalance());
            }

//            blance.setSpan(new ForegroundColorSpan(Color.parseColor("#ec584e")),"余额：￥".length(),(vipInfo.getMembercard().getBalance()+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            blanceTV.setText(blance);


        } else {
            //没有会员信息 重启摄像头
            beginCamcer();
        }

        if (vipInfo.isIsAvailable()) {
            couponTV.setVisibility(View.VISIBLE);
            couponTV.setText("现金券抵扣：" + vipInfo.getFaceValue() + "元");
            setListTop(180);
        } else {
            couponTV.setVisibility(View.GONE);

            couponNumber = null;
            if (null == vipBean) {
                setListTop(250);
            } else {
                setListTop(140);
            }
        }


        HashMap<String, String> newPrice = vipInfo.getAmountMap().getNewGoodsPrice();

        for (String key : newPrice.keySet()) {


            for (ProductDtlBean good : myGood) {

                if (good.getId().equals(key)) {
                    good.setReturnPrice(Double.parseDouble(newPrice.get(key)));
                }
            }
        }

        adapter.notifyDataSetChanged();
        refreshPrice();
    }


    @Event(R.id.cleanIB)
    private void delVipInfo(View view) {
        vipBean = null;
        vipInfo = null;
        captureroot.setVisibility(View.VISIBLE);
        vipInfoLL.setVisibility(View.INVISIBLE);
        couponNumber = null;
        for (ProductDtlBean good : myGood) {
            good.setReturnPrice(0);
            good.setUserPrice(0);
        }
        getVipInfo(null, null);

        beginCamcer();

    }

    private void setListTop(int dp) {
        android.view.ViewGroup.LayoutParams lp = vipInfoLL.getLayoutParams();
        lp.height = DisplayUtil.dip2px(dp);
    }

    @Event(R.id.couponBtn)
    private void coupon(View view) {
        NavigationHelper.getInstance().startCouponScan();
    }

    @Event(R.id.rechargeBtn)
    private void recharge(View view) {

        NavigationHelper.getInstance().startVipRecharge(vipBean.getBarCode());
    }

    @Event(R.id.goPayBtn)
    private void goPay(View view) {
//        if (null == vipInfo)return;

        double price = 0;
//        android:text="￥0     共0件"
        for (ProductDtlBean item : myGood) {
            if (item.getUserPrice() > 0) {
                price += (item.getNum() * item.getUserPrice());
            } else if (item.getReturnPrice() > 0) {
                price += (item.getNum() * item.getReturnPrice());
            } else {
                price += (item.getNum() * item.getRetailPrice());
            }

        }

        if (null != vipBean) {
            double coupon = 0;
            if (null != vipInfo && null != vipInfo.getFaceValue())
                coupon = Double.parseDouble(vipInfo.getFaceValue());
            if (price > (Double.parseDouble(vipBean.getBalance()) + coupon)) {
//                recharge(null);
                showToast("余额不足，请先充值");
                return;
            }

        }

        if (0 == myGood.size()) {
            showToast("未选择商品");
            return;
        }

        String vipNo = null;
        if (null != vipBean) {
            vipNo = vipBean.getId();

            //大于100要输入支付密码
            if (price > 100) {
                PayPwdDialogView.show(vipBean.getId(), new PayPwdDialogView.PwdDialogClickListener() {
                    @Override
                    public void doConfirm(String text) {
                        payRequest(vipBean.getId());
                    }
                }, true, "取消", "确定");

                return;
            }

        }

        if (vipNo == null) {
            CommonDialogView.show("请确认已收到客户的现金再进行结算", new CommonDialogView.DialogClickListener() {
                @Override
                public void doConfirm() {
                    payRequest(null);

                }
            }, true, false, "", "继续");
            return;
        }

        payRequest(vipNo);

    }

    /**
     * 提交订单
     *
     * @param vipNo
     */
    private void payRequest(String vipNo) {
        LogUtil.e(String.format("___________couponNumber:%s,vipNo:%s", couponNumber, vipNo));
        RequestUtils.submitOrder(vipNo, couponNumber, myGood, new HttpCallBack<OrderSubmitResultBean>() {
            @Override
            public void success(OrderSubmitResultBean result, String msg) {
                setResult(999);
                finish();
                boolean isVipPay = (null != vipBean);

                NavigationHelper.getInstance().startOrderResult(result, isVipPay);
            }

            @Override
            public boolean failure(int state, String msg) {
                showToast(msg);
                return super.failure(state, msg);
            }
        });
    }

    private void refreshPrice() {
        double num = 0;
        double price = 0;
//        android:text="￥0     共0件"
        for (ProductDtlBean item : myGood) {
            num += item.getNum();
            if (item.getReturnPrice() > 0) {
                price += (item.getNum() * item.getReturnPrice());
            } else {
                price += (item.getNum() * item.getRetailPrice());
            }

        }

        if (null != vipBean && null != vipInfo.getAmountMap() && null != vipInfo.getAmountMap().getTotalAmount()) {
            double total = Double.parseDouble(vipInfo.getAmountMap().getTotalAmount());
            amountTV.setText(Html.fromHtml(String.format("￥<font color=\"red\">%.2f</font>     共<font color=\"red\">%s</font>件", (total >= 0 ? total : 0), num)));
            //amountTV.setText("￥" + (total >= 0 ? total : 0) + "     共" + num + "件");
        } else {
            amountTV.setText(Html.fromHtml(String.format("￥<font color=\"red\">%.2f</font>     共<font color=\"red\">%s</font>件", price, num)));
            //amountTV.setText("￥" + price + "     共" + num + "件");
        }


    }

    @Override
    public void goodPlus(ProductDtlBean item) {
        for (ProductDtlBean item1 : myGood) {
            if (item1.equals(item)) {
                item1.setNum(item1.getNum() + 1);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();

                refreshPrice();

                getVipInfo(null == vipBean ? null : vipBean.getBarCode(), couponNumber);
                return;
            }
        }

    }

    @Override
    public void goodMinus(ProductDtlBean item) {
        for (ProductDtlBean item1 : myGood) {
            if (item1.equals(item)) {
                item1.setNum(item1.getNum() - 1);
                if (item1.getNum() <= 0) myGood.remove(item1);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();

                refreshPrice();
                getVipInfo(null == vipBean ? null : vipBean.getBarCode(), couponNumber);
                return;
            }
        }

    }

    @Override
    public void goodPriceEdit(ProductDtlBean item, double newPrice) {
        for (ProductDtlBean item1 : myGood) {
            if (item1.equals(item)) {
                item1.setUserPrice(newPrice);

                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();
                refreshPrice();
                getVipInfo(null == vipBean ? null : vipBean.getBarCode(), couponNumber);
                return;
            }
        }
    }

    @Override
    public void goodNunEdit(ProductDtlBean item, double num) {
        for (ProductDtlBean item1 : myGood) {
            if (item1.equals(item)) {
                item1.setNum(num);
                if (0 == num) myGood.remove(item);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();
                refreshPrice();
                getVipInfo(null == vipBean ? null : vipBean.getBarCode(), couponNumber);
                return;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 99) {
            String couponcode = data.getStringExtra("couponcode");
            getVipInfo(null == vipBean ? null : vipBean.getBarCode(), couponcode);
        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(GoonReceiver);
//    }
//
//
//    private void registBroadcast(){
//        IntentFilter intentFilter = new IntentFilter(PayResultActivity.BROADCAST_GOON_BUY);
//
//        //注册广播
//        this.registerReceiver(GoonReceiver, intentFilter);
//
//    }
//
//    private BroadcastReceiver GoonReceiver= new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            CashieringActivity.this.finish();
//        }
//    };

}