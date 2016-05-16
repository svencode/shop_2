package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.CashieringAdapter;
import com.cqgk.clerk.adapter.PickGoodAdapter;
import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.view.CommonDialogView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by sven on 16/5/11.
 */
@ContentView(R.layout.activity_pickgood)
public class PickGoodActivity extends BusinessBaseActivity{
    @ViewInject(R.id.listView)
    ListView listView;

    private String keyWork;

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

        getValue();
    }


    private void layoutView(){
        PickGoodAdapter adapter = new PickGoodAdapter(this);
        listView.setAdapter( adapter);
    }

    private void getValue(){
        RequestUtils.searchGood(keyWork, 1, new HttpCallBack<GoodListBean>() {
            @Override
            public void success(GoodListBean result) {

            }
        });
    }
}
