package com.zyn.freefishassistant.utils;

import android.os.SystemClock;
import android.util.Log;

import com.zyn.freefishassistant.base.MyApplication;
import com.zyn.freefishassistant.beans.GoodsDetailBean;
import com.zyn.freefishassistant.beans.RequestDataBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
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

    public static RequestDataBean getEntityData(Map<String, String> data){

        RequestDataBean requestDataBean = new RequestDataBean();
        requestDataBean.setGoodsDetailBeanList(new ArrayList<GoodsDetailBean>());

        String url = baseUrl + "?t="+ System.currentTimeMillis();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            url += "&"+key + "=" + value;
        }
        try {
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.getElementsByClass("item-info-wrapper item-idle clearfix");

            //每一类只取前9个
            for (int i = 0; i < 9; i++) {
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

                requestDataBean.getGoodsDetailBeanList().add(goodsDetailBean);
            }

            int currentPage = Integer.parseInt(doc.getElementsByClass("paginator-curr").get(0).text());
            int totalPage = Integer.parseInt(doc.getElementsByClass("paginator-count").get(0).text().replace("共", "").replace("页", ""));
            requestDataBean.setCurrentPage(currentPage);
            requestDataBean.setTotalPage(totalPage);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return requestDataBean;
    }
}
