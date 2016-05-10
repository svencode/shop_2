package com.cqgk.clerk.activity.product;

import android.graphics.PointF;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.GridView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.ProductEditItemAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.EditBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品编辑或上传
 * Created by duke on 16/5/9.
 */
@ContentView(R.layout.productedit)
public class ProductEdit extends BusinessBaseActivity {

    @ViewInject(R.id.selview)
    GridView selview;


    private List<EditBean> editBeanList;
    private ProductEditItemAdapter productEditItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("商品上传");

        editBeanList = new ArrayList<>();
        editBeanList.add(new EditBean());
        productEditItemAdapter = new ProductEditItemAdapter(this,editBeanList);
        selview.setAdapter(productEditItemAdapter);



    }


}
