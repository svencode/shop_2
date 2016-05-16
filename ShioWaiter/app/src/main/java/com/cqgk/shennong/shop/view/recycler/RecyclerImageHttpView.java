package com.cqgk.shennong.shop.view.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cqgk.shennong.shop.helper.ImageHelper;
import com.cqgk.shennong.shop.utils.CheckUtils;


/**
 * Created by duke on 16/5/5.
 */
public class RecyclerImageHttpView extends ImageView {

    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public RecyclerImageHttpView(Context context) {
        super(context);
    }


    public RecyclerImageHttpView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public RecyclerImageHttpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(CheckUtils.isAvailable(imgUrl))
           ImageHelper.getInstance().displayZhan(this, imgUrl);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}
