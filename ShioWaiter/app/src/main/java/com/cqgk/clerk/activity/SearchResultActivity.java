package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.cqgk.clerk.adapter.PickGoodAdapter;
import com.cqgk.clerk.adapter.SearchResultAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.R;

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
        listView.setAdapter( adapter);
    }
}
