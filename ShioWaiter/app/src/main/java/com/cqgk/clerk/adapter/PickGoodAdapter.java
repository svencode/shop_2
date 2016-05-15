package com.cqgk.clerk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqgk.clerk.R;

/**
 * Created by sven on 16/5/12.
 */
public class PickGoodAdapter extends BaseAdapter{
    private Context context;

    public  PickGoodAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
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
        if ( 0 == position || 4 == position){
            view = LayoutInflater.from(context).inflate(R.layout.cell_title, null);
            TextView name = (TextView) view.findViewById(R.id.nameTV);
            name.setText(0==position?"本单商品":"本店热销");
            view.setBackgroundResource(R.color.bg_color);
        }else if (position>0 && position<4){
            view = LayoutInflater.from(context).inflate(R.layout.cell_good_one, null);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.cell_good_two, null);
        }

        return view;
    }
}
