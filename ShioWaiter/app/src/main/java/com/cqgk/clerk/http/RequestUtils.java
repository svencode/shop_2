package com.cqgk.clerk.http;

import android.util.Log;

import com.alipay.mobilesecuritysdk.deviceID.LOG;
import com.cqgk.clerk.bean.logicbean.OrderSubmitBean;
import com.cqgk.clerk.bean.logicbean.WechatResultBean;
import com.cqgk.clerk.bean.normal.AdsBean;
import com.cqgk.clerk.bean.normal.CardDtlBean;
import com.cqgk.clerk.bean.normal.DeviceBindBean;
import com.cqgk.clerk.bean.normal.DeviceItemBean;
import com.cqgk.clerk.bean.normal.DeviceListBean;
import com.cqgk.clerk.bean.normal.EditBean;
import com.cqgk.clerk.bean.normal.FileUploadResultBean;
import com.cqgk.clerk.bean.normal.HomeAdsBean;
import com.cqgk.clerk.bean.normal.HomeBean;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.JIesuanReturnBean;
import com.cqgk.clerk.bean.normal.JieSuanScanMemberResultBean;
import com.cqgk.clerk.bean.normal.JiesuanScanSubmit;
import com.cqgk.clerk.bean.normal.LoginResultBean;
import com.cqgk.clerk.bean.normal.MeProductListBean;
import com.cqgk.clerk.bean.normal.MembercardActBean;
import com.cqgk.clerk.bean.normal.OrderSubmitResultBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.bean.normal.ProductStandInfoBean;
import com.cqgk.clerk.bean.normal.RechargeResultBean;
import com.cqgk.clerk.bean.normal.ShopInfoBean;
import com.cqgk.clerk.config.Key;
import com.cqgk.clerk.helper.PreferencesHelper;
import com.cqgk.clerk.utils.CheckUtils;
import com.cqgk.clerk.utils.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by duke on 16/5/12.
 */
public class RequestUtils {

    /**
     * 删除商品
     *
     * @param goodsid
     * @param callBack
     */
    public static void deleteClerkGoods(String goodsid, HttpCallBack<String> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_deleteClerkGoods));
        params.addParameter("goodsId", goodsid);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }

    /**
     * @param phoneNumber
     * @param verifyCode
     * @param newpwd
     * @param callBack
     */
    public static void findPwd(String phoneNumber, String verifyCode, String newpwd, HttpCallBack<String> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_findPwd));
        params.addParameter("phoneNumber", phoneNumber);
        params.addParameter("verifyCode", verifyCode);
        params.addParameter("password", newpwd);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * 结算价格改变
     */
    public static void settleReCalculate(String memberBarCode, String couponBarCode, List<ProductDtlBean> goods,
                                         HttpCallBack<JIesuanReturnBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_settleRecalculate));
        JiesuanScanSubmit jiesuanScanSubmit = new JiesuanScanSubmit();
        if (CheckUtils.isAvailable(memberBarCode)) {
            jiesuanScanSubmit.setMemberBarCode(memberBarCode);
        }

        if (CheckUtils.isAvailable(couponBarCode)) {
            jiesuanScanSubmit.setCouponBarCode(couponBarCode);
        }

        List<JiesuanScanSubmit.ProductInfoEntity> entityList = new ArrayList<>();
        for (int i = 0; i < goods.size(); i++) {
            JiesuanScanSubmit.ProductInfoEntity item = new JiesuanScanSubmit.ProductInfoEntity();
            item.setGsid(goods.get(i).getId());
            item.setNum(String.valueOf(goods.get(i).getNum()));
            if (goods.get(i).getUserPrice() > 0) {
                item.setPrice(String.valueOf(goods.get(i).getUserPrice()));
            } else {

            }
//            item.setPrice(String.valueOf(goods.get(i).getPrice()));
            entityList.add(item);
        }
        jiesuanScanSubmit.setGOODS(entityList);
        params.setBodyContent(new Gson().toJson(jiesuanScanSubmit));
        Log.e("content", params.getBodyContent());
        RequestHelper.sendPost(true, params, callBack);
    }

    /**
     * 提交数据：
     * {
     * "barCode":"会员卡二维码",
     * "GOODS":[
     * {
     * "gsid"："商品规格编号"，
     * "price"："商品价格（手动修改后的价格，没有修改则传空）"，
     * "num" ："商品数量"
     * },
     * {
     * "gsid"："商品规格编号"，
     * "price"："商品价格（手动修改后的价格，没有修改则传空）"，
     * "num" ："商品数量"
     * }
     * ...
     * ]
     * }
     */
