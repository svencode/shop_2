package com.cqgk.shennong.shop.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cqgk.shennong.shop.R;
import com.cqgk.shennong.shop.adapter.SearchResultPopAdapter;
import com.cqgk.shennong.shop.bean.normal.ProductDtlBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duke on 16/5/17.
 */
public class SearchResultPopView extends PopupWindow {


    private NormalListView listview;
    private SearchResultPopAdapter searchResultPopAdapter;


    public SearchResultPopView(Context context) {
        // 获取自定义布局文件poplayout.xml的视图
        LayoutInflater layoutInflater = (LayoutInflater) (context)
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View popview = layoutInflater.inflate(R.layout.searchresultpopview, null);
        setContentView(popview);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);

        listview = (NormalListView) popview.findViewById(R.id.listview);


        List<ProductDtlBean> beanList = new ArrayList<>();
        searchResultPopAdapter = new SearchResultPopAdapter(context);
        searchResultPopAdapter.setValuelist(beanList);
        listview.setAdapter(searchResultPopAdapter);

    }

    public NormalListView getListView(){
        return listview;
    }

    public SearchResultPopAdapter getAdapter() {
        return searchResultPopAdapter;
    }

}
