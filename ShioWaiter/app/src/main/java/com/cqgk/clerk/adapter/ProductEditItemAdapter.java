package com.cqgk.clerk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.bean.normal.EditBean;
import com.cqgk.clerk.helper.ImageHelper;
import com.cqgk.clerk.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ProductEditItemAdapter extends BaseAdapter {

    private List<EditBean> valueList;
    private Context context;


    public ProductEditItemAdapter(Context context) {
        valueList = new ArrayList<>();
        this.context = context;
    }

    public ProductEditItemAdapter(Context context, List<EditBean> valueList) {
        this.valueList = valueList;
        this.context = context;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public List<EditBean> getValueList() {
        return valueList;
    }

    public void setValueList(List<EditBean> valueList) {
        this.valueList = valueList;
    }


    public void addValueLIst(List<EditBean> valueList) {
        if (this.valueList == null)
            this.valueList = new ArrayList<>();


        this.valueList.addAll(valueList);

    }

    @Override
    public int getCount() {
        return valueList.size();
    }

    @Override
    public EditBean getItem(int i) {
        return valueList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        EditBean editBean = valueList.get(i);
        if (editBean.getPhotoInfo() == null) {
            view = LayoutInflater.from(context).inflate(R.layout.addnewitem, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.product_selitem, null);
            ImageView myimg = ViewHolderUtil.get(view, R.id.myimg);
            ImageHelper.getInstance().displayFile(myimg, "file://"+editBean.getPhotoInfo().getPhotoPath());
        }
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
