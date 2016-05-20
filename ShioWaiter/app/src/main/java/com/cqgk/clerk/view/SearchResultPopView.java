package com.cqgk.clerk.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.SearchResultPopAdapter;
import com.cqgk.clerk.bean.normal.ProductDtlBean;

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


    /**
     * //0编辑商品1-挑选商品
     * @param context
     * @param showtype
     */
    public SearchResultPopView(Context context,int showtype) {
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
        searchResultPopAdapter.setShowtype(showtype);
        searchResultPopAdapter.setValuelist(beanList);
        listview.setAdapter(searchResultPopAdapter);



        setFocusable(true);



    }

    public NormalListView getListView(){
        return listview;
    }

    public SearchResultPopAdapter getAdapter() {
        return searchResultPopAdapter;
    }

}
