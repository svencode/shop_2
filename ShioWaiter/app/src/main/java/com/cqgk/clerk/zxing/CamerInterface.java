package com.cqgk.clerk.zxing;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * Created by duke on 16/5/13.
 */
public  interface  CamerInterface {
    public void handleDecode(final Result result, Bitmap barcode);
}
