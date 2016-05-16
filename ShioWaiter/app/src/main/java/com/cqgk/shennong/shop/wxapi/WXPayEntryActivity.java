package com.cqgk.shennong.shop.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cqgk.shennong.shop.R;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.config.Constant;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 微信支付回调
 * Created by duke on 15/12/24.
 */
@ContentView(R.layout.wxpayresult)
public class WXPayEntryActivity extends BusinessBaseActivity
        implements IWXAPIEventHandler {

    public static String TAG = "paytag";
    protected IWXAPI api;

    @ViewInject(R.id.showmsgicon)
    TextView showmsgicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("支付结果");
        getTitleDelegate().setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        api = WXAPIFactory.createWXAPI(this, Constant.wxAppid);

        initView();
        api.handleIntent(getIntent(), this);


        //不通过微信支付已支付成功检测(如:积分支付,钱包支付)
        String result = getStringExtra(TAG);
        if (result != null && result.equals("trading_success")) {//支付成功

        }
    }

    protected static Context mContext;
    public static Handler handler = new Handler(new Handler.Callback() {

        //		msg.what== 0 ：表示支付成功
//		msg.what== -1 ：表示支付失败
//		msg.what== -2 ：表示取消支付
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub

            switch (msg.what) {
                case 800://商户订单号重复或生成错误

                    break;
                case 0://支付成功
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    break;
                case -1://支付失败
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case -2://取消支付
                    Toast.makeText(mContext, "取消支付", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    public void initView() {
        super.initView();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }


    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case 0://支付成功
                showToast("充值成功");
                showmsgicon.setText("充值成功");
                break;
            case -1://支付失败
                showToast("支付失败");
                showmsgicon.setText("支付失败");
                break;
            case -2://取消支付
                showToast("取消支付");
                showmsgicon.setText("取消支付");
                break;
        }
    }


}
