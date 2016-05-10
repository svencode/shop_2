package com.cqgk.clerk.activity.product;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.ProductEditItemAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.EditBean;
import com.cqgk.clerk.zxing.ScanListener;
import com.cqgk.clerk.zxing.ScanManager;
import com.cqgk.clerk.zxing.decode.DecodeThread;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 商品编辑或上传
 * Created by duke on 16/5/9.
 */
@ContentView(R.layout.productedit)
public class ProductEdit extends BusinessBaseActivity implements ScanListener {

    @ViewInject(R.id.selview)
    GridView selview;

    @ViewInject(R.id.capture_preview)
    SurfaceView capture_preview;

    @ViewInject(R.id.captureroot)
    RelativeLayout captureroot;

    @ViewInject(R.id.capture_scan_line)
    ImageView capture_scan_line;

    @ViewInject(R.id.parentrel)
    RelativeLayout parentrel;


    @ViewInject(R.id.capture_crop_view)
    RelativeLayout capture_crop_view;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private List<EditBean> editBeanList;
    private ProductEditItemAdapter productEditItemAdapter;
    ScanManager scanManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("商品上传");

        editBeanList = new ArrayList<>();
        editBeanList.add(new EditBean());


        initView();
    }

    @Override
    public void initView() {
        super.initView();
        //构造出扫描管理器
        scanManager = new ScanManager(this, capture_preview,
                parentrel, capture_crop_view,
                capture_scan_line, DecodeThread.ALL_MODE, this);


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
    public void scanError(Exception e) {
        showToast(e.getMessage());
    }

    @Override
    public void scanResult(Result rawResult, Bundle bundle) {

        showToast(rawResult.getText());

    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }


}
