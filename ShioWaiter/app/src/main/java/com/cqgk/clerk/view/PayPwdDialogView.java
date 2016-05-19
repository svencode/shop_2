package com.cqgk.clerk.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.Basic;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.CheckUtils;


/**
 * Created by duke on 15/11/14.
 */
public class PayPwdDialogView extends Basic {
    private static Dialog dlg;
    private PwdDialogClickListener dialogClickListener;

    public interface PwdDialogClickListener {
        void doConfirm(String text);
    }



    /**
     *
     * @param dialogClickListener
     * @param isChoice
     * @param cancelText
     * @param confirmText
     */
    public static void show(final String cardId, final PwdDialogClickListener dialogClickListener,
                            boolean isChoice,
                            String cancelText,
                            String confirmText) {
        
        if (getActivity() != null && getActivity().isFinishing()){
            AppUtil.showToast("窗口已关闭");
            return;
        }

        if (null != dlg && dlg.isShowing()) {

            dlg.dismiss();

        }

        dlg = new Dialog(getActivity(),R.style.Translucent_NoTitle);

        dlg.show();

        dlg.getWindow().setContentView(R.layout.dialog_pay_pwd);

        final EditText pwdET = (EditText) dlg.getWindow().findViewById(R.id.pwdET);

        dlg.getWindow().findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pwdET.getText().toString().length()!=6){
                    AppUtil.showToast("请输入6位数密码");
                    return;
                }


                RequestUtils.verifyPwd(cardId, pwdET.getText().toString(), new HttpCallBack<String>() {
                    @Override
                    public void success(String result) {
                        dlg.dismiss();
                        if (dialogClickListener != null) dialogClickListener.doConfirm(pwdET.getText().toString());
                    }

                    @Override
                    public boolean failure(int state, String msg) {
                        AppUtil.showToast(msg);
                        return super.failure(state, msg);
                    }
                });



            }
        });
        if(CheckUtils.isAvailable(confirmText)){
           Button button = (Button)dlg.getWindow().findViewById(R.id.btn_confirm);
            button.setText(confirmText);
        }
        if(CheckUtils.isAvailable(cancelText)){
            Button button = (Button)dlg.getWindow().findViewById(R.id.btn_cancel);
            button.setText(cancelText);
        }
        if (isChoice) {
            dlg.getWindow().findViewById(R.id.btn_cancel).setVisibility(View.VISIBLE);
            dlg.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                }
            });
        }







    }
}
