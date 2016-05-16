package com.cqgk.shennong.shop.activity.product;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cqgk.shennong.shop.adapter.ProductRowAdapter;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.bean.normal.ProductDtlBean;
import com.cqgk.shennong.shop.bean.normal.ProductRowBean;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestUtils;
import com.cqgk.shennong.shop.utils.CheckUtils;
import com.cqgk.shennong.shop.view.NormalListView;
import com.cqgk.shennong.shop.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
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

    @ViewInject(R.id.keyword)
    EditText keyword;

    private ProductRowAdapter productRowAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("寻找商品");
        getTitleDelegate().setRightDrawable(R.drawable.icon_scan);

        initView();
        requestData();
    }

    @Override
    public void requestData() {
        super.requestData();

        RequestUtils.allProdct("1", "10", new HttpCallBack<List<ProductDtlBean>>() {
            @Override
            public void success(List<ProductDtlBean> result) {
                if(result==null || result.size()==0){
                    showLongToast("对不起,没找到相应的商品");
                    return;
                }
                productRowAdapter.setValuelist(result);
                productRowAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean failure(int state, String msg) {
                showLongToast(msg);
                return super.failure(state, msg);
            }
        });
    }

    private void searchByKeyWord(String keyword) {
        RequestUtils.queryClerkGoodsByKey(keyword, "1", "10", new HttpCallBack<List<ProductDtlBean>>() {
            @Override
            public void success(List<ProductDtlBean> result) {
                if(result==null || result.size()==0){
                    showLongToast("对不起,没找到相应的商品");
                    return;
                }
                productRowAdapter.setValuelist(result);
                productRowAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean failure(int state, String msg) {
                showLongToast(msg);
                return super.failure(state, msg);
            }
        });
    }

    @Override
    public void initView() {
        super.initView();

        productRowAdapter = new ProductRowAdapter(this);
        listview.setAdapter(productRowAdapter);
    }


    @Event(R.id.searchbtn)
    private void searchbtn_click(View view) {
        if (!CheckUtils.isAvailable(keyword.getText().toString())) {
            requestData();
            return;
        }
        searchByKeyWord(keyword.getText().toString());

    }
}
