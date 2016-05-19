package com.cqgk.clerk.activity.product;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cqgk.clerk.adapter.ProductRowAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.MeProductListBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.bean.normal.ProductRowBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.view.NormalListView;
import com.cqgk.clerk.R;
import com.cqgk.clerk.view.SearchResultPopView;

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

    @ViewInject(R.id.search_row)
    LinearLayout search_row;


    private ProductRowAdapter productRowAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("寻找商品");
        getTitleDelegate().setRightDrawable(R.drawable.icon_scan);
        getTitleDelegate().setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationHelper.getInstance().startBarCodeFind(0);
            }
        });

        initView();
        requestData();





    }

    @Override
    public void requestData() {
        super.requestData();

        RequestUtils.allProdct("1", "10", new HttpCallBack<MeProductListBean>() {
            @Override
            public void success(MeProductListBean result) {
                if(result==null || result.getTotal()==0){
                    showLongToast("对不起,没找到相应的商品");
                    return;
                }
                productRowAdapter.setValuelist(result.getList());
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
        RequestUtils.queryClerkGoodsByKey(keyword, "1", "10", new HttpCallBack<MeProductListBean>() {
            @Override
            public void success(MeProductListBean result) {
                if(result==null || result.getTotal()==0){
                    showLongToast("对不起,没找到相应的商品");
                    return;
                }
                productRowAdapter.setValuelist(result.getList());
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProductDtlBean productDtlBean = productRowAdapter.getItem(i);
                NavigationHelper.getInstance().startUploadProduct(productDtlBean.getGoodsId());
            }
        });

        productRowAdapter = new ProductRowAdapter(this);
        listview.setAdapter(productRowAdapter);
    }


    @Event(R.id.searchbtn)
    private void searchbtn_click(View view) {
//        SearchResultPopView  searchResultPopView = new SearchResultPopView(this);
//        searchResultPopView.showAsDropDown(search_row);
        if (!CheckUtils.isAvailable(keyword.getText().toString())) {
            requestData();
            return;
        }
        searchByKeyWord(keyword.getText().toString());

    }
}
