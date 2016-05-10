package com.cqgk.clerk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cqgk.clerk.R;
import com.cqgk.clerk.bean.normal.EditBean;

/**
 * Created by sven on 16/5/9.
 */
public class CashieringAdapter extends BaseAdapter{
    private Context context;

    public CashieringAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.cell_cashiering, null);


        return view;
    }
}
