package com.cqgk.clerk.http;

import com.cqgk.clerk.utils.AppUtil;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @param <T>
 * @author duke
 */
public abstract class HttpCallBack<T> {

    private final Type type;

    public HttpCallBack() {
        Type mySuperClass = getClass().getGenericSuperclass();
        type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    /**
     *  - 200：调用正常，表示已按照正常业务流程处理；
     - 400：请求传入的参数不合法被服务端拒绝；
     - 500：服务端处理出现异常；
     - 401：用户未登录或者会话已超期；
     - 403：用户执行未授权的操作；
     - 510：登录失败（用户名或者密码错误）；
     - 511：登录失败（用户验证通过，但无权限登录该功能）；
     - 601：会员卡不存在；
     - 602：会员卡已被使用；
     */
    void handle(String resultJsonStr) {
        if (!CheckUtils.isAvailable(resultJsonStr)) {
            AppUtil.showToast("服务器空返回.(Server Response Empty)");
            return;
        }

        ResponseVo responseVo = null;

        try {
            JSONObject jsonObject = new JSONObject(resultJsonStr);
            responseVo = new ResponseVo();
            responseVo.retCode = jsonObject.optInt("code", Integer.MIN_VALUE);
            responseVo.msg = jsonObject.optString("msg");
            responseVo.data = jsonObject.optString("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //返回格式有问题或没值返回
        if (responseVo == null || Integer.MIN_VALUE == responseVo.retCode) {
            if (!failure(Integer.MIN_VALUE, "json format error or no value response")) {
                AppUtil.showToast("json format error or no value response");
            }
            return;
        }


        if (200 == responseVo.retCode) {
            try {
                //返回字符串
                if (type.toString().contains("java.lang.String")) {
                    success((T) responseVo.data, responseVo.msg);

                } else {


                    Object object = GsonUtil.parseGson(responseVo.data, type);
                    success((T) object, responseVo.msg);


                }


//                if (type.toString().contains("java.lang.String")
//                        && !CheckUtils.isAvailable(responseVo.msg)) {
//                    success((T) "", responseVo.msg);
//
//                } else if (!CheckUtils.isAvailable(responseVo.data)
//                        && !CheckUtils.isAvailable(responseVo.msg)) {
//                    AppUtil.showToast("操作成功");
//                } else if (type.toString().contains("java.lang.String")) {
//                    success((T) responseVo.data, responseVo.msg);
//                } else {
//                    Object object = GsonUtil.parseGson(responseVo.data, type);
//                    success((T) object, responseVo.msg);
//                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                if (!failure(0, "返回格式有误")) {// 失败
                }
            }
        }

        //返回失败
        if (!failure(responseVo.retCode, responseVo.msg)) {

            //AppUtil.showToast(String.format("code:%s,msg:%s", responseVo.retCode, responseVo.msg));
        }

    }

    public abstract void success(T result, String msg);

    /**
     * 返回正确则处理，否则不出
     *
     * @param state
     * @param msg
     * @return
     */
    public boolean failure(int state, String msg) {
        //LogUtil.e("hanler result faild___");
        return false;
    }

    public void onLoading(int progess) {

    }

    public void onFinished() {

    }

    static class ResponseVo {
        int retCode = Integer.MIN_VALUE;
        String msg;
        String data;
        String error;
        String detail;
    }


}
