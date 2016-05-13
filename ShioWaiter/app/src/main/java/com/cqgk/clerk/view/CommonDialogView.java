package com.cqgk.clerk.view;

import android.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.Basic;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.utils.AppUtil;


/**
 * Created by duke on 15/11/14.
 */
public class CommonDialogView extends Basic {
    private static AlertDialog dlg;
    private DialogClickListener dialogClickListener;

    public interface DialogClickListener {
        void doConfirm();
    }

    public static void show(String content, final DialogClickListener dialogClickListener) {
        show(null,content, dialogClickListener, true, false);
    }

    /**
     *
     */
    public static void showLoginDialog(){
        show("你的操作需要登录,是否马上进行登录?", new CommonDialogView.DialogClickListener() {
            @Override
            public void doConfirm() {
                NavigationHelper.getInstance().startLoginActivity();
            }
        });
    }

    /**
     *
     * @param content 对话框内容
     * @param dialogClickListener 确认按钮回调事件
     * @param isChoice 是否显示选择模式对话框
     * @param isHtmlContent 是否显示html格式内容
     */
    public static void show(String title,String content,
                            final DialogClickListener dialogClickListener,
                            boolean isChoice,
                            boolean isHtmlContent) {
        
        if (getActivity() != null && getActivity().isFinishing()){
            AppUtil.showToast("窗口已关闭");
            return;
        }

        if (null != dlg && dlg.isShowing()) {

            dlg.dismiss();

        }

        dlg = new AlertDialog.Builder(getActivity()).create();
        dlg.show();
        dlg.getWindow().setContentView(R.layout.dialog_style_1);
        dlg.getWindow().findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
                if (dialogClickListener != null) dialogClickListener.doConfirm();
            }
        });
        if (isChoice) {
            dlg.getWindow().findViewById(R.id.btn_cancel).setVisibility(View.VISIBLE);
            dlg.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                }
            });
        }
        dlg.getWindow().findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        TextView content_tv = (TextView) dlg.getWindow().findViewById(R.id.content);
        if (isHtmlContent) {
            content_tv.setText(Html.fromHtml(content));
        } else {
            content_tv.setText(content);
        }


        TextView title_tv = (TextView) dlg.getWindow().findViewById(R.id.title);
        if (null == title){
            title_tv.setText(getResources().getString(R.string.app_name));
        }else {
            title_tv.setText(title);
        }

    }
}
