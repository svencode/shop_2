package com.cqgk.clerk.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cqgk.clerk.adapter.ProductRowAdapter;
import com.cqgk.clerk.adapter.SearchResultPopAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.MeProductListBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.bean.normal.ProductRowBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.view.NormalGridView;
import com.cqgk.clerk.view.NormalListView;
import com.cqgk.clerk.R;
import com.cqgk.clerk.view.SearchResultPopView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查找商品
 * Created by duke on 16/5/14.
 */
@ContentView(R.layout.searchproduct)
public class SeachProductActivity extends BusinessBaseActivity {

    @ViewInject(R.id.listview)
    NormalGridView listview;

    @ViewInject(R.id.searlistview)
    NormalListView searlistview;

    @ViewInject(R.id.et_search)
    EditText keyword;


    @ViewInject(R.id.search_product_area)
    LinearLayout search_product_area;

    @ViewInject(R.id.my_product_area)
    LinearLayout my_product_area;

    private ProductRowAdapter productRowAdapter;
    private int page = 1;
    private int myproductTotal;
    private int searchPage = 1;
    private int searchTotal;
    private SearchResultPopAdapter searchResultPopAdapter;


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
        loadProductList();
    }

    /**
     * 我的商品列表
     */
    private void loadProductList() {
        RequestUtils.allProdct(String.valueOf(page), "20", new HttpCallBack<MeProductListBean>() {
            @Override
            public void success(MeProductListBean result, String msg) {
                if (result == null) {
                    if (CheckUtils.isAvailable(msg))
                        showToast(msg);
                    return;
                }

                myproductTotal = result.getTotal();

//                if (result.getTotal() == 0) {
//                    listview.addFooterView("已经到底了");
//                    return;
//                }

                my_product_area.setVisibility(View.VISIBLE);
                search_product_area.setVisibility(View.GONE);
                productRowAdapter.addValuelist(result.getList());
                productRowAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean failure(int state, String msg) {
                showLongToast(msg);
                return super.failure(state, msg);
            }
        });
    }


    /**
     * 搜索
     *
     * @param keyword
     */
    private void searchByKeyWord(String keyword) {
        RequestUtils.searchShopGood(keyword, searchPage, new HttpCallBack<GoodListBean>() {
            @Override
            public void success(GoodListBean result, String msg) {
                if (result == null) {
                    showLongToast("对不起,没找到相应的商品");
                    return;
                }

                searchTotal = result.getTotal();

                search_product_area.setVisibility(View.VISIBLE);
                my_product_area.setVisibility(View.GONE);

                searchResultPopAdapter.addValuelist(result.getList());
                searchResultPopAdapter.notifyDataSetChanged();

            }

            @Override
            public boolean failure(int state, String msg) {
                showToast(msg);
                return super.failure(state, msg);
            }
        });


    }

    @Override
    public void initView() {
        super.initView();

        productRowAdapter = new ProductRowAdapter(this);
        listview.setAdapter(productRowAdapter);

        searchResultPopAdapter = new SearchResultPopAdapter(this);
        searlistview.setAdapter(searchResultPopAdapter);



        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    my_product_area.setVisibility(View.VISIBLE);
                    search_product_area.setVisibility(View.GONE);
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProductDtlBean productDtlBean = productRowAdapter.getItem(i);
                NavigationHelper.getInstance().startUploadProduct(productDtlBean.getGoodsId());
            }
        });

        listview.setScrollStateEvent(new NormalGridView.ScrollStateEvent() {
            @Override
            public void isBottom() {


                if(productRowAdapter.getCount()+1>myproductTotal){
                    listview.addFooter(my_product_area,"已经到底了");
                    return;
                }

                page++;
                loadProductList();
            }

            @Override
            public void isOver() {

            }

            @Override
            public void isTop() {

            }

            @Override
            public void isFling() {
                listview.removeFooterView(my_product_area);
            }
        });

        searchResultPopAdapter.setItemListener(new SearchResultPopAdapter.ItemListener() {
            @Override
            public void itemClick(int i) {
                ProductDtlBean productDtlBean = searchResultPopAdapter.getItem(i);
                NavigationHelper.getInstance().startUploadProduct(productDtlBean.getGoodsId());
            }
        });
        searlistview.setScrollStateEvent(new NormalListView.ScrollStateEvent() {
            @Override
            public void isBottom() {
                if(searchResultPopAdapter.getCount()+1>searchTotal){
                    searlistview.addFooterView("已经到底了");
                    return;
                }

                searchPage++;
                searchByKeyWord(keyword.getText().toString());
            }

            @Override
            public void isOver() {

            }

            @Override
            public void isTop() {

            }
        });


//        searchResultPopView = new SearchResultPopView(this, 0);
//        searchResultPopView.getAdapter().setItemListener(new SearchResultPopAdapter.ItemListener() {
//            @Override
//            public void itemClick(int i) {
//                ProductDtlBean productDtlBean = searchResultPopView.getAdapter().getItem(i);
//                NavigationHelper.getInstance().startUploadProduct(productDtlBean.getGoodsId());
//            }
//        });
//        searchResultPopView.getListView().setScrollStateEvent(new NormalListView.ScrollStateEvent() {
//            @Override
//            public void isBottom() {
//                searchPage++;
//                searchByKeyWord(keyword.getText().toString());
//            }
//
//            @Override
//            public void isOver() {
//
//            }
//
//            @Override
//            public void isTop() {
//            }
//        });
    }

    @Event(R.id.cleanIB)
    private void cleanIB_click(View view) {
        keyword.setText("");
        my_product_area.setVisibility(View.VISIBLE);
        search_product_area.setVisibility(View.GONE);
    }


    @Event(R.id.btn_search)
    private void searchbtn_click(View view) {
        if (!CheckUtils.isAvailable(keyword.getText().toString())) {
            showToast("请输入搜索内容");
            return;
        }

        searchPage = 1;
        searchResultPopAdapter.setValuelist(new ArrayList<ProductDtlBean>());
        searchByKeyWord(keyword.getText().toString());

    }
}
