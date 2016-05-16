package com.cqgk.shennong.shop.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.cqgk.shennong.shop.view.CommonDialogView;
import com.cqgk.shennong.shop.zxing.CamerBaseActivity;
import com.cqgk.shennong.shop.R;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by duke on 16/5/14.
 */
@ContentView(R.layout.scanproduct)
public class ScanProductActivity extends CamerBaseActivity {

    @ViewInject(R.id.capture_preview)
    SurfaceView capture_preview;
    private boolean hasSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("条码扫描");
        getTitleDelegate().setRightText("确定");
        getTitleDelegate().setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("此功能还没开放");
            }
        });

        getTitleDelegate().setLeftText("取消");
        getTitleDelegate().setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonDialogView.show("取消后您已扫描的商品不会被添加.是否继续?", new CommonDialogView.DialogClickListener() {
                    @Override
                    public void doConfirm() {
                          finish();
                    }
                },true,false,"","继续");
            }
        });
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
    public void handleDecode(Result result, Bitmap barcode) {
        super.handleDecode(result, barcode);
        String recode = recode(result.toString());
        showLongToast(String.format("扫描结果:%s", recode));
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
}
