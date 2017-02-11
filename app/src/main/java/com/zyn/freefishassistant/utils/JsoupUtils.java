package com.zyn.freefishassistant.utils;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zyn.freefishassistant.beans.GoodsDetailBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    public static List<GoodsDetailBean> getEntityData(Context mContext, Map<String, String> data, String lastId){

        List<GoodsDetailBean> goodsDetailBeanList = new ArrayList<GoodsDetailBean>();

        if(data.get("is_Show").equals("不显示")){
            return goodsDetailBeanList;
        }

        String url = baseUrl + "?t="+ System.currentTimeMillis();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if(!entry.getKey().equals("is_Show")){
                String key = entry.getKey();
                String value = "";
                value = entry.getValue();
                url += "&"+key + "=" + value;
            }
        }

        try {
            url = URLEncoderURI.encode(url, "gbk");

            Log.e("TAG", "url:"+url);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            String htmlSource = "";
            if (response.isSuccessful()) {
                htmlSource = response.body().string();
            }

            Document doc = Jsoup.parse(htmlSource);

            String html = doc.outerHtml();
            String[] htmls = html.split("div");

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
                int minute = Integer.parseInt(element.getElementsByClass("item-pub-time").get(0).text().replace("分钟前", ""));
                if(minute > 30){
                    break;
                }
                GoodsDetailBean goodsDetailBean = new GoodsDetailBean();
                //商品列表图片
                String list_imgUrl = element.getElementsByTag("img").get(0).attr("src");
                list_imgUrl = list_imgUrl.substring(2, list_imgUrl.length());
                //商品链接
                String goods_url = element.getElementsByTag("a").get(0).attr("href");
                //商品ID
                String id = goods_url.split("id=")[1];

//                if(id.equals(lastId)){
//                    //这是上次搜索的第一个
//                    break;//不显示了
//                }

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

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return goodsDetailBeanList;
    }
}
