package com.cqgk.clerk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.adapter.ScanProductListAdapter;
import com.cqgk.clerk.bean.normal.ProductDtlBean;

import java.util.List;

/**
 * Created by duke on 16/7/6.
 */

public class ProductListDialog extends AlertDialog {


    private NormalListView mListview;
    private ScanProductListAdapter productListAdapter;
    private List<ProductDtlBean> productItemBeanList;
    private ItemOnClickListener  itemOnClickListener;

    public ItemOnClickListener getItemOnClickListener() {
        return itemOnClickListener;
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public interface ItemOnClickListener{
       public void itemOnclick(int position);
   }


    public ProductListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ProductListDialog(Context context, List<ProductDtlBean> result) {
        super(context, R.style.FullDialogStyle);
        productItemBeanList = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_productlist);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置

        findViewById(R.id.rootview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        initView();
    }

    private void initView() {
        mListview = (NormalListView) findViewById(R.id.listview);
        productListAdapter = new ScanProductListAdapter(getContext(), productItemBeanList);
        mListview.setAdapter(productListAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(itemOnClickListener!=null)
                    itemOnClickListener.itemOnclick(i);
            }
        });
        //mListview.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dialog_enter));
    }
}
