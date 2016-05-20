package com.cqgk.clerk.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.bean.normal.MeProductListBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.LogUtil;
import com.cqgk.clerk.view.SearchResultPopView;
import com.cqgk.clerk.zxing.CamerBaseActivity;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

/**
 * Created by duke on 16/5/17.
 */
@ContentView(R.layout.barcodefindcash)
public class BarCodeFindCashActivity extends CamerBaseActivity {


    @ViewInject(R.id.capture_preview)
    SurfaceView capture_preview;

    private boolean hasSurface;

    private static final int UPTATE_INTERVAL_TIME = 4000;
    private long lastUpdateTime;

    private int showType = 0;//0-编辑商品1-返回商品


    //private SearchResultPopView searchResultPopView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("现金卷扫描");

        try {
            showType = getIntent().getIntExtra("showtype", 0);
        } catch (NullPointerException e) {

        }

//        searchResultPopView = new SearchResultPopView(this);
//        searchResultPopView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ProductDtlBean productDtlBean = searchResultPopView.getAdapter().getItem(i);
//                if (showType == 0) {
//                    NavigationHelper.getInstance().startUploadProduct(productDtlBean.getGoodsId());
//                } else {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("dtl", (Serializable) productDtlBean);
//                    Intent intent = new Intent();
//                    intent.putExtras(bundle);
//                    setResult(1, intent);
//                    finish();
//
//                }
//            }
//        });
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
        onPause();
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - lastUpdateTime;
        if (timeInterval < UPTATE_INTERVAL_TIME) {
            return;
        }

        lastUpdateTime = currentUpdateTime;




        String bar_code = recode(result.toString());
        Intent intent = new Intent();
        intent.putExtra("couponcode", bar_code);
        setResult(99, intent);
        finish();

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
