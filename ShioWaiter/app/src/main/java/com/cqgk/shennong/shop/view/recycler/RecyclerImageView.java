package com.cqgk.shennong.shop.view.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by duke on 16/5/5.
 */
public class RecyclerImageView extends ImageView {


    public RecyclerImageView(Context context) {
        super(context);
    }


    public RecyclerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public RecyclerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        try {
//            Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
//            bitmap.recycle();
//        }catch (Exception e){
//
//        }
        setImageDrawable(null);
    }
}
