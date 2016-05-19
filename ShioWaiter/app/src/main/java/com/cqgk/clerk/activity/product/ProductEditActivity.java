package com.cqgk.clerk.activity.product;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.cqgk.clerk.adapter.ProductEditItemAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.logicbean.WechatResultBean;
import com.cqgk.clerk.bean.normal.EditBean;
import com.cqgk.clerk.bean.normal.FileUploadResultBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.bean.normal.ProductStandInfoBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.view.CommonDialogView;
import com.cqgk.clerk.zxing.CamerBaseActivity;
import com.cqgk.clerk.R;
import com.google.zxing.Result;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    @ViewInject(R.id.delete)
    Button delete;

    @ViewInject(R.id.row_2_title)
    TextView row_2_title;
    @ViewInject(R.id.row_3_title)
    TextView row_3_title;
    @ViewInject(R.id.row_4_title)
    TextView row_4_title;

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
        row_2_title.setText(Html.fromHtml("名称<font color=\"red\">*</font>"));
        row_3_title.setText(Html.fromHtml("零售价<font color=\"red\">*</font>"));
        row_4_title.setText(Html.fromHtml("会员价<font color=\"red\">*</font>"));


        try {
            productId = getStringExtra("productid");
        } catch (NullPointerException e) {

        }

        editBeanList = new ArrayList<>();
        editBeanList.add(new EditBean());


        initView();
        requestData();
    }

    @Override
    public void requestData() {
        super.requestData();
        if (CheckUtils.isAvailable(productId)) {
            RequestUtils.queryClerkGoodsById(productId, new HttpCallBack<ProductDtlBean>() {
                @Override
                public void success(ProductDtlBean result) {
                    productTitle.setText(result.getGoodsTitle());
                    vipPrice.setText(String.valueOf(result.getVipPrice()));
                    retailPrice.setText(String.valueOf(result.getRetailPrice()));
                    productcode.setText(result.getBarCode());

                    if (result.getPhotoList() != null) {
                        Iterator iter = result.getPhotoList().entrySet().iterator();
                        while (iter.hasNext()) {

                            Map.Entry entry = (Map.Entry) iter.next();
                            String key = (String) entry.getKey();
                            String val = (String) entry.getValue();

                            EditBean editBean = new EditBean();
                            editBean.setUploadId(key);
                            PhotoInfo photoInfo = new PhotoInfo();
                            photoInfo.setPhotoPath(val);
                            editBean.setPhotoInfo(photoInfo);
                            editBeanList.add(editBean);
                            productEditItemAdapter.setValueList(editBeanList);
                            productEditItemAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

    }

    @Override
    public void initView() {
        super.initView();

        if (CheckUtils.isAvailable(productId)) {
            getTitleDelegate().setTitle("商品更新");
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonDialogView.show("你确定要删除?", new CommonDialogView.DialogClickListener() {
                        @Override
                        public void doConfirm() {
                            RequestUtils.deleteClerkGoods(productId, new HttpCallBack<String>() {
                                @Override
                                public void success(String result) {
                                    showToast("删除成功.");
                                }

                                @Override
                                public boolean failure(int state, String msg) {
                                    showLongToast(msg);
                                    return super.failure(state, msg);
                                }
                            });
                        }
                    });

                }
            });
        } else {
            getTitleDelegate().setTitle("商品上传");
            getTitleDelegate().setRightText("商品");
            getTitleDelegate().setRightOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationHelper.getInstance().startSearchProduct();
                }
            });
        }


        productEditItemAdapter = new ProductEditItemAdapter(this, editBeanList);
        selview.setAdapter(productEditItemAdapter);
        selview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                EditBean editBean = productEditItemAdapter.getItem(i);
                if (editBean.getPhotoInfo() == null) {
                    if (productEditItemAdapter.getCount() >= 4) {
                        showLongToast("最多只能上传3张图片");
                        return;
                    }
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
//                File file = new File(photoInfo.getPhotoPath());
//                if (file.length() / 1024 > 1024) {
//                    showLongToast("上传的图片不能大于1M");
//                    return;
//                }

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
        queryProductDefInfo();
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

//        if (!CheckUtils.isAvailable(productcode.getText().toString())) {
//            showLongToast("请扫描后输入条形码");
//            return;
//        }

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

        if (editBeanList == null || editBeanList.size() == 1) {
            showLongToast("请上传商品图片");
            return;
        }

        String ids = getAllUploadId();


        RequestUtils.productUpdate(productId,
                productcode.getText().toString(),
                productTitle.getText().toString(),
                retailPrice.getText().toString(),
                vipPrice.getText().toString(),
                editBeanList.get(1).getUploadId(),
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
            if (editBeanList.get(i).getPhotoInfo() == null) continue;

            String uploadId = editBeanList.get(i).getUploadId();
            if (CheckUtils.isAvailable(uploadId))
                uploadIDS += uploadId + ",";
        }

        return uploadIDS.length() > 0 ? uploadIDS.substring(0, uploadIDS.length() - 1) : "";
    }


    private void queryProductDefInfo() {
        RequestUtils.queryGoodsStandardInfo(productcode.getText().toString(), new HttpCallBack<ProductStandInfoBean>() {
            @Override
            public void success(ProductStandInfoBean result) {

                if (result == null)
                    return;

                productTitle.setText(result.getTitle());
                retailPrice.setText(String.valueOf(result.getRetailPrice()));
                vipPrice.setText(String.valueOf(result.getVipPrice()));
            }

        });
    }

}
