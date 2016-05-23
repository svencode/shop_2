package com.cqgk.clerk.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
 * 通用listview
 * Created by duke on 15/11/11.
 */
public class NormalGridView extends GridView {

    private ScrollStateEvent scrollStateEvent;

    private boolean isHaveFooterView=false;


    public boolean isHaveFooterView() {
        return isHaveFooterView;
    }

    public void setHaveFooterView(boolean haveFooterView) {
        isHaveFooterView = haveFooterView;
    }

    public ScrollStateEvent getScrollStateEvent() {
        return scrollStateEvent;
    }

    public void setScrollStateEvent(ScrollStateEvent scrollStateEvent) {
        this.scrollStateEvent = scrollStateEvent;
    }


    public interface ScrollStateEvent {
        public void isBottom();

        public void isOver();

        public void isTop();
    }

    public NormalGridView(Context context) {
        super(context);
        initView();
    }

    public NormalGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NormalGridView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    void initView() {
        this.setCacheColorHint(Color.TRANSPARENT);
        this.setScrollingCacheEnabled(false);
        this.setHorizontalFadingEdgeEnabled(false);
        this.setOnScrollListener(new AbsListViewScollEvent());
    }

    /**
     * 计算是否最底
     *
     * @param listView
     * @return
     */
    public boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result;
    }

    public void removeFooterView(){

    }

    /**
     * 添加底部
     * @param parent
     */
    public void addFooter(View parent,String content) {
        if(isHaveFooterView)
            return;

       View foot = FootEndView.getFootView(getContext(),content);
        LinearLayout parentView = (LinearLayout)parent;
        parentView.addView(foot);
        isHaveFooterView=true;
    }


    class AbsListViewScollEvent implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                // 当不滚动时
                case OnScrollListener.SCROLL_STATE_IDLE:
                    if (isListViewReachBottomEdge(view)) {
                        if (scrollStateEvent != null)
                            scrollStateEvent.isBottom();
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (1 > view.getChildCount()) return;

            //LogUtils.e(String.format("%s",view.getChildAt(0).getTop()));

            if (0 == view.getChildAt(0).getTop()) {
                if (scrollStateEvent != null)
                    scrollStateEvent.isTop();
            } else {
                if (scrollStateEvent != null)
                    scrollStateEvent.isOver();
            }
        }
    }
}
