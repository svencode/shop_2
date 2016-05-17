package com.cqgk.shennong.shop.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cqgk.shennong.shop.bean.normal.GoodListBean;

import com.cqgk.shennong.shop.R;

import java.util.ArrayList;


/**
 * Created by sven on 16/5/12.
 */
public class PickGoodAdapter extends BaseAdapter{
    private Context context;
    private PickGoodDelegate delegate;

    private ArrayList<GoodListBean.Item> topGoodList;
    private ArrayList<GoodListBean.Item> myGood;

    public  PickGoodAdapter(Context context,PickGoodDelegate delegate){
        this.context = context;
        this.delegate = delegate;
        topGoodList = new ArrayList<>();
        myGood = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    public void stContext(Context context) {
        this.context = context;
    }

    public ArrayList<GoodListBean.Item> getTopGoodList() {
        return topGoodList;
    }

    public void setTopGoodList(ArrayList<GoodListBean.Item> topGoodList) {
        this.topGoodList = topGoodList;
    }

    public ArrayList<GoodListBean.Item> getMyGood() {
        return myGood;
    }

    public void setMyGood(ArrayList<GoodListBean.Item> myGood) {
        this.myGood = myGood;
    }

    @Override
    public int getCount() {
        return 2+myGood.size()+topGoodList.size()/2+topGoodList.size()%2;
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
        if ( 0 == position){
            view = LayoutInflater.from(context).inflate(R.layout.cell_title, null);
            TextView name = (TextView) view.findViewById(R.id.nameTV);
            name.setText("本单商品");
            view.setBackgroundResource(R.color.bg_color);
        }else if ( (myGood.size()+1) == position){
            view = LayoutInflater.from(context).inflate(R.layout.cell_title, null);
            TextView name = (TextView) view.findViewById(R.id.nameTV);
            name.setText("本店热销");
            view.setBackgroundResource(R.color.bg_color);
        }else if (position>0 && position<=(myGood.size()+1)){
            view = LayoutInflater.from(context).inflate(R.layout.cell_good_one, null);
            ImageView img = (ImageView)view.findViewById(R.id.imgIV);
            TextView name = (TextView)view.findViewById(R.id.nameTV);
            TextView desc = (TextView)view.findViewById(R.id.descTV);
            TextView price = (TextView)view.findViewById(R.id.priceTV);

        }else {
            view = LayoutInflater.from(context).inflate(R.layout.cell_good_two, null);

            LinearLayout good1 = (LinearLayout)view.findViewById(R.id.good1LL);
            LinearLayout good2 = (LinearLayout)view.findViewById(R.id.good2LL);
            good2.setVisibility(View.GONE);

            int index = position-(myGood.size()+2);


            final GoodListBean.Item item1 = topGoodList.get(index*2);
            ImageView img1 = (ImageView)view.findViewById(R.id.img1IV);
            TextView name1 = (TextView)view.findViewById(R.id.name1TV);
            TextView price1 = (TextView)view.findViewById(R.id.price1TV);
            name1.setText(item1.getGoodsTitle());
            good1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegate.topGoodClick(item1);
                }
            });

            if (topGoodList.size()>(index*2+1)){
                final GoodListBean.Item item2 = topGoodList.get(index*2+1);
                ImageView img2 = (ImageView)view.findViewById(R.id.img2IV);
                TextView name2 = (TextView)view.findViewById(R.id.name2TV);
                TextView price2 = (TextView)view.findViewById(R.id.price2TV);
                name2.setText(item2.getGoodsTitle());

                good2.setVisibility(View.VISIBLE);
                good2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delegate.topGoodClick(item2);
                    }
                });
            }
        }

        return view;
    }

    public interface PickGoodDelegate{
        void topGoodClick(GoodListBean.Item item);
    }
}


