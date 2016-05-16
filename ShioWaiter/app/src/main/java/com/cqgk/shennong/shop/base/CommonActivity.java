package com.cqgk.shennong.shop.base;


import com.cqgk.shennong.shop.helper.ProgressDialogHelper;
import com.cqgk.shennong.shop.utils.AppUtil;
import com.cqgk.shennong.shop.utils.LogUtil;

/**
 * @author duke
 */
public class CommonActivity extends HandlerActivity {

    private String tag = this.getClass().getName();

    public void showProgressDialog() {
        ProgressDialogHelper.get().show();
    }

    public void dismissProgressDialog() {
        ProgressDialogHelper.get().dismiss();
    }

    public void showToast(String text) {
        AppUtil.showToast(text);
    }

    public void showLongToast(String text) {
        AppUtil.showLongToast(text);
    }

    public void loge(String text) {
        LogUtil.e(tag, text);
    }

    public void loge(String text, Throwable r) {
        LogUtil.e(tag, text, r);
    }

    public void logi(String text) {
        LogUtil.i(tag, text);
    }

    public void logi(String text, Throwable r) {
        LogUtil.i(tag, text, r);
    }

    public void logv(String text) {
        LogUtil.v(tag, text);
    }

    public void logv(String text, Throwable r) {
        LogUtil.v(tag, text, r);
    }

    public void logw(String text) {
        LogUtil.w(tag, text);
    }

    public void logw(String text, Throwable r) {
        LogUtil.w(tag, text, r);
    }

    public void logd(String text) {
        LogUtil.d(tag, text);
    }

    public void logd(String text, Throwable r) {
        LogUtil.d(tag, text, r);
    }

    public Object getSerializableExtra(String key) {
        try {
            return (Object) this.getIntent().getSerializableExtra(key);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getStringExtra(String key) {
        try {
            return (String) this.getIntent().getStringExtra(key);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "";
        }

    }
}