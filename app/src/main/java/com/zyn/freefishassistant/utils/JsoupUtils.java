package com.zyn.freefishassistant.utils;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zyn.freefishassistant.activities.MainActivity;
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
    public static int queryCount = 0;

    /**
     * 获取商品列表，最多9条
     *
     * @param data 参数数据
     * @return
     */
    public static List<GoodsDetailBean> getEntityData(Context mContext, Map<String, String> data, String lastId) {

        List<GoodsDetailBean> goodsDetailBeanList = new ArrayList<GoodsDetailBean>();

        if (data.get("is_Show").equals("不显示")) {
            return goodsDetailBeanList;
        }

        String url = baseUrl + "?t=" + System.currentTimeMillis();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (!entry.getKey().equals("is_Show")) {
                String key = entry.getKey();
                String value = "";
                value = entry.getValue();
                url += "&" + key + "=" + value;
            }
        }

        try {
            url = URLEncoderURI.encode(url, "gbk");

            Log.e("TAG", "url:" + url);

            OkHttpClient client = new OkHttpClient();

            Request request = null;

            request = new Request
                    .Builder()
                    .url(url)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                    .addHeader("Cache-Control", "max-age=0")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Cookie", SharedPreferencesUtils.getString(mContext, "FREEFISH", "cookie", "_uab_collina=152282227792711970906309; cna=bnHpELL7nzQCAavdiPI9SjXO; thw=cn; miid=7060675441659287368; UM_distinctid=1623bd6f08a5a2-0463b9f45665af-5d4e211f-1fa400-1623bd6f08b7d4; l=AnNzInOP/O9tWlkJBr9lctElg32btQdq; enc=4BSuBlBVVCj%2FytowVooinSE08TYPVVahskmDT5qe5evHChiM1pE5uwZ4crdpN07s5VT9N0xQ7WDZKjX8XSMu9A%3D%3D; _umdata=09FE5CB1CF9363E0C87DA08CFBDF43C0211C3E081E70B2E8826FE2B670125B34EB5F4C55456F73CDCD43AD3E795C914CF11B4A3FD09F31043F12FF4DD5D9C128; CNZZDATA30057895=cnzz_eid%3D1849410573-1522994845-%26ntime%3D1522994845; _tb_token_=7833337313b03; hng=CN%7Czh-CN%7CCNY%7C156; v=0; uc3=nk2=ty5ADnl3KA%3D%3D&id2=W80p86Fpa88J&vt3=F8dBz4PAjlLGtM%2Fhv1U%3D&lg2=VFC%2FuZ9ayeYq2g%3D%3D; existShop=MTUyMzM3NDM4Ng%3D%3D; lgc=%5Cu66FE%5Cu6960zyn; tracknick=%5Cu66FE%5Cu6960zyn; dnk=%E6%9B%BE%E6%A5%A0zyn; cookie2=37cc2112b6b4db280faf477dec06761f; sg=n20; csg=98ff5729; cookie1=Vv0hf2hRANimAnUBf9w%2FuRx1uRtQuC9B7eC5o572tCM%3D; unb=841099872; skt=2a86f5889d746e33; t=60c2578b2fdf1e39d49f0c187e56b603; publishItemObj=Ng%3D%3D; _cc_=WqG3DMC9EA%3D%3D; tg=0; _l_g_=Ug%3D%3D; _nk_=%5Cu66FE%5Cu6960zyn; cookie17=W80p86Fpa88J; CNZZDATA1252911424=1091689292-1522821212-https%253A%252F%252Fs.2.taobao.com%252F%7C1523370164; CNZZDATA30058275=cnzz_eid%3D80617283-1522818273-https%253A%252F%252Fs.2.taobao.com%252F%26ntime%3D1523370560; uc1=cookie16=VT5L2FSpNgq6fDudInPRgavC%2BQ%3D%3D&cookie21=UtASsssmfaCOMId3WwGQmg%3D%3D&cookie15=Vq8l%2BKCLz3%2F65A%3D%3D&existShop=true&pas=0&cookie14=UoTePTbATtwQiw%3D%3D&tag=8&lng=zh_CN; mt=ci=11_1; isg=BImJ5WjZN_SOTMt888u6woPWmLUjfnwPBY2yYyv-BXCvcqmEcyaN2HegsNZEKhVA"))
                    .addHeader("Host", "s.2.taobao.com")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                    .build();

            Response response = client.newCall(request).execute();

            String htmlSource = "";
            if (response.isSuccessful()) {
                htmlSource = response.body().string();
                Log.e("ZYN", "htmlSource:"+htmlSource);
            }

            Document doc = Jsoup.parse(htmlSource);

            String html = doc.outerHtml();
            String[] htmls = html.split("div");

            Elements elements = doc.getElementsByClass("item-info-wrapper item-idle clearfix");

            int count = 0;
            if (elements.size() >= 9) {
                count = 9;
            } else {
                count = elements.size();
            }

            //每一类只取前9个
            for (int i = 0; i < count; i++) {
                Element element = elements.get(i);
                int minute = Integer.parseInt(element.getElementsByClass("item-pub-time").get(0).text().replace("分钟前", ""));
                if (minute > 30) {
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
        queryCount++;
        if (queryCount >= 2000) {
            queryCount = 0;
        }
        return goodsDetailBeanList;
    }
}
