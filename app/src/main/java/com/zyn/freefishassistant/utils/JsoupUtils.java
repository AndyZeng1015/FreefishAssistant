package com.zyn.freefishassistant.utils;

import android.util.Log;

import com.zyn.freefishassistant.beans.GoodsDetailBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件名 JsoupUtils
 * 使用jsoup对html数据进行解析
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/7
 * 版权声明 Created by ZengYinan
 */

public class JsoupUtils {

    public static String baseUrl = "https://s.2.taobao.com/list/list.htm";

    /**
     * 获取商品列表，最多9条
     * @param data 参数数据
     * @return
     */
    public static List<GoodsDetailBean> getEntityData(Map<String, String> data){

        List<GoodsDetailBean> goodsDetailBeanList = new ArrayList<GoodsDetailBean>();

        String url = baseUrl + "?t="+ System.currentTimeMillis();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            url += "&"+key + "=" + value;
        }

        Log.e("TAG", "url:"+url);

        try {
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.getElementsByClass("item-info-wrapper item-idle clearfix");
            int count = 0;
            if(elements.size() >= 9){
                count = 9;
            }else{
                count = elements.size();
            }
            //每一类只取前9个
            for (int i = 0; i < count; i++) {
                Element element = elements.get(i);
                GoodsDetailBean goodsDetailBean = new GoodsDetailBean();
                //商品列表图片
                String list_imgUrl = element.getElementsByTag("img").get(0).attr("src");
                list_imgUrl = list_imgUrl.substring(2, list_imgUrl.length());
                //商品链接
                String goods_url = element.getElementsByTag("a").get(0).attr("href");
                //商品ID
                String id = goods_url.split("id=")[1];
                //商品标题
                String title = element.getElementsByTag("a").get(1).text();
                //商品价格
                String price = element.getElementsByTag("em").get(0).text();
                //商品描述
                String desc = element.getElementsByClass("item-description").get(0).text();

                goodsDetailBean.setGoodsId(id);
                goodsDetailBean.setList_desc(desc);
                goodsDetailBean.setList_imgUrl(list_imgUrl);
                goodsDetailBean.setPrice(price);
                goodsDetailBean.setTitle(title);
                goodsDetailBean.setUrl(goods_url);

                goodsDetailBeanList.add(goodsDetailBean);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return goodsDetailBeanList;
    }
}
