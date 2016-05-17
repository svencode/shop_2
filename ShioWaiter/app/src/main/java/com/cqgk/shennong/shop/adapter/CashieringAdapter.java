package com.cqgk.shennong.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqgk.shennong.shop.bean.normal.EditBean;
import com.cqgk.shennong.shop.R;
import com.cqgk.shennong.shop.bean.normal.GoodListBean;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by sven on 16/5/9.
 */
public class CashieringAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<GoodListBean.Item> myGood;

    public CashieringAdapter(Context context){
        this.context = context;
    }

    public ArrayList<GoodListBean.Item> getMyGood() {
        return myGood;
    }

    public void setMyGood(ArrayList<GoodListBean.Item> myGood) {
        this.myGood = myGood;
    }

    @Override
    public int getCount() {
        if (null == myGood)return 0;
        return myGood.size();
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
        final GoodListBean.Item item = myGood.get(position);
        view = LayoutInflater.from(context).inflate(R.layout.cell_cashiering, null);

        ImageView img = (ImageView)view.findViewById(R.id.imgIV);
        TextView name = (TextView)view.findViewById(R.id.nameTV);
        TextView price = (TextView)view.findViewById(R.id.priceTV);
        TextView originalPriceTV = (TextView)view.findViewById(R.id.originalPriceTV);

        EditText numET = (EditText)view.findViewById(R.id.numET);
        Button plusBtn = (Button)view.findViewById(R.id.plusBtn);
        Button minusBtn = (Button)view.findViewById(R.id.minusBtn);

        numET.setText(""+item.getNum());
        name.setText(item.getGoodsTitle());
        price.setText(""+item.getRetailPrice());

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item.setNum(item.getNum()+1);
                notifyDataSetChanged();
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setNum(item.getNum()-1);
                if (item.getNum()==0){
                    myGood.remove(item);
                }

                notifyDataSetChanged();
            }
        });

        return view;
    }
}
