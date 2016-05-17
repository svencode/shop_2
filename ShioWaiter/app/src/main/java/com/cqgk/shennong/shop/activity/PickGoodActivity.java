package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cqgk.shennong.shop.adapter.CashieringAdapter;
import com.cqgk.shennong.shop.adapter.PickGoodAdapter;
import com.cqgk.shennong.shop.base.AppEnter;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.bean.normal.GoodListBean;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestUtils;
import com.cqgk.shennong.shop.view.CommonDialogView;
import com.cqgk.shennong.shop.R;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

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


    private String keyWork;

    PickGoodAdapter adapter;

    private ArrayList<GoodListBean.Item> myGood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("挑选商品");
        getTitleDelegate().setRightText("确定");
        getTitleDelegate().setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationHelper.getInstance().startPayBill();
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

    private void getValue(){
        RequestUtils.searchGood(keyWork, 1, new HttpCallBack<GoodListBean>() {
            @Override
            public void success(GoodListBean result) {

                result = new GoodListBean();

            }
        });
    }

    //去支付事件
    @Event(R.id.goPayBtn)
    private void goPay(View view) {
        NavigationHelper.getInstance().startPayBill();
    }

    //清楚搜索
    @Event(R.id.cleanIB)
    private void cleanSearch(View view) {
        et_search.setText("");
    }

    //搜索事件
    @Event(R.id.searchbtn)
    private void search(View view){

    }

    private void getHotGood(){
        RequestUtils.queryTopGoodsList(new HttpCallBack<GoodListBean>() {
            @Override
            public void success(GoodListBean result) {
                ArrayList<GoodListBean.Item> items = new ArrayList<GoodListBean.Item>();
                GoodListBean.Item item = null;

                for (int i=0;i<9;i++){
                    item = new GoodListBean.Item();
                    item.setGoodsTitle("我是一个商品"+i);
                    item.setRetailPrice(32.76);
                    item.setSpecificationDesc("我是一个描述");
                    items.add(item);
                }

                adapter.getTopGoodList().addAll(items);
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void refreshPrice(){
        int num = 0;
        double price = 0;
//        android:text="￥0     共0件"
        for (GoodListBean.Item item:myGood){
            num += item.getNum();
            price += item.getRetailPrice();
        }

        amountTV.setText("￥" +price + "     共"+num+"件");
    }

    @Override
    public void topGoodClick(GoodListBean.Item item) {
        boolean alreadyHad = false;
        for (GoodListBean.Item item1:myGood){
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


}
