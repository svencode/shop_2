package com.cqgk.clerk.activity.active;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.EditBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.utils.CheckUtils;
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
public class ActiveCard extends CamerBaseActivity {

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


    @ViewInject(R.id.scanagain)
    ImageView scanagain;

    @ViewInject(R.id.memeber_name)
    EditText memeber_name;

    @ViewInject(R.id.row_4_pwd)
    EditText row_4_pwd;

    @ViewInject(R.id.opencard)
    ImageView opencard;

    @ViewInject(R.id.phone)
    EditText phone;

    private boolean hasSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("会员开卡");
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

    private void reScan() {

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
        String recode = recode(result.toString());
        scansuccess.setVisibility(View.VISIBLE);
        cardnum.setText(String.format("卡号:%s", recode));
        cardmoney.setText(Html.fromHtml(String.format("余额:<font color=\"red\">￥%s</font>", 0)));
        captureroot.setVisibility(View.GONE);
        opencard.setVisibility(View.VISIBLE);
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

    @Event(R.id.scanagain)
    private void scanagain_click(View view) {
        CommonDialogView.show("你确认删除此张卡片信息?", new CommonDialogView.DialogClickListener() {
            @Override
            public void doConfirm() {
                initCamera(capture_preview.getHolder());
                scansuccess.setVisibility(View.GONE);
                captureroot.setVisibility(View.VISIBLE);
            }
        });

    }

    @Event(R.id.opencard)
    private void opencard_click(View view) {
        if (!CheckUtils.isAvailable(memeber_name.getText().toString())) {
            showLongToast("请输入会员姓名");
            return;
        }

        if (!CheckUtils.isAvailable(phone.getText().toString())) {
            showLongToast("请输入手机号码");
            return;
        }

        if (!CheckUtils.isAvailable(row_4_pwd.getText().toString())) {
            showLongToast("请输入6位密码");
            return;
        }

        CommonDialogView.show(String.format("请跟客户确认手机号如下\n%s",
                phone.getText().toString()),
                new CommonDialogView.DialogClickListener() {
                    @Override
                    public void doConfirm() {
                        CommonDialogView.show("卡片信息已完成绑定\n还需要充值100元才能成功开通",
                                new CommonDialogView.DialogClickListener() {
                                    @Override
                                    public void doConfirm() {
                                        NavigationHelper.getInstance().startInputMoney();
                                    }
                                }, true, false, "取消", "去充值");
                    }
                }, true, false, "返回修改", "确定开通");

    }
}