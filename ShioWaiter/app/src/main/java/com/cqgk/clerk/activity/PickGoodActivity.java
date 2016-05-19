package com.cqgk.clerk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cqgk.clerk.adapter.CashieringAdapter;
import com.cqgk.clerk.adapter.PickGoodAdapter;
import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.view.CommonDialogView;
import com.cqgk.clerk.R;
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
public class PickGoodActivity extends BusinessBaseActivity implements PickGoodAdapter.PickGoodDelegate{
    @ViewInject(R.id.listView)
    ListView listView;


    @ViewInject(R.id.amountTV)
    TextView amountTV;
    @ViewInject(R.id.et_search)
    EditText et_search;

    SearchResultPopView popView;

    PickGoodAdapter adapter;

    private ArrayList<ProductDtlBean> myGood;

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



    private void layoutView(){
        adapter = new PickGoodAdapter(this,this);
        listView.setAdapter(adapter);
    }



    //去支付事件
    @Event(R.id.goPayBtn)
    private void goPay(View view) {
        if (0 == myGood.size()){
            showToast("未选择商品");
            return;
        }
        NavigationHelper.getInstance().startPayBill(myGood);
    }

    //清楚搜索
    @Event(R.id.cleanIB)
    private void cleanSearch(View view) {
        et_search.setText("");
    }

    //搜索事件
    @Event(R.id.btn_search)
    private void search(View view){
        search(et_search.getText().toString());

    }

    private void search(String keyWork){
//        if (keyWork.length()==0)return;
        RequestUtils.searchGood(keyWork, 1, new HttpCallBack<GoodListBean>() {
            @Override
            public void success(final GoodListBean result) {

                if (null == result.getList() || result.getList().size() == 0){
                    showToast("搜索不到该商品");
                    return;
                }

                if (null == popView){
                    popView = new SearchResultPopView(PickGoodActivity.this);
                }

                if (popView.isShowing()){
                    popView.dismiss();
                }



                popView.getAdapter().setValuelist(result.getList());
                popView.getAdapter().notifyDataSetChanged();
                popView.showAsDropDown(et_search);
                popView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        topGoodClick(result.getList().get(position));
                        popView.dismiss();
                    }
                });




            }
        });
    }



    private void getHotGood(){
        RequestUtils.queryTopGoodsList(new HttpCallBack<List<ProductDtlBean>>() {
            @Override
            public void success(List<ProductDtlBean> result) {
                List<ProductDtlBean> items = result;
                if (null != items){
                    adapter.getTopGoodList().addAll(items);
                    adapter.notifyDataSetChanged();
                }


            }
        });
    }


    private void refreshPrice(){
        int num = 0;
        double price = 0;
//        android:text="￥0     共0件"
        for (ProductDtlBean item:myGood){
            num += item.getNum();
            price += (item.getNum()*item.getRetailPrice());
        }

        amountTV.setText("￥" +price + "     共"+num+"件");
    }

    @Override
    public void topGoodClick(ProductDtlBean item) {
        boolean alreadyHad = false;
        for (ProductDtlBean item1:myGood){
            if (item1.equals(item)){
                alreadyHad = true;
                item1.setNum(item1.getNum()+1);
            }
        }

        if (alreadyHad == false){
            item.setNum(1);
            myGood.add(item);
        }

        adapter.setMyGood(myGood);
        adapter.notifyDataSetChanged();

        refreshPrice();
    }
    @Override
    public void goodPlus(ProductDtlBean item){
        for (ProductDtlBean item1:myGood){
            if (item1.equals(item)){
                item1.setNum(item1.getNum()+1);
                adapter.setMyGood(myGood);
                adapter.notifyDataSetChanged();

                refreshPrice();
                return;
            }
        }

    }
    @Override
    public void goodMinus(ProductDtlBean item){
        for (ProductDtlBean item1:myGood){
            if (item1.equals(item)) {
                item1.setNum(item1.getNum()-1);
                if (item1.getNum()==0)myGood.remove(item1);
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
        if (resultCode==1){
            ProductDtlBean bean = (ProductDtlBean)data.getSerializableExtra("dtl");
            topGoodClick(bean);
        }
    }
}