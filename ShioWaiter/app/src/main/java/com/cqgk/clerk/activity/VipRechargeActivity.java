package com.cqgk.clerk.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqgk.clerk.BuildConfig;
import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.CardDtlBean;
import com.cqgk.clerk.bean.normal.RechargeResultBean;
import com.cqgk.clerk.config.Constant;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.utils.LogUtil;
import com.cqgk.clerk.view.CommonDialogView;
import com.cqgk.clerk.zxing.CamerBaseActivity;
import com.cqgk.clerk.R;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 会员充值
 * Created by duke on 16/5/11.
 */
@ContentView(R.layout.viprecharge)
public class VipRechargeActivity extends CamerBaseActivity {

    @ViewInject(R.id.scansuccess)
    RelativeLayout scansuccess;

    @ViewInject(R.id.captureroot)
    RelativeLayout captureroot;


    @ViewInject(R.id.cardnum)
    TextView cardnum;

    @ViewInject(R.id.cardmoney)
    TextView cardmoney;

    @ViewInject(R.id.capture_preview)
    SurfaceView capture_preview;

    @ViewInject(R.id.inputmoney)
    EditText inputmoney;

    @ViewInject(R.id.summoney)
    TextView summoney;

    private boolean hasSurface;
    private String card_id;
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
        getTitleDelegate().setTitle("会员充值");

        try {
            card_id = getStringExtra("card_id");
        } catch (NullPointerException e) {

        }


        inputmoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                summoney.setText(String.format("￥%s", inputmoney.getText().toString()));
            }
        });


        if (BuildConfig.DEBUG) {
            //card_id = AppEnter.TestCardid;
        }

        requestData();
    }

    @Override
    public void requestData() {
        super.requestData();
//        if (CheckUtils.isAvailable(card_id)) {
//            loadCardInfo(card_id);
//        }
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


    /**
     * @param cid
     */
    private void loadCardInfo(String cid) {
        RequestUtils.cardInfo(cid, new HttpCallBack<CardDtlBean>() {
            @Override
            public void success(CardDtlBean result) {
                card_id = result.getCard_id();
                scansuccess.setVisibility(View.VISIBLE);
                cardnum.setText(String.format("卡号:%s", result.getCard_id()));
                cardmoney.setText(Html.fromHtml(String.format("余额:<font color=\"red\">￥%s</font>", result.getBalance())));
                captureroot.setVisibility(View.GONE);
            }

            @Override
            public boolean failure(int state, String msg) {
                showToast(msg);
                handler.postDelayed(runnable, 4000);
                return super.failure(state, msg);
            }
        });
    }


    @Override
    public void handleDecode(Result result, Bitmap barcode) {
        super.handleDecode(result, barcode);
        String cid = recode(result.toString());
//        if (BuildConfig.DEBUG)
//            cid = AppEnter.TestCardid;


        loadCardInfo(cid);
    }

    private void checkCard(String card_id) {
        RequestUtils.checkCardState(card_id, new HttpCallBack<String>() {
            @Override
            public void success(String result) {

            }

            @Override
            public boolean failure(int state, String msg) {
                showToast(msg);
                handler.postDelayed(runnable, Constant.CameraRestartTime);
                return super.failure(state, msg);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    protected void reScan() {
        super.reScan();
        handler.removeCallbacks(runnable);
    }

    @Event(R.id.scanagain)
    private void scanagain_click(View view) {
        CommonDialogView.show("你确认删除此张卡片信息?", new CommonDialogView.DialogClickListener() {
            @Override
            public void doConfirm() {
                card_id = "";
                reScan();
                scansuccess.setVisibility(View.GONE);
                captureroot.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }


    @Event(R.id.congnow)
    private void congnow_click(View view) {


        if (!CheckUtils.isAvailable(card_id)) {
            showLongToast("请扫描有效的会员卡");
            return;
        }


        if (!CheckUtils.isAvailable(inputmoney.getText().toString())) {
            showLongToast("请输入您要充值的金额");
            return;
        }

        long imoney = Long.parseLong(inputmoney.getText().toString());
        if (imoney % 100 != 0) {
            showLongToast("充值金额必须为100元的整倍数");
            return;
        }

        if (imoney <= 0) {
            showLongToast("充值金额必须为100元的整倍数");
            return;
        }


        CommonDialogView.show("请确定您收到客户的现金后再进行充值", new CommonDialogView.DialogClickListener() {
            @Override
            public void doConfirm() {
                getRechargeCode();

            }
        }, true, false, "", "继续");
    }

    private void getRechargeCode() {
        //inputmoney.getText().toString()
        RequestUtils.vipRecharge(card_id, "0.01", new HttpCallBack<RechargeResultBean>() {
            @Override
            public void success(RechargeResultBean result) {
                AppEnter.user_msg = result.getUserMsg();
                NavigationHelper.getInstance().startVipPaySelect(result);
            }

            @Override
            public boolean failure(int state, String msg) {
                showLongToast(msg);
                return super.failure(state, msg);
            }
        });
    }
}
