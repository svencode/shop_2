package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.helper.NavigationHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by duke on 16/5/9.
 */
@ContentView(R.layout.main)
public class MainActivity extends BusinessBaseActivity {

    @ViewInject(R.id.uploadprodct)
    ImageView uploadprodct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("店小二");
        getTitleDelegate().hideLeftBtn();
    }

    @Event(R.id.activecard)
    private void activecard_click(View view){
        NavigationHelper.getInstance().startActiveCard();
    }

    @Event(R.id.uploadprodct)
    private void uploadprodct_click(View view) {
        NavigationHelper.getInstance().startUploadProduct();
    }


    @Event(R.id.viprecharge)
    private void viprecharge_click(View view) {
        NavigationHelper.getInstance().startVipRecharge();
    }
}
