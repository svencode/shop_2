package com.cqgk.shennong.shop.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 通用listview
 * Created by duke on 15/11/11.
 */
public class NormalListView extends ListView {

    private ScrollStateEvent scrollStateEvent;

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

    public NormalListView(Context context) {
        super(context);
        initView();
    }

    public NormalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NormalListView(Context context, AttributeSet attrs,
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


    /**
     * 添加底部
     * @param content
     */
    public void addFooterView(String content) {
        if (getFooterViewsCount() == 0) {
           View view = FootEndView.getFootView(getContext(),content);
            addFooterView(view);
        }
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
