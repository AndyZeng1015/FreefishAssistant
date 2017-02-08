package com.zyn.freefishassistant.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名 RequestDataBean
 * 请求获取的商品信息
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/7
 * 版权声明 Created by ZengYinan
 */

public class RequestDataBean {
    private List<GoodsDetailBean> mGoodsDetailBeanList;
    private int totalPage;//总页数
    private int currentPage;//当前页数

    public RequestDataBean(){
        mGoodsDetailBeanList = new ArrayList<GoodsDetailBean>();
    }

    public List<GoodsDetailBean> getGoodsDetailBeanList() {
        return mGoodsDetailBeanList;
    }

    public void setGoodsDetailBeanList(List<GoodsDetailBean> goodsDetailBeanList) {
        mGoodsDetailBeanList = goodsDetailBeanList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
