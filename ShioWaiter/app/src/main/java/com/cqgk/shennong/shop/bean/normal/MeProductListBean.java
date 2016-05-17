package com.cqgk.shennong.shop.bean.normal;

import java.util.List;

/**
 * Created by duke on 16/5/17.
 */
public class MeProductListBean {

    /**
     * list : [{"goodsId":"7834fdc4-7560-4cd9-8594-cbd6c8a9fdba","goodsTitle":"iwiwi","id":"0e8e4212-08b8-4f33-be6e-adde2c330ca3","retailPrice":20,"specificationDesc":"缺省","vipPrice":10},{"goodsId":"b8c3f9ec-e1ff-41da-81a7-0aa3ebc58133","goodsTitle":"444","id":"1b74bdd0-0b30-49e2-b92e-b595b204dc1c","retailPrice":10,"specificationDesc":"缺省","vipPrice":8},{"goodsId":"ecdcf73d-a3bd-4ff4-8263-5f072e3a3a93","goodsTitle":"8282828","id":"6078175c-d490-4aa5-83c1-175cc68f8530","retailPrice":30,"specificationDesc":"缺省","vipPrice":10},{"goodsId":"592dfffd-4ad4-4a3e-9c91-8d0d15e18933","goodsTitle":"喔喔喔","id":"c46f7954-448a-499f-9297-ab548683373c","retailPrice":20,"specificationDesc":"缺省","vipPrice":10},{"goodsId":"89cdb007-41c0-4fab-af9d-b74460f0aacc","goodsTitle":"234","id":"d83db93e-611a-47c1-8969-d8c087daa213","retailPrice":122,"specificationDesc":"缺省","vipPrice":100}]
     * total : 5
     */

    private int total;
    /**
     * goodsId : 7834fdc4-7560-4cd9-8594-cbd6c8a9fdba
     * goodsTitle : iwiwi
     * id : 0e8e4212-08b8-4f33-be6e-adde2c330ca3
     * retailPrice : 20.0
     * specificationDesc : 缺省
     * vipPrice : 10.0
     */

    private List<ProductDtlBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ProductDtlBean> getList() {
        return list;
    }

    public void setList(List<ProductDtlBean> list) {
        this.list = list;
    }


}
