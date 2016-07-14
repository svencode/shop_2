package com.cqgk.clerk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.utils.ViewHolderUtil;
import com.cqgk.clerk.view.PricesTextView;

import java.util.List;


/**
 * Created by sven on 16/5/12.
 */
public class ScanProductListAdapter extends BaseAdapter{

    private List<ProductDtlBean> valuelist;

    public List<ProductDtlBean> getValuelist() {
        return valuelist;
    }

    public void setValuelist(List<ProductDtlBean> valuelist) {
        this.valuelist = valuelist;
    }

    private Context context;

    public ScanProductListAdapter(Context context,List<ProductDtlBean> productItemBeanList){
        this.context = context;
        this.valuelist = productItemBeanList;
    }

    @Override
    public int getCount() {
        return valuelist==null?0:valuelist.size();
    }

    @Override
    public ProductDtlBean getItem(int position) {
        return valuelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.adapter_scanproductitem, null);
        }

        ProductDtlBean  item = valuelist.get(position);
        TextView product_name= ViewHolderUtil.get(view,R.id.product_name);
        PricesTextView product_price= ViewHolderUtil.get(view,R.id.product_price);

        product_name.setText(item.getGoodsTitle());
        product_price.setText(String.valueOf(item.getPrice()));

        return view;
    }
}
