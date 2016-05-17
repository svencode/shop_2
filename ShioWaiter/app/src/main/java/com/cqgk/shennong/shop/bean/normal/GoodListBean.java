package com.cqgk.shennong.shop.bean.normal;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sven on 16/5/16.
 */
public class GoodListBean {
    private int total;
    private ArrayList<ProductDtlBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<ProductDtlBean> getList() {
        return list;
    }

    public void setList(ArrayList<ProductDtlBean> list) {
        this.list = list;
    }



}


