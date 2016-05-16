package com.cqgk.shennong.shop.zxing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.utils.LogUtil;
import com.cqgk.shennong.shop.zxing.camera.CameraManager;
import com.cqgk.shennong.shop.zxing.decoding.CaptureActivityHandler;
import com.cqgk.shennong.shop.R;
import com.google.zxing.Result;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 照相继承
 * Created by duke on 16/5/13.
 */
public class CamerBaseActivity extends BusinessBaseActivity
        implements CamerInterface,SurfaceHolder.Callback {
    protected CaptureActivityHandler handler;

    private static final int UPTATE_INTERVAL_TIME = 2000;
    private long lastUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager.init(getApplication());
    }

    public Handler getHandler() {
        return handler;
    }


    @Override
    public void handleDecode(Result result, Bitmap barcode) {
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - lastUpdateTime;
        if (timeInterval < UPTATE_INTERVAL_TIME){
            return;
        }

        lastUpdateTime = currentUpdateTime;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    protected String recode(String str) {
        String formart = "";

        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                    .canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", formart);
            } else {
                formart = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formart;
    }

    protected void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, null,null);
        }
    }

    protected void reScan(){
        if(handler!=null){
            handler.sendEmptyMessage(R.id.restart_preview);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }
}
