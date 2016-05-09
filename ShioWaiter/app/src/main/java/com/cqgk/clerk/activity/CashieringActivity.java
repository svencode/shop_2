package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by sven on 16/5/9.
 */

@ContentView(R.layout.activity_cashiering)
public class CashieringActivity extends BusinessBaseActivity {

    @ViewInject(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("收银记账");
    }


}