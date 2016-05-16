package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by sven on 16/5/11.
 */
@ContentView(R.layout.activity_pickgood)
public class PickGoodActivity extends BusinessBaseActivity{
    @ViewInject(R.id.listView)
    ListView listView;

    private String keyWork;

    PickGoodAdapter adapter;

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

        layoutView();

        getHotGood();
        getValue();
    }


    private void layoutView(){
        adapter = new PickGoodAdapter(this);
        listView.setAdapter(adapter);
    }

    private void getValue(){
        RequestUtils.searchGood(keyWork, 1, new HttpCallBack<GoodListBean>() {
            @Override
            public void success(GoodListBean result) {

                result = new GoodListBean();
                ArrayList<GoodListBean.Item> items = new ArrayList<GoodListBean.Item>();
                GoodListBean.Item item = null;

                for (int i=0;i<10;i++){
                    item = new GoodListBean.Item();
                    item.setGoodsTitle("我是一个商品"+i);
                    item.setRetailPrice(32.76);
                    item.setSpecificationDesc("我是一个描述");
                }
                items.add(item);
                adapter.setTopGoodList(result);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getHotGood(){
        RequestUtils.queryTopGoodsList(new HttpCallBack<GoodListBean>() {
            @Override
            public void success(GoodListBean result) {

            }
        });
    }
}