package com.cqgk.clerk.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cqgk.clerk.adapter.CashieringAdapter;
import com.cqgk.clerk.adapter.PickGoodAdapter;
import com.cqgk.clerk.adapter.SearchResultPopAdapter;
import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.MeProductListBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.utils.LogUtil;
import com.cqgk.clerk.view.CommonDialogView;
import com.cqgk.clerk.R;
import com.cqgk.clerk.view.NormalListView;
import com.cqgk.clerk.view.ProductListDialog;
import com.cqgk.clerk.view.SearchResultPopView;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sven on 16/5/11.
 */
@ContentView(R.layout.activity_pickgood)
public class PickGoodActivity extends BusinessBaseActivity implements PickGoodAdapter.PickGoodDelegate {
    @ViewInject(R.id.listView)
    ListView listView;


    @ViewInject(R.id.amountTV)
    TextView amountTV;
    @ViewInject(R.id.et_search)
    EditText et_search;

    @ViewInject(R.id.searchlistview)
    NormalListView searchlistview;

    @ViewInject(R.id.product_code_num)
    EditText product_code_num;

    //SearchResultPopView popView;
    PickGoodAdapter adapter;


    private ArrayList<ProductDtlBean> myGood;
    private int search_page = 1;
    private int searchTotal;
    private SearchResultPopAdapter searchResultPopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("挑选商品");
        getTitleDelegate().setRightText("确定");
        getTitleDelegate().setRightDrawable(R.drawable.icon_qr_scan);
        getTitleDelegate().setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationHelper.getInstance().startBarCodeFind(1);
            }
        });

        myGood = new ArrayList<>();

        layoutView();
        getHotGood();

    }


    private void layoutView() {
        product_code_num.requestFocus();
        product_code_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_DONE) {
                    String product_bar_code = textView.getText().toString();
                    getCodeForProductList(product_bar_code);
                }
                return false;
            }
        });


        adapter = new PickGoodAdapter(this, this);
        listView.setAdapter(adapter);

        searchResultPopAdapter = new SearchResultPopAdapter(this);
        searchResultPopAdapter.setItemListener(new SearchResultPopAdapter.ItemListener() {
            @Override
            public void itemClick(int i) {
                topGoodClick(searchResultPopAdapter.getItem(i));
                searchlistview.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });
        searchlistview.setAdapter(searchResultPopAdapter);
        searchlistview.setScrollStateEvent(new NormalListView.ScrollStateEvent() {
            @Override
            public void isBottom() {

                if (searchResultPopAdapter.getCount() + 1 > searchTotal) {
                    searchlistview.addFooterView("已经到底了");
                    return;
                }

                search_page++;
                search(et_search.getText().toString());
            }

            @Override
            public void isOver() {

            }

            @Override
            public void isTop() {

            }
        });
    }


    //去支付事件
    @Event(R.id.goPayBtn)
    private void goPay(View view) {
        double goodNum = 0;
        for (ProductDtlBean good : myGood) {
            goodNum += good.getNum();
        }
        if (0 == goodNum) {
            showToast("未选择商品");
            return;
        }

        NavigationHelper.getInstance().startPayBill(myGood);
    }

    //清楚搜索
    @Event(R.id.cleanIB)
    private void cleanSearch(View view) {
        et_search.setText("");
        searchlistview.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    //搜索事件
    @Event(R.id.btn_search)
    private void search(View view) {
        if (!CheckUtils.isAvailable(et_search.getText().toString())) {
            showToast("请输入搜索内容");
            return;
        }
        search_page = 1;
        searchResultPopAdapter.setValuelist(new ArrayList<ProductDtlBean>());
        search(et_search.getText().toString());
    }

    private void search(String keyWork) {
        RequestUtils.searchGood(keyWork, search_page, new HttpCallBack<GoodListBean>() {
            @Override
            public void success(final GoodListBean result, String msg) {

                if (null == result.getList() || result.getList().size() == 0) {
                    showToast("搜索不到该商品");
                    return;
                }

                searchTotal = result.getTotal();


                searchlistview.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
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


    private void getHotGood() {
        RequestUtils.queryTopGoodsList(new HttpCallBack<List<ProductDtlBean>>() {
            @Override
            public void success(List<ProductDtlBean> result, String msg) {
                List<ProductDtlBean> items = result;
                if (null != items) {
                    adapter.getTopGoodList().addAll(items);
                    adapter.notifyDataSetChanged();
                }


            }
        });
    }


    private void refreshPrice() {
        int num = 0;
        double price = 0;
//        android:text="￥0     共0件"
        for (ProductDtlBean item : myGood) {
            num += item.getNum();
            price += (item.getNum() * item.getRetailPrice());
        }

        amountTV.setText(Html.fromHtml(String.format("￥<font color=\"red\">%.2f</font>     共<font color=\"red\">%s</font>件", price, num)));
    }

    @Override
    public void topGoodClick(ProductDtlBean item) {
        boolean alreadyHad = false;

        if (item.getNum() <= 0)
            item.setNum(1);

        for (ProductDtlBean item1 : myGood) {
            if (item1.getId().equals(item.getId())) {
                alreadyHad = true;
                item1.setNum(item1.getNum() + item.getNum());
            }
        }

        if (alreadyHad == false) {


            myGood.add(ProductDtlBean.copy(item));
        }


        adapter.setMyGood(myGood);
        adapter.notifyDataSetChanged();

        refreshPrice();
    }

    @Override
    public void goodPlus(ProductDtlBean item) {
        for (ProductDtlBean item1 : myGood) {
            if (item1.equals(item)) {
                item1.setNum(item1.getNum() + 1);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();
                refreshPrice();
                return;
            }
        }

    }


    private void getCodeForProductList(final String product_bar_code) {
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

                if (result.getTotal() > 1) {
                    searchTotal = result.getTotal();
                    searchlistview.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    searchResultPopAdapter.setValuelist(result.getList());
                    searchResultPopAdapter.notifyDataSetChanged();
                } else {
                    topGoodClick(result.getList().get(0));
                    searchlistview.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
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

    @Override
    public void goodMinus(ProductDtlBean item) {
        for (ProductDtlBean item1 : myGood) {
            if (item1.equals(item)) {
                item1.setNum(item1.getNum() - 1);
                if (item1.getNum() <= 0) myGood.remove(item1);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();
                refreshPrice();
                return;
            }
        }
    }

    @Override
    public void changQty(ProductDtlBean item) {
        for (ProductDtlBean item1 : myGood) {
            if (item1.equals(item)) {
                item1.setNum(item.getNum());
                if (0 == item1.getNum()) myGood.remove(item1);

                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();
                refreshPrice();
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            List<ProductDtlBean> beanlist = (List<ProductDtlBean>) data.getSerializableExtra("dtllist");
            for (int i = 0; i < beanlist.size(); i++) {
                LogUtil.e(String.format("_________id:%s num:%s", beanlist.get(i).getGoodsId(), beanlist.get(i).getNum()));
                topGoodClick(beanlist.get(i));
            }

        } else if (resultCode == 999) {
            //清理筛选物品
            myGood.clear();
            adapter.setMyGood(myGood);
            adapter.notifyDataSetChanged();
            refreshPrice();
        }
    }
}
