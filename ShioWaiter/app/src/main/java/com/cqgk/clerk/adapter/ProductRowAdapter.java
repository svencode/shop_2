package com.cqgk.clerk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.bean.normal.ProductRowBean;
import com.cqgk.clerk.R;
import com.cqgk.clerk.helper.ImageHelper;
import com.cqgk.clerk.utils.ViewHolderUtil;
import com.cqgk.clerk.view.PricesTextView;

import java.util.List;

/**
 * Created by sven on 16/5/9.
 */
public class ProductRowAdapter extends BaseAdapter {
    private Context context;

    private List<ProductDtlBean> valuelist;

    public ProductRowAdapter(Context context) {
        this.context = context;
    }

    public ProductRowAdapter(Context context,List<ProductDtlBean> valuelist) {
        this.context = context;
        this.valuelist = valuelist;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<ProductDtlBean> getValuelist() {
        return valuelist;
    }

    public void setValuelist(List<ProductDtlBean> valuelist) {
        this.valuelist = valuelist;
    }

    @Override
    public int getCount() {
        return valuelist == null ? 0 : valuelist.size();
    }

    @Override
    public ProductDtlBean getItem(int position) {
        return valuelist==null?null:valuelist.get(position);
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

        ProductDtlBean item = valuelist.get(position);

        ImageView imageView = ViewHolderUtil.get(view,R.id.gimg);
        TextView title = ViewHolderUtil.get(view,R.id.title);
        TextView desc = ViewHolderUtil.get(view,R.id.desc);
        PricesTextView price = ViewHolderUtil.get(view,R.id.price);

        ImageHelper.getInstance().display(imageView,item.getLogoImg());
        title.setText(item.getGoodsTitle());
        desc.setText(item.getSpecificationDesc());
        price.setText(String.valueOf(item.getRetailPrice()));

        return view;
    }
}
