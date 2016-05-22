package com.cqgk.clerk.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.utils.LogUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by sven on 16/5/9.
 */
public class CashieringAdapter extends BaseAdapter {
    private Context context;
    private CashieringDelegate delegate;
    private ArrayList<ProductDtlBean> myGood;

    private int handlePricePosition;
    private int handleNumPosition;
    private double handlePrice;
    private double handleNum;

    Handler handler = new Handler();
    Runnable editPriceRun = new Runnable() {
        @Override
        public void run() {
            delegate.goodPriceEdit(getItem(handlePricePosition),handlePrice);
        }
    };

    Runnable editNumRun = new Runnable() {
        @Override
        public void run() {
            delegate.goodNunEdit(getItem(handleNumPosition), handleNum);
        }
    };

    public CashieringAdapter(Context context, CashieringDelegate delegate) {
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
        if (null == myGood) return 0;
        return myGood.size();
    }

    @Override
    public ProductDtlBean getItem(int position) {
        return myGood == null ? null : myGood.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ProductDtlBean item = myGood.get(position);
        view = LayoutInflater.from(context).inflate(R.layout.cell_cashiering, null);

        ImageView img = (ImageView) view.findViewById(R.id.imgIV);
        TextView name = (TextView) view.findViewById(R.id.nameTV);
        final EditText price = (EditText) view.findViewById(R.id.priceET);
        TextView originalPriceTV = (TextView) view.findViewById(R.id.originalPriceTV);

        final EditText numET = (EditText) view.findViewById(R.id.numET);
        Button plusBtn = (Button) view.findViewById(R.id.plusBtn);
        Button minusBtn = (Button) view.findViewById(R.id.minusBtn);

        ImageHelper.getInstance().display(img, item.getLogoImg());
        numET.setText("" + item.getNum());
        name.setText(item.getGoodsTitle());

        if (item.getUserPrice() > 0) {
            price.setText("￥" + item.getUserPrice());
        } else if (item.getReturnPrice() > 0) {
            price.setText("￥" + item.getReturnPrice());
        } else {
            price.setText("￥" + item.getRetailPrice());
        }


        if (item.getReturnPrice() != item.getRetailPrice()) {
            SpannableString sp = new SpannableString("￥" + item.getRetailPrice());
            sp.setSpan(new StrikethroughSpan(), 0, sp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            originalPriceTV.setText(sp);
            originalPriceTV.setVisibility(View.VISIBLE);
        } else {
            originalPriceTV.setVisibility(View.GONE);
        }


        if (1 == item.getIsAllowedModifyPrice()) {
            price.setEnabled(true);
            numET.setEnabled(true);
        } else {
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

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handlePricePosition = position;
                handler.removeCallbacks(editPriceRun);
                if (CheckUtils.isAvailable(s.toString().replace("￥",""))){
                    handlePrice = Double.parseDouble(s.toString().replace("￥",""));
                }else {
                    handlePrice = 0;
                }
                handler.postDelayed(editPriceRun,3000);
            }
        });

        numET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handleNumPosition = position;
                handler.removeCallbacks(editNumRun);
                if (CheckUtils.isAvailable(s.toString())){
                    handleNum = Double.parseDouble(s.toString());
                }else {
                    handleNum = 0;
                }
                handler.postDelayed(editNumRun,3000);
            }
        });



        return view;
    }





    public interface CashieringDelegate {
        void goodPlus(ProductDtlBean item);

        void goodMinus(ProductDtlBean item);

        void goodPriceEdit(ProductDtlBean item, double newPrice);

        void goodNunEdit(ProductDtlBean item, double num);


    }




}
