package com.cqgk.clerk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.cqgk.clerk.helper.NumberFormatHelper;
import com.cqgk.clerk.utils.AbStrUtil;
import com.cqgk.clerk.utils.LogUtil;


/**
 * 自定义价格显示组件
 * Created by duke on 16/1/6.
 */
public class PricesTextView extends TextView {

    /**
     * 边框面板的高度
     */
    private int mBorderPaneHeight;

    private Paint mBorderPaint;
    private float mBorderSize;

    public PricesTextView(Context context) {
        super(context);
        initView();
    }

    public PricesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        this.setGravity(Gravity.CENTER);
    }

    public void setDouble(double text) {
        setText(String.valueOf(text));
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        if(text!=null) {
            text = getFormatText(text.toString());
        }
        super.setText(text, type);
    }

    private String getFormatText(String text) {

        try {
            Double prices = AbStrUtil.parseEmptyDouble(text);
            String formattext = NumberFormatHelper.getFormat().format(prices);
            if (formattext.indexOf(".00") > -1) {
                formattext = formattext.replace(".00", "");
            }
            return formattext;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(String.format("error:"+text));
            //MobclickAgent.reportError(getContext(), "error:" + e.getMessage());
        }
        return text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
