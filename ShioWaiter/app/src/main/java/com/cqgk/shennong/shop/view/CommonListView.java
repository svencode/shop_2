package com.cqgk.shennong.shop.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by duke on 15/10/31.
 */
public class CommonListView extends ListView {

    public CommonListView(Context context) {
        super(context);
        initView();
    }

    public CommonListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommonListView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView(){
        this.setCacheColorHint(Color.TRANSPARENT);
        this.setScrollingCacheEnabled(false);
        this.setHorizontalFadingEdgeEnabled(false);
        //this.setDivider(null);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
