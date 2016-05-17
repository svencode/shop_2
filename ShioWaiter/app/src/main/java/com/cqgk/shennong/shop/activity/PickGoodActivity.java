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


        myGood = new ArrayList<>();

        layoutView();

        getHotGood();
        getValue();
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
        NavigationHelper.getInstance().startPayBill(myGood);
    }

    //清楚搜索
    @Event(R.id.cleanIB)
    private void cleanSearch(View view) {
        et_search.setText("");
    }

    //搜索事件
    @Event(R.id.searchbtn)
    private void search(View view){
        et_search.setText("");
    }

    private void getHotGood(){
        RequestUtils.queryTopGoodsList(new HttpCallBack<GoodListBean>() {
            @Override
            public void success(GoodListBean result) {
                ArrayList<GoodListBean.Item> items = new ArrayList<GoodListBean.Item>();
                GoodListBean.Item item = null;

                for (int i = 0; i < 1; i++) {
                    item = new GoodListBean.Item();
                    item.setLogoImg("http://fs.51xnb.cn/f26228a7-f9e2-4008-9ad0-314b85b650b3.jpg");
                    item.setId("0006de64-5f12-4cbc-9477-06c2a8f5ad2d");
                    item.setGoodsId("082344bd-3c3c-41ee-855a-08588ab5466f");
                    item.setGoodsTitle("康佳现代 高清蓝光LED电视 LED43H90C");
                    item.setPrice(1490.00);
                    item.setRetailPrice(1699.00);
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
            price += (item.getNum()*item.getPrice());
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
    @Override
    public void goodPlus(GoodListBean.Item item){
        for (GoodListBean.Item item1:myGood){
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
    public void goodMinus(GoodListBean.Item item){
        for (GoodListBean.Item item1:myGood){
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

}
