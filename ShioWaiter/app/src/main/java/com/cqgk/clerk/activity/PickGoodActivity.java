package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.CashieringAdapter;
import com.cqgk.clerk.adapter.PickGoodAdapter;
import com.cqgk.clerk.base.BusinessBaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by sven on 16/5/11.
 */
@ContentView(R.layout.activity_pickgood)
public class PickGoodActivity extends BusinessBaseActivity{
    @ViewInject(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("挑选商品");
        layoutView();
    }


    private void layoutView(){
        PickGoodAdapter adapter = new PickGoodAdapter(this);
        listView.setAdapter( adapter);
    }
}
