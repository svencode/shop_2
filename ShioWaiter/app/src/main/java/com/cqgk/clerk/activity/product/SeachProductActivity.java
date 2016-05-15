package com.cqgk.clerk.activity.product;

import android.os.Bundle;

import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.ProductRowAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.ProductRowBean;
import com.cqgk.clerk.view.NormalListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找商品
 * Created by duke on 16/5/14.
 */
@ContentView(R.layout.searchproduct)
public class SeachProductActivity extends BusinessBaseActivity {

    @ViewInject(R.id.listview)
    NormalListView listview;

    private ProductRowAdapter productRowAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("寻找商品");
        getTitleDelegate().setRightDrawable(R.drawable.icon_scan);

        initView();
    }

    @Override
    public void initView() {
        super.initView();
        List<ProductRowBean> list = new ArrayList<>();
        list.add(new ProductRowBean());
        list.add(new ProductRowBean());
        list.add(new ProductRowBean());

        productRowAdapter = new ProductRowAdapter(this,list);
        listview.setAdapter(productRowAdapter);
    }
}
