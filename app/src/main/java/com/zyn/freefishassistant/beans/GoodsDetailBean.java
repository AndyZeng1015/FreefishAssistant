package com.zyn.freefishassistant.beans;

/**
 * 文件名 GoodsDetailBean
 * 商品的实体类
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/7
 * 版权声明 Created by ZengYinan
 */

public class GoodsDetailBean  implements Comparable{
    private String list_imgUrl;//列表图片地址
    private String url;//商品对应的链接
    private String goodsId;//商品的ID
    private String title;//标题
    private String price;//价格
    private String list_desc;//列表描述内容

    public String getList_imgUrl() {
        return list_imgUrl;
    }

    public void setList_imgUrl(String list_imgUrl) {
        this.list_imgUrl = list_imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getList_desc() {
        return list_desc;
    }

    public void setList_desc(String list_desc) {
        this.list_desc = list_desc;
    }

    @Override
    public int compareTo(Object o) {
        GoodsDetailBean sdto = (GoodsDetailBean)o;

        long otherGoodsId = Long.parseLong(sdto.getGoodsId());

        long thisGoodsId = Long.parseLong(this.goodsId);

        if(thisGoodsId > otherGoodsId){
            return -1;
        }else if(thisGoodsId < otherGoodsId){
            return 1;
        }else{
            return 0;
        }
    }
}
