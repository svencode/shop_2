package com.cqgk.clerk.activity.product;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baoyz.actionsheet.ActionSheet;
import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.ProductEditItemAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.EditBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.zxing.CamerBaseActivity;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 商品编辑或上传
 * Created by duke on 16/5/9.
 */
@ContentView(R.layout.productedit)
public class ProductEditActivity extends CamerBaseActivity {

    @ViewInject(R.id.selview)
    GridView selview;

    @ViewInject(R.id.capture_preview)
    SurfaceView capture_preview;


    @ViewInject(R.id.productcode)
    EditText productcode;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private List<EditBean> editBeanList;
    private ProductEditItemAdapter productEditItemAdapter;


    private boolean hasSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("商品上传");
        getTitleDelegate().setRightText("商品");
        getTitleDelegate().setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationHelper.getInstance().startSearchProduct();
            }
        });

        editBeanList = new ArrayList<>();
        editBeanList.add(new EditBean());


        initView();
    }

    @Override
    public void initView() {
        super.initView();


        productEditItemAdapter = new ProductEditItemAdapter(this, editBeanList);
        selview.setAdapter(productEditItemAdapter);
        selview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditBean editBean = productEditItemAdapter.getItem(i);
                if (editBean.getPhotoInfo() == null) {
                    startActionSheet();
                }
            }
        });
    }

    private void startActionSheet() {
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消(Cancel)")
                .setOtherButtonTitles("打开相册(Open Gallery)", "拍照(Camera)")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        String path = "/sdcard/pk1-2.jpg";
                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, 8, mOnHanlderResultCallback);
                                break;
                            case 1:
                                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                editBeanList = new ArrayList<>();
                editBeanList.add(new EditBean());
                for (PhotoInfo item : resultList) {
                    EditBean temp = new EditBean();
                    temp.setPhotoInfo(item);
                    editBeanList.add(temp);
                }
                productEditItemAdapter.setValueList(editBeanList);
                productEditItemAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            showToast("errorMsg");
        }
    };

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
        String recode = recode(result.toString());
        productcode.setText(recode);
        reScan();
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
