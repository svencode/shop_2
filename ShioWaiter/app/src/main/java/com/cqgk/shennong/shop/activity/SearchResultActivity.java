package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.cqgk.shennong.shop.adapter.PickGoodAdapter;
import com.cqgk.shennong.shop.adapter.SearchResultAdapter;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.R;
import com.cqgk.shennong.shop.bean.normal.GoodListBean;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by sven on 16/5/11.
 */
@ContentView(R.layout.activity_pickgood)
public class SearchResultActivity extends BusinessBaseActivity{
    @ViewInject(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("我的商品");
        layoutView();
    }


    private void layoutView(){
        SearchResultAdapter adapter = new SearchResultAdapter(this);
        listView.setAdapter(adapter);
    }


}
