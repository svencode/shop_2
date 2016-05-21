package com.cqgk.clerk.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.cqgk.clerk.utils.LogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * Created by duke on 16/1/11.
 */
public class PullRefreshBaseScrollView extends PullToRefreshScrollView {


    private ScrollBottomListeren scrollBottomListeren;
    private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;
    private Handler handler;

    public ScrollBottomListeren getScrollBottomListeren() {
        return scrollBottomListeren;
    }

    public void setScrollBottomListeren(ScrollBottomListeren scrollBottomListeren) {
        this.scrollBottomListeren = scrollBottomListeren;
    }

    public interface ScrollBottomListeren {
        public void isBottom();
    }

    public PullRefreshBaseScrollView(Context context) {
        super(context);
        initView();
        handler = getHandler();
    }

    public PullRefreshBaseScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        handler = getHandler();
    }


    private void initView() {
        //setOnScrollChangeListener(new AbsListViewScollEvent());
        handler = getHandler();
    }

    public boolean isListViewBottom(AbsListView view) {
        try {

            //滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                View v = (View) view.getChildAt(view.getChildCount() - 1);
                int[] location = new int[2];
                if (location == null)
                    return false;

                v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                int y = location[1];
                LogUtil.e(String.format("x:%s,y:%s", location[0], location[1]));
                if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y)//第一次拖至底部
                {
                    getLastVisiblePosition = view.getLastVisiblePosition();
                    lastVisiblePositionY = y;

                    if (scrollBottomListeren != null)
                        scrollBottomListeren.isBottom();

                    return true;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();

        }
        //未滚动到底部，第二次拖至底部都初始化
        getLastVisiblePosition = 0;
        lastVisiblePositionY = 0;
        return false;
    }

    @Override
    protected void onRefreshing(boolean doScroll) {
        super.onRefreshing(doScroll);
    }

    @Override
    protected void onReleaseToRefresh() {
        super.onReleaseToRefresh();
        if (handler != null)
            handler.postDelayed(runnable, 10000);
    }

    public void setAutoReleaseRefresh(Handler handler) {
        this.handler = handler;
    }


    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                if (isRefreshing()) {
                    refreshForceStop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private void refreshForceStop() {
        this.onRefreshComplete();
    }

    private class AbsListViewScollEvent implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                isListViewBottom(view);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    ;

}
