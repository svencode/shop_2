package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.view.CommonDialogView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 输入金额充值
 * Created by duke on 16/5/11.
 */
@ContentView(R.layout.inputmoney)
public class InputMoney extends BusinessBaseActivity {

    @ViewInject(R.id.summoney)
    TextView summoney;
    @ViewInject(R.id.inputmoney)
    EditText inputmoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("充值金额");

        inputmoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                summoney.setText(String.format("￥%s", inputmoney.getText().toString()));
            }
        });
    }

    @Event(R.id.congnow)
    private void congnow_click(View view) {
        if (!CheckUtils.isAvailable(inputmoney.getText().toString())) {
            showLongToast("请输入您要充值的金额");
            return;
        }

        long imoney = Long.parseLong(inputmoney.getText().toString());
        if (imoney % 100 != 0) {
            showLongToast("充值金额必须为100元的整倍数");
            return;
        }
        CommonDialogView.show("请确定您收到客户的现金后再进行充值", new CommonDialogView.DialogClickListener() {
            @Override
            public void doConfirm() {
                NavigationHelper.getInstance().startPaySelect();
            }
        }, true, false, "", "继续");
    }
}