//    public static void settleScanMemberCard(String barcode, List<ProductDtlBean> goods, HttpCallBack<JieSuanScanMemberResultBean> callBack) {
//        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_settleScanMemberCard));
//
//        JiesuanScanSubmit jiesuanScanSubmit = new JiesuanScanSubmit();
//        jiesuanScanSubmit.setBarCode(barcode);
//
//        List<JiesuanScanSubmit.ProductInfoEntity> entityList = new ArrayList<>();
//        for (int i = 0; i < goods.size(); i++) {
//            JiesuanScanSubmit.ProductInfoEntity item = new JiesuanScanSubmit.ProductInfoEntity();
//            item.setGsid(goods.get(i).getGoodsId());
//            item.setNum(String.valueOf(goods.get(i).getNum()));
//            item.setPrice(String.valueOf(goods.get(i).getPrice()));
//        }
//        jiesuanScanSubmit.setGOODS(entityList);
//
//        params.setBodyContent(new Gson().toJson(jiesuanScanSubmit));
//        RequestHelper.sendPost(true, params, callBack);
//    }

    /**
     * 店铺名称
     *
     * @param callBack
     */
    public static void queryServiceNickName(HttpCallBack<ShopInfoBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryServiceNickName));
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * 首页广告
     *
     * @param callBack
     */
    public static void homeads(HttpCallBack<List<AdsBean>> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryAdsByPosition));
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }

    /**
     * 用户退出
     *
     * @param callBack
     */
    public static void logout(HttpCallBack<String> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_logout));
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * 根据Id查询商品
     *
     * @param goodsId
     * @param callBack
     */
    public static void queryClerkGoodsById(String goodsId, HttpCallBack<ProductDtlBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkGoodsById));
        params.addParameter("goodsId", goodsId);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }

    /**
     * 使用条码查询店小二上传的商品
     *
     * @param barcode
     * @param callBack
     */
    public static void queryClerkGoodsByBarcode(String barcode, HttpCallBack<MeProductListBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkGoodsByBarcode));
        params.addParameter("barcode", barcode);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * 查询商品标准信息
     *
     * @param barcode
     * @param callBack
     */
    public static void queryGoodsStandardInfo(String barcode, HttpCallBack<ProductStandInfoBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryGoodsStandardInfo));
        params.addParameter("barcode", barcode);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * 获取短信验证码
     *
     * @param verifyCodeType
     * @param phoneNumber
     * @param callBack
     */
    public static void getVerifyCode(String verifyCodeType, String phoneNumber, HttpCallBack<String> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_randomcode));
        params.addParameter("verifyCodeType", verifyCodeType);
        params.addParameter("phoneNumber", phoneNumber);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }

    /**
     * 获取微信支付参数
     *
     * @param payCode
     * @param gateway
     * @param callBack - 10：店小二微信支付通道
     *                 - 8：农掌柜微信支付通道
     *                 - 9：新农宝微信支付通道
     */
    public static void prepareWeixinPay(String payCode, String gateway, HttpCallBack<WechatResultBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_prepareWeixinPay));
        params.addParameter("payCode", payCode);
        params.addParameter("gateway", gateway);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * @param keyword
     * @param pageIndex
     * @param pageSize
     * @param callBack
     */
    public static void queryClerkGoodsByKey(String keyword, String pageIndex, String pageSize, HttpCallBack<MeProductListBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkGoods));
        params.addParameter("keyword", keyword);
        params.addParameter("pageIndex", pageIndex);
        params.addParameter("pageSize", pageSize);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }

    /**
     * 查询上传所有的商品
     *
     * @param pageIndex
     * @param pageSize
     * @param callBack
     */
    public static void allProdct(String pageIndex, String pageSize, HttpCallBack<MeProductListBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkGoods));
        params.addParameter("pageIndex", pageIndex);
        params.addParameter("pageSize", pageSize);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * 会员开卡
     * @param card_id
     * @param card_name
     * @param card_mobile
     * @param birth_day
     * @param card_password
     * @param sex
     * @param callBack
     */
    public static void membercardAct(String card_id,
                                     String card_name,
                                     String card_mobile,
                                     String birth_day,
                                     String card_password,
                                     String sex,
                                     String recommend_phone,
                                     HttpCallBack<MembercardActBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_membercardActivate));
        params.addParameter("card_id", card_id);
        params.addParameter("card_name", card_name);
        params.addParameter("card_mobile", card_mobile);
        params.addParameter("birth_day", birth_day);
        params.addParameter("card_password", card_password);
        params.addParameter("gender", sex);
        params.addParameter("recommend_phone", recommend_phone);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);

    }

    /**
     * 会员卡充值->获取paycode
     *
     * @param card_id
     * @param amount
     * @param callBack
     */
    public static void vipRecharge(String card_id, String amount, HttpCallBack<RechargeResultBean> callBack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_recharge));
        params.addParameter("card_id", card_id);
        params.addParameter("amount", amount);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * 卡信息
     *
     * @param card_id
     * @param callBlack
     */
    public static void cardInfo(String card_id, HttpCallBack<CardDtlBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_getCardInfo));
        params.addParameter("card_id", card_id);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

    /**
     * 检测卡有效性
     *
     * @param card_id
     * @param callBlack
     */
    public static void checkCardState(String card_id, HttpCallBack<String> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_checkCardStatus));
        params.addParameter("card_id", card_id);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }


    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param callBlack
     */
    public static void userlogin(String username, String password, HttpCallBack<LoginResultBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_login));
        params.addParameter("username", username);
        params.addParameter("password", password);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }


    /**
     * 首页
     *
     * @param callBlack
     */
    public static void homedata(HttpCallBack<HomeBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_incomeStatistics));
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }


    /**
     *
     * @param keyword
     * @param pageIndex
     * @param callBlack
     */
    public static void searchShopGood(String keyword, int pageIndex, HttpCallBack<GoodListBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkGoodsByKey));
        if (null == keyword) {
            keyword = "";
        }
        params = getLoginParams(params);
        params.addParameter("keyword", keyword);
        params.addParameter("pageIndex", pageIndex + "");
        params.addParameter("pageSize", 10 + "");
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }


    /* 搜索商品
     * @param keyword
     * @param pageIndex
     * @param callBlack
     */
    public static void searchGood(String keyword, int pageIndex, HttpCallBack<GoodListBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkGoodsListPageByKeys));
        if (null == keyword) {
            keyword = "";
        }
        params = getLoginParams(params);
        params.addParameter("keyword", keyword);
        params.addParameter("pageIndex", pageIndex + "");
        params.addParameter("pageSize", 10 + "");
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

    /**
     * 文件上传
     *
     * @param filepath
     * @param filename
     * @param callBlack
     */
    public static void fileUpload(String filepath, String filename, HttpCallBack<FileUploadResultBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_appUpload));
        params.setHeader("x_file_name", filename);
        params.setMultipart(true);
        try {
            params.addBodyParameter("file",
                    new FileInputStream(new File(filepath)), null, filename);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        RequestHelper.sendPost(true, params, callBlack);
    }

    /**
     * {
     * "id":"商品ID",
     * "barCode":"商品的条形码",
     * "title":"商品的名称",
     * "retailPrice":"商品零售价",
     * "vipPrice":"商品的会员价",
     * "logoId":"商品主图文件ID",
     * "photoIdList":"商品附图文件ID列表"
     * }
     */
    public static void productUpdate(String id,
                                     String barcode,
                                     String title,
                                     String retailPrice,
                                     String vipPrice,
                                     String logoId,
                                     String iphoneIds,
                                     HttpCallBack<String> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_doSaveGoods));
        params.addParameter("id", CheckUtils.isAvailable(id) ? id : "");
        params.addParameter("barCode", barcode);
        params.addParameter("title", title);
        params.addParameter("retailPrice", retailPrice);
        params.addParameter("vipPrice", vipPrice);
        params.addParameter("logoId", logoId);
        params.addParameter("photoIdList", iphoneIds);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

    /* 提交订单
    * @param MCID
    * @param CCID
    * @param goods
    * @param callBlack
    */
    public static void submitOrder(String MCID, String CCID, ArrayList<ProductDtlBean> goods, HttpCallBack<OrderSubmitResultBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_submitOrder));

        ArrayList<OrderSubmitBean.SubmitGood> list = new ArrayList<>();

        OrderSubmitBean bean = new OrderSubmitBean();
        bean.setMCID(MCID);
        bean.setCCID(CCID);

        for (ProductDtlBean item : goods) {
            OrderSubmitBean.SubmitGood good = new OrderSubmitBean.SubmitGood();
            good.setGsid(item.getId());
            if (item.getReturnPrice()>0){
                good.setPrice(""+item.getReturnPrice());
            }

            good.setNum(item.getNum() + "");

            list.add(good);
        }

        bean.setGOODS(list);
        params.setBodyContent(new Gson().toJson(bean));
        Log.e("content", params.getBodyContent());
        RequestHelper.sendPost(true, params, callBlack);
    }


    /**
     * 查询本店热销商品
     *
     * @param callBlack
     */
    public static void queryTopGoodsList(HttpCallBack<List<ProductDtlBean>> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkTopGoodsList));
        RequestHelper.sendPost(true, params, callBlack);
    }

    private static CommonParams getLoginParams(CommonParams params) {
//        params.setHeader("x_token", PreferencesHelper.find(Key.TOKEN,""));
        return params;
    }

    /**
     * 校验支付密码
     *
     * @param cardId
     * @param pwd
     * @param callBlack
     */
    public static void verifyPwd(String cardId, String pwd, HttpCallBack<String> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_settleCheckCardPwd));
        params.addBodyParameter("memberCardId", cardId);
        params.addBodyParameter("cardPwd", pwd);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

    /**
     * 添加打印架
     * @param deviceSerialNumber
     * @param deviceName
     * @param callBlack
     */
    public static void device_bind(String deviceSerialNumber, String deviceName, HttpCallBack<DeviceItemBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_device_bind));
        params.addBodyParameter("deviceSerialNumber", deviceSerialNumber);
        params.addBodyParameter("deviceName", deviceName);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

    /**
     *
     * @param callBlack
     */
    public static void device_list(HttpCallBack<DeviceListBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_device_list));
        RequestHelper.sendPost(true, params, callBlack);
    }


    /**
     *
     * @param deviceSerialNumber
     * @param printTimes
     * @param orderId
     * @param callBlack
     */
    public static void device_print(String deviceSerialNumber, String printTimes, String orderId,HttpCallBack<String> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_device_print));
        params.addBodyParameter("deviceSerialNumber", deviceSerialNumber);
        params.addBodyParameter("printTimes", printTimes);
        params.addBodyParameter("orderId", orderId);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

    /**
     * 删除打印机
     * @param deviceSerialNumber
     * @param callBlack
     */
    public static void device_del(String deviceSerialNumber,HttpCallBack<String> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_device_del));
        params.addBodyParameter("deviceSerialNumber", deviceSerialNumber);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

}
