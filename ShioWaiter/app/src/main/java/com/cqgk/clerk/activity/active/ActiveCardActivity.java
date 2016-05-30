package com.cqgk.clerk.activity.active;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqgk.clerk.BuildConfig;
import com.cqgk.clerk.R;
import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.bean.normal.CardDtlBean;
import com.cqgk.clerk.bean.normal.MembercardActBean;
import com.cqgk.clerk.bean.normal.RechargeResultBean;
import com.cqgk.clerk.config.Constant;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.AbStrUtil;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.utils.DisplayUtil;
import com.cqgk.clerk.utils.LogUtil;
import com.cqgk.clerk.view.CommonDialogView;
import com.cqgk.clerk.zxing.CamerBaseActivity;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 开卡充值
 * Created by duke on 16/5/11.
 */
@ContentView(R.layout.activecard)
public class ActiveCardActivity extends CamerBaseActivity implements TextWatcher {

    @ViewInject(R.id.scansuccess)
    RelativeLayout scansuccess;

    @ViewInject(R.id.captureroot)
    RelativeLayout captureroot;

    @ViewInject(R.id.capture_preview)
    SurfaceView capture_preview;

    @ViewInject(R.id.cardnum)
    TextView cardnum;

    @ViewInject(R.id.cardmoney)
    TextView cardmoney;



    @ViewInject(R.id.memeber_name)
    EditText memeber_name;

    @ViewInject(R.id.row_4_pwd)
    EditText row_4_pwd;


    @ViewInject(R.id.phone)
    EditText phone;

    @ViewInject(R.id.card_idcard)
    EditText card_idcard;

    @ViewInject(R.id.opencard)
    Button opencard;

    @ViewInject(R.id.inputarea)
    LinearLayout inputarea;


    @ViewInject(R.id.row_1_title)
    TextView row_1_title;
    @ViewInject(R.id.row_2_title)
    TextView row_2_title;
    @ViewInject(R.id.row_3_title)
    TextView row_3_title;
    @ViewInject(R.id.row_4_title)
    TextView row_4_title;

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
        getTitleDelegate().setTitle("会员开卡");

        row_1_title.setText(Html.fromHtml("会员姓名<font color=\"red\">*<font/>"));
        row_2_title.setText(Html.fromHtml("会员手机号<font color=\"red\">*<font/>"));
        //row_3_title.setText(Html.fromHtml("会员身份证号<font color=\"red\">*<font/>"));
        row_4_title.setText(Html.fromHtml("密码<font color=\"red\">*<font/>"));
        opencard.setEnabled(false);


        memeber_name.addTextChangedListener(this);
        phone.addTextChangedListener(this);
        row_4_pwd.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (CheckUtils.isAvailable(memeber_name.getText().toString()) &&
                CheckUtils.isAvailable(phone.getText().toString()) &&
                CheckUtils.isAvailable(row_4_pwd.getText().toString())) {
            opencard.setBackgroundColor(getResources().getColor(R.color.font_color_1));
            opencard.setEnabled(true);
        }
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void handleDecode(Result result, Bitmap barcode) {
        super.handleDecode(result, barcode);

        String cid = recode(result.toString());
        if (BuildConfig.DEBUG)
            cid = AppEnter.TestCardid;

        card_id = cid;


        RequestUtils.checkCardState(cid, new HttpCallBack<String>() {
            @Override
            public void success(String result,String msg) {
                scansuccess.setVisibility(View.VISIBLE);
                cardnum.setText(String.format("卡号:%s", card_id));
                cardmoney.setText(Html.fromHtml(String.format("余额:<font color=\"red\">￥%s</font>", 0)));
                captureroot.setVisibility(View.GONE);
                opencard.setVisibility(View.VISIBLE);
                setListTop(140);
            }

            /**
             * 200：可用
             601：会员卡不存在
             602：会员卡已被使用
             * @param state
             * @param msg
             * @return
             */
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

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Event(R.id.scanagain)
    private void scanagain_click(View view) {
        CommonDialogView.show("你确认删除此张卡片信息?", new CommonDialogView.DialogClickListener() {
            @Override
            public void doConfirm() {
                reScan();
                scansuccess.setVisibility(View.GONE);
                captureroot.setVisibility(View.VISIBLE);
                memeber_name.setText("");
                phone.setText("");
                row_4_pwd.setText("");
                setListTop(250);
            }
        });
    }



    @Event(R.id.opencard)
    private void opencard_click(View view) {

        if (!CheckUtils.isAvailable(memeber_name.getText().toString())) {
            showLongToast("请输入会员姓名");
            return;
        }

        if(!AbStrUtil.isChinese(memeber_name.getText().toString())){
            showLongToast("抱歉,姓名只允许中文");
            return;
        }

        if (!CheckUtils.isAvailable(phone.getText().toString())) {
            showLongToast("请输入手机号码");
            return;
        }

        if(phone.getText().toString().length()!=11){
            showLongToast("抱歉,请输入11位手机号码");
            return;
        }

        if (!CheckUtils.isAvailable(row_4_pwd.getText().toString())) {
            showLongToast("请输入6位密码");
            return;
        }

        if (row_4_pwd.getText().toString().trim().length() != 6) {
            showLongToast("请输入6位密码");
            return;
        }

        CommonDialogView.show(String.format("请跟客户确认手机号如下\n%s",
                phone.getText().toString()),
                new CommonDialogView.DialogClickListener() {
                    @Override
                    public void doConfirm() {
                        openVipCard();
                    }
                }, true, false, "返回修改", "确定开通");

    }

    private void openVipCard() {
        RequestUtils.membercardAct(card_id,
                memeber_name.getText().toString(),
                phone.getText().toString(),
                card_idcard.getText().toString(),
                row_4_pwd.getText().toString(), new HttpCallBack<MembercardActBean>() {
                    @Override
                    public void success(MembercardActBean result,String msg) {
                        CommonDialogView.show("卡片信息已完成绑定\n还需要充值100元才能成功开通",
                                new CommonDialogView.DialogClickListener() {
                                    @Override
                                    public void doConfirm() {
                                        NavigationHelper.getInstance().startVipRecharge(card_id);
                                        //getRechargeCode();
                                    }
                                }, true, false, "取消", "去充值");
                    }

                    @Override
                    public boolean failure(int state, String msg) {
                        showLongToast(msg);
                        return super.failure(state, msg);
                    }
                });
    }

    private void getRechargeCode() {
        RequestUtils.vipRecharge(card_id, "0.01", new HttpCallBack<RechargeResultBean>() {
            @Override
            public void success(RechargeResultBean result,String msg) {
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

    private void setListTop(int dp) {
        android.view.ViewGroup.LayoutParams lp = inputarea.getLayoutParams();
        ((FrameLayout.LayoutParams)lp).setMargins(0, DisplayUtil.dip2px(dp),0,0);
    }
}
