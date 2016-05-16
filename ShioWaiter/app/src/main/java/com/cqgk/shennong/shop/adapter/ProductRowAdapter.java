package com.cqgk.shennong.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cqgk.shennong.shop.bean.normal.ProductRowBean;
import com.cqgk.shennong.shop.R;

import java.util.List;

/**
 * Created by sven on 16/5/9.
 */
public class ProductRowAdapter extends BaseAdapter {
    private Context context;

    private List<ProductRowBean> valuelist;

    public ProductRowAdapter(Context context) {
        this.context = context;
    }

    public ProductRowAdapter(Context context,List<ProductRowBean> valuelist) {
        this.context = context;
        this.valuelist = valuelist;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<ProductRowBean> getValuelist() {
        return valuelist;
    }

    public void setValuelist(List<ProductRowBean> valuelist) {
        this.valuelist = valuelist;
    }

    @Override
    public int getCount() {
        return valuelist == null ? 0 : valuelist.size();
    }

    @Override
    public ProductRowBean getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.productrowitem, null);
        }

        ProductRowBean item = valuelist.get(position);


        return view;
    }
}
