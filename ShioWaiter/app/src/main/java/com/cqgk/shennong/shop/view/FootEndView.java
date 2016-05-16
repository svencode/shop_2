package com.cqgk.shennong.shop.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cqgk.shennong.shop.R;


/**
 * 自定义行组件
 * Created by duke on 15/12/23.
 */
public class FootEndView {
    public static View getFootView(Context context,String text) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.footview, null);
        TextView textView = (TextView) view.findViewById(R.id.footer_text);
        textView.setText(text);
        return view;
    }
}
