package com.cqgk.clerk.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqgk.clerk.adapter.ProductRowAdapter;
import com.cqgk.clerk.adapter.SearchResultPopAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.MeProductListBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.bean.normal.ProductRowBean;
import com.cqgk.clerk.bean.normal.ProductStandInfoBean;
import com.cqgk.clerk.config.Constant;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.view.NormalGridView;
import com.cqgk.clerk.view.NormalListView;
import com.cqgk.clerk.R;
import com.cqgk.clerk.view.ProductListDialog;
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
public class SearchProductActivity extends BusinessBaseActivity {

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

    @ViewInject(R.id.product_code_num)
    EditText product_code_num;

    private ProductRowAdapter productRowAdapter;
    private int page = 1;
    private int myproductTotal;
    private int searchPage = 1;
    private int searchTotal;
    private SearchResultPopAdapter searchResultPopAdapter;
    private ProductListDialog productListDialog;


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

        product_code_num.requestFocus();
        product_code_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
                    getCodeForProductList(textView.getText().toString());
                }
                return false;
            }
        });

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


                if (productRowAdapter.getCount() + 1 > myproductTotal) {
                    listview.addFooter(my_product_area, "已经到底了");
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
                if (searchResultPopAdapter.getCount() + 1 > searchTotal) {
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

    }


    /**
     * 条形码查找商品
     *
     * @param product_bar_code
     */
    private void getCodeForProductList(final String product_bar_code) {
        if (productListDialog != null && productListDialog.isShowing())
            return;
        RequestUtils.queryClerkGoodsByBarcode(product_bar_code, new HttpCallBack<MeProductListBean>() {
            @Override
            public void success(final MeProductListBean result, String msg) {
                if (CheckUtils.isAvailable(msg))
                    showToast(msg);

                if (result == null)
                    return;

                if (result.getTotal() == 0 || result.getList().size() == 0) {
                    showToast(product_bar_code + " 没找到对应商品");
                    product_code_num.setText("");
                    return;
                }

                if (result.getList().size() == 1) {//单个商品直接跳转
                    ProductDtlBean productDtlBean = result.getList().get(0);
                    NavigationHelper.getInstance().startUploadProduct(productDtlBean.getGoodsId());
                } else {
//                    List<ProductDtlBean> tempArr = new ArrayList<ProductDtlBean>();
//                    ProductDtlBean productDtlBean = new ProductDtlBean();
//                    productDtlBean.setId("123123");
//                    productDtlBean.setGoodsTitle("test1");
//                    productDtlBean.setNum(2);
//                    productDtlBean.setUserPrice(100);
//                    productDtlBean.setPrice(100);
//                    tempArr.add(productDtlBean);
//                    productDtlBean.setId("123123");
//                    productDtlBean.setGoodsTitle("test2");
//                    productDtlBean.setNum(2);
//                    productDtlBean.setUserPrice(100);
//                    productDtlBean.setPrice(1001);
//                    tempArr.add(productDtlBean);
                    productListDialog = new ProductListDialog(SearchProductActivity.this, result.getList());
                    productListDialog.setItemOnClickListener(new ProductListDialog.ItemOnClickListener() {
                        @Override
                        public void itemOnclick(int position) {
                            ProductDtlBean productDtlBean = result.getList().get(position);
                            NavigationHelper.getInstance().startUploadProduct(productDtlBean.getGoodsId());
                        }
                    });
                    productListDialog.show();
                }

            }

            @Override
            public boolean failure(int state, String msg) {
                showToast(msg);
                product_code_num.setText("");
                return super.failure(state, msg);
            }
        });
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
