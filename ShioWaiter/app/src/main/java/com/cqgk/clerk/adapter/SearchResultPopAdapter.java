package com.cqgk.clerk.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.helper.ImageHelper;
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.ViewHolderUtil;
import com.cqgk.clerk.view.PricesTextView;

import java.util.List;

/**
 * Created by duke on 16/5/17.
 */
public class SearchResultPopAdapter extends BaseAdapter {
    private Context context;

    private List<ProductDtlBean> valuelist;
    private ItemListener  itemListener;
    private int showtype;

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public SearchResultPopAdapter(Context context) {
        this.context = context;
    }

    public SearchResultPopAdapter(Context context, List<ProductDtlBean> valuelist) {
        this.context = context;
        this.valuelist = valuelist;
    }

    public ItemListener getItemListener() {
        return itemListener;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public interface ItemListener{
        public void itemClick(int i);
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

    public void addValuelist(List<ProductDtlBean> valuelist){
        this.valuelist.addAll(valuelist);
    }

    @Override
    public int getCount() {
        return valuelist == null ? 0 : valuelist.size();
    }

    @Override
    public ProductDtlBean getItem(int position) {
        return valuelist == null ? null : valuelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.poplistitem, null);
        }



        ProductDtlBean item = valuelist.get(position);
        item.setNum(1);

        TextView productname = ViewHolderUtil.get(view, R.id.productname);
        productname.setText(item.getGoodsTitle());

        PricesTextView prices = ViewHolderUtil.get(view, R.id.prices);
        prices.setText(String.valueOf(item.getRetailPrice()));

        TextView qtytv = ViewHolderUtil.get(view, R.id.qtytv);
        Button btn_add = ViewHolderUtil.get(view, R.id.numadd);
        qtytv.setText(Html.fromHtml(String.format("<font color=\"red\">%s</font>件",item.getNum())));
        numadd(btn_add, qtytv, position);

        itemClick(view,position);

        if(showtype==0){
            qtytv.setVisibility(View.GONE);
            btn_add.setVisibility(View.GONE);
        }
        if(showtype==1){
            qtytv.setVisibility(View.VISIBLE);
            btn_add.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void itemClick(View view,final int i){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemListener!=null)
                    itemListener.itemClick(i);
            }
        });
    }

    private void numadd(View view, final TextView qtyty, final int i) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDtlBean bean = valuelist.get(i);
                bean.setNum(bean.getNum() + 1);
                qtyty.setText(Html.fromHtml(String.format("<font color=\"red\">%s</font>件",bean.getNum())));
            }
        });
    }
}
