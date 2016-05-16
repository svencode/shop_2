package com.cqgk.shennong.shop.http;

import org.xutils.http.annotation.HttpResponse;

/**
 * json 返回值示例, 如果它作为Callback的泛型,
 * 那么xUtils将自动调用JsonResponseParser将字符串转换为BaiduResponse.
 *
 * @HttpResponse 注解 和 ResponseParser接口仅适合做json, xml等文本类型数据的解析,
 * 如果需要其他类型的解析可参考:
 * {@link org.xutils.http.loader.LoaderFactory}
 * 和 {@link org.xutils.common.Callback.PrepareCallback}.
 * LoaderFactory提供PrepareCallback第一个泛型参数类型的自动转换,
 * 第二个泛型参数需要在prepare方法中实现.
 * (LoaderFactory中已经默认提供了部分常用类型的转换实现, 其他类型需要自己注册.)
 */
@org.xutils.http.annotation.HttpResponse(parser = JsonResponseParser.class)
public class CommonHttpResponse {
    // some properties

    private String requestUrl;
    private String content;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return content;
    }
}
