package com.cqgk.clerk.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.bean.normal.GoodListBean;

/**
 * Created by sven on 16/5/12.
 */
public class PickGoodAdapter extends BaseAdapter{
    private Context context;

    private GoodListBean topGoodList;
    private GoodListBean myGood;

    public  PickGoodAdapter(Context context){
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public GoodListBean getTopGoodList() {
        return topGoodList;
    }

    public void setTopGoodList(GoodListBean topGoodList) {
        this.topGoodList = topGoodList;
    }

    public GoodListBean getMyGood() {
        return myGood;
    }

    public void setMyGood(GoodListBean myGood) {
        this.myGood = myGood;
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
            ImageView img = (ImageView)view.findViewById(R.id.imgIV);
            TextView name = (TextView)view.findViewById(R.id.nameTV);
            TextView desc = (TextView)view.findViewById(R.id.descTV);
            TextView price = (TextView)view.findViewById(R.id.priceTV);

        }else {
            view = LayoutInflater.from(context).inflate(R.layout.cell_good_two, null);

            ImageView img1 = (ImageView)view.findViewById(R.id.img1IV);
            TextView name1 = (TextView)view.findViewById(R.id.name1TV);
            TextView price1 = (TextView)view.findViewById(R.id.price1TV);

            ImageView img2 = (ImageView)view.findViewById(R.id.img2IV);
            TextView name2 = (TextView)view.findViewById(R.id.name2TV);
            TextView price2 = (TextView)view.findViewById(R.id.price2TV);

        }

        return view;
    }
}
