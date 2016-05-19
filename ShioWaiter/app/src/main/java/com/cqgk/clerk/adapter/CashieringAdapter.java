package com.cqgk.clerk.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.EditBean;
import com.cqgk.clerk.R;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.helper.ImageHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by sven on 16/5/9.
 */
public class CashieringAdapter extends BaseAdapter{
    private Context context;
    private CashieringDelegate delegate;
    private ArrayList<ProductDtlBean> myGood;

    public CashieringAdapter(Context context,CashieringDelegate delegate){
        this.context = context;
        this.delegate = delegate;
    }

    public ArrayList<ProductDtlBean> getMyGood() {
        return myGood;
    }

    public void setMyGood(ArrayList<ProductDtlBean> myGood) {
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
        final ProductDtlBean item = myGood.get(position);
        view = LayoutInflater.from(context).inflate(R.layout.cell_cashiering, null);

        ImageView img = (ImageView)view.findViewById(R.id.imgIV);
        TextView name = (TextView)view.findViewById(R.id.nameTV);
        final EditText price = (EditText)view.findViewById(R.id.priceET);
        TextView originalPriceTV = (TextView)view.findViewById(R.id.originalPriceTV);

        final EditText numET = (EditText)view.findViewById(R.id.numET);
        Button plusBtn = (Button)view.findViewById(R.id.plusBtn);
        Button minusBtn = (Button)view.findViewById(R.id.minusBtn);

        ImageHelper.getInstance().display(img, item.getLogoImg());
        numET.setText("" + item.getNum());
        name.setText(item.getGoodsTitle());

        if (item.getUserPrice()>0){
            price.setText("￥"+item.getUserPrice());
        }else if(item.getVipPrice()>0){
            price.setText("￥"+item.getVipPrice());
        }else {
            price.setText("￥"+item.getRetailPrice());
        }
//        originalPriceTV.setText("￥"+item.getRetailPrice());

        if (1 == item.getIsAllowedModifyPrice()){
            price.setEnabled(true);
            numET.setEnabled(true);
        }else {
            price.setEnabled(false);
            numET.setEnabled(false);
        }

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.goodPlus(item);

            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.goodMinus(item);
            }
        });


        price.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    double newPrice = Double.parseDouble(price.getText().toString().replace("￥", ""));
                    if (newPrice > 0) {
                        delegate.goodPriceEdit(item, newPrice);
                    } else {
                        ((BusinessBaseActivity) context).showToast("请输入价格");
                    }

                }
                return false;
            }
        });


        numET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    double num = Double.parseDouble(numET.getText().toString());
                    if (num > 0) {
                        delegate.goodNunEdit(item, num);
                    } else {
                        ((BusinessBaseActivity) context).showToast("数量需要大于0");
                    }

                }
                return false;
            }
        });


        return view;
    }

    public interface CashieringDelegate{
        void goodPlus(ProductDtlBean item);
        void goodMinus(ProductDtlBean item);
        void goodPriceEdit(ProductDtlBean item,double newPrice);
        void goodNunEdit(ProductDtlBean item,double num);
    }
}
