package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.AdsBean;
import com.cqgk.clerk.bean.normal.HomeBean;
import com.cqgk.clerk.bean.normal.ShopInfoBean;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.view.CommonDialogView;
import com.cqgk.clerk.R;
import com.cqgk.clerk.view.PullRefreshBaseScrollView;
import com.cqgk.clerk.view.SlideShowView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 首页
 * Created by duke on 16/5/9.
 */
@ContentView(R.layout.main)
public class MainActivity extends BusinessBaseActivity {

    @ViewInject(R.id.money_a)
    TextView money_a;


    @ViewInject(R.id.money_b)
    TextView money_b;

    @ViewInject(R.id.money_c)
    TextView money_c;

    @ViewInject(R.id.pull_refresh_scrollview)
    PullRefreshBaseScrollView mPullRefreshScrollView;

    @ViewInject(R.id.slideshow)
    SlideShowView slideshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEnter.MainActivity = this;
        enableTitleDelegate();
        getTitleDelegate().setTitle(getResources().getString(R.string.app_name));
        getTitleDelegate().hideLeftBtn();
        getTitleDelegate().setRightText("退出");
        getTitleDelegate().setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonDialogView.show("你要退出店小二账号?", new CommonDialogView.DialogClickListener() {
                    @Override
                    public void doConfirm() {
                        RequestUtils.logout(new HttpCallBack<String>() {
                            @Override
                            public void success(String result,String msg) {
                                AppEnter.exitAccount();
                            }

                            @Override
                            public boolean failure(int state, String msg) {
                                showLongToast(msg);
                                AppEnter.exitAccount();
                                return super.failure(state, msg);
                            }
                        });

                    }
                });
            }
        });

        getTitleDelegate().setLeftText("v"+AppUtil.getVersion());
        getTitleDelegate().setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        mPullRefreshScrollView.setAutoReleaseRefresh(new Handler());
        mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                if(!AppUtil.isNetworkAvailable()){
                    mPullRefreshScrollView.onRefreshComplete();
                }
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {

            }
        });




        initView();
        requestData();
    }

    @Override
    public void initView() {
        super.initView();
        int width = AppUtil.getDisplayWidth();
        slideshow.setLayoutParams(new LinearLayout.LayoutParams(width, width * 165 / 320));

    }

    @Override
    public void requestData() {
        super.requestData();


        //店铺名称
        RequestUtils.queryServiceNickName(new HttpCallBack<ShopInfoBean>() {
            @Override
            public void success(ShopInfoBean result,String msg) {
                if(result==null)
                    return;
                getTitleDelegate().setTitle(result.getNickName());
            }
        });

        //收益
        RequestUtils.homedata(new HttpCallBack<HomeBean>() {
            @Override
            public void success(HomeBean result,String msg) {
                viewRefresh();
                if (result == null)
                    return;

                money_a.setText(String.valueOf(result.getTMA()));
                money_b.setText(String.valueOf(result.getTCA()));
                money_c.setText(String.valueOf(result.getYTA()));
            }

            @Override
            public boolean failure(int state, String msg) {
                viewRefresh();
                showLongToast(msg);
                return super.failure(state, msg);
            }
        });

        //首页轮播
        RequestUtils.homeads(new HttpCallBack<List<AdsBean>>() {
            @Override
            public void success(List<AdsBean> result,String msg) {
                slideshow.setImageUrls(result);
                slideshow.initUI(AppEnter.getInstance());
            }
        });
    }


    private void viewRefresh() {
        if (mPullRefreshScrollView != null)
            mPullRefreshScrollView.onRefreshComplete();
    }

    @Event(R.id.activecard)
    private void activecard_click(View view) {
        NavigationHelper.getInstance().startActiveCard();
    }

    @Event(R.id.uploadprodct)
    private void uploadprodct_click(View view) {
        NavigationHelper.getInstance().startUploadProduct("");
    }


    @Event(R.id.viprecharge)
    private void viprecharge_click(View view) {
        NavigationHelper.getInstance().startVipRecharge("");
    }

    @Event(R.id.ad_1)
    private void ad_1_click(View view) {
        NavigationHelper.getInstance().startWebView("http://baidu.com");
    }


    @Event(R.id.moneyrecord)
    private void moneyrecord_click(View view) {
        NavigationHelper.getInstance().startCashiering();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
