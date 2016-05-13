package com.cqgk.clerk.zxing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.zxing.camera.CameraManager;
import com.cqgk.clerk.zxing.decoding.CaptureActivityHandler;
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