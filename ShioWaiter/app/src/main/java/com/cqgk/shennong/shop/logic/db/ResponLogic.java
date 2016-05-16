package com.cqgk.shennong.shop.logic.db;


import com.cqgk.shennong.shop.base.Basic;
import com.cqgk.shennong.shop.bean.dbbean.ResponBean;
import com.cqgk.shennong.shop.helper.DBHelper;
import com.cqgk.shennong.shop.utils.LogUtil;

/**
 * http返回缓存
 * Created by duke on 16/4/13.
 */
public class ResponLogic extends Basic {
    /**
     * 保存http
     *
     * @param url
     * @param respon
     */
    public static void saveRespon(String url, String respon) {
        ResponBean responBean = new ResponBean();
        responBean.setResponstr(respon);
        responBean.setUrl(url);

        ResponBean bean = DBHelper.getInstance(getAppContext()).getResond(url);
        if (bean != null) {
            //LogUtil.e(String.format("update:%s",url));
            DBHelper.getInstance(getAppContext()).updateRespond(responBean);
            return;
        }

        //LogUtil.e(String.format("insert:%s",url));
        DBHelper.getInstance(getAppContext()).insertRespond(responBean);

    }

    public static ResponBean findRespon(String url) {
        ResponBean item = DBHelper.getInstance(getAppContext()).getResond(url);
        if(item!=null)
               LogUtil.e(String.format("cache:%s", item.getResponstr()));

        return item;
    }
}
