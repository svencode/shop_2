package com.cqgk.shennong.shop.activity.product;

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
import com.cqgk.shennong.shop.adapter.ProductEditItemAdapter;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.bean.normal.EditBean;
import com.cqgk.shennong.shop.bean.normal.FileUploadResultBean;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestUtils;
import com.cqgk.shennong.shop.utils.AppUtil;
import com.cqgk.shennong.shop.utils.CheckUtils;
import com.cqgk.shennong.shop.zxing.CamerBaseActivity;
import com.cqgk.shennong.shop.R;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
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


    @ViewInject(R.id.productTitle)
    EditText productTitle;

    @ViewInject(R.id.retailPrice)
    EditText retailPrice;

    @ViewInject(R.id.vipPrice)
    EditText vipPrice;


    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private List<EditBean> editBeanList;
    private ProductEditItemAdapter productEditItemAdapter;


    private boolean hasSurface;
    private String productId;//商品ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();

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

        if (CheckUtils.isAvailable(productId)) {
            getTitleDelegate().setTitle("商品更新");
        } else {
            getTitleDelegate().setTitle("商品上传");
        }


        productEditItemAdapter = new ProductEditItemAdapter(this, editBeanList);
        selview.setAdapter(productEditItemAdapter);
        selview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditBean editBean = productEditItemAdapter.getItem(i);
                if (editBean.getPhotoInfo() == null) {
                    startActionSheet();
                } else {
                    editBeanList.remove(i);
                    productEditItemAdapter.setValueList(editBeanList);
                    productEditItemAdapter.notifyDataSetChanged();

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
                        switch (index) {
                            case 0:
                                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
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

                final PhotoInfo photoInfo = resultList.get(0);
                File file = new File(photoInfo.getPhotoPath());
                if(file.length()/1024>1024){
                    showLongToast("上传的图片不能大于1M");
                    return;
                }

                String fileName = AppUtil.getFileName(photoInfo.getPhotoPath());

                RequestUtils.fileUpload(photoInfo.getPhotoPath(),
                        fileName, new HttpCallBack<FileUploadResultBean>() {
                    @Override
                    public void success(FileUploadResultBean result) {
                        EditBean temp = new EditBean();
                        temp.setPhotoInfo(photoInfo);
                        temp.setUploadId(result.getFile_id());
                        editBeanList.add(temp);
                        productEditItemAdapter.setValueList(editBeanList);
                        productEditItemAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean failure(int state, String msg) {
                        showLongToast(msg);
                        return super.failure(state, msg);
                    }
                });

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


    @Event(R.id.savenow)
    private void savenow_click(View view) {

        if (!CheckUtils.isAvailable(productcode.getText().toString())) {
            showLongToast("请扫描后输入条形码");
            return;
        }

        if (!CheckUtils.isAvailable(productTitle.getText().toString())) {
            showLongToast("请输入商品名称");
            return;
        }

        if (!CheckUtils.isAvailable(retailPrice.getText().toString())) {
            showLongToast("请输入零售价");
            return;
        }
        if (!CheckUtils.isAvailable(vipPrice.getText().toString())) {
            showLongToast("请输入会员价");
            return;
        }

        if (editBeanList == null || editBeanList.size() == 0) {
            showLongToast("请上传商品图片");
            return;
        }

        String ids = getAllUploadId();


        RequestUtils.productUpdate(productId,
                productcode.getText().toString(),
                productTitle.getText().toString(),
                retailPrice.getText().toString(),
                vipPrice.getText().toString(),
                editBeanList.get(0).getUploadId(),
                ids, new HttpCallBack<String>() {
                    @Override
                    public void success(String result) {
                        showLongToast(result);
                        finish();
                    }

                    @Override
                    public boolean failure(int state, String msg) {
                        showLongToast(msg);
                        return super.failure(state, msg);
                    }
                });
    }


    private String getAllUploadId() {
        String uploadIDS = "";
        for (int i = 0; i < editBeanList.size(); i++) {
            if(editBeanList.get(i).getPhotoInfo()==null)continue;

            String uploadId = editBeanList.get(i).getUploadId();
            if (CheckUtils.isAvailable(uploadId))
                uploadIDS += uploadId + ",";
        }

        return uploadIDS.length() > 0 ? uploadIDS.substring(0,uploadIDS.length()-1) : "";
    }


}
