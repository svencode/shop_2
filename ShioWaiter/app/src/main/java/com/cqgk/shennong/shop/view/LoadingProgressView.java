package com.cqgk.shennong.shop.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.cqgk.shennong.shop.R;


public class LoadingProgressView extends Dialog {

    public LoadingProgressView(Context context) {
        super(context, R.style.LodingDialog);
    }

    public LoadingProgressView(Context context, int theme) {
        super(context, R.style.LodingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setContentView(R.layout.loading_progress_dialog);

    }

}