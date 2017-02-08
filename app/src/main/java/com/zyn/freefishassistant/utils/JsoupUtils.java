package com.zyn.freefishassistant.utils;

import android.os.SystemClock;
import android.util.Log;

import com.zyn.freefishassistant.base.MyApplication;
import com.zyn.freefishassistant.beans.RequestDataBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

/**
 * 文件名 JsoupUtils
 * 描述
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/7
 * 版权声明 Created by ZengYinan
 */

public class JsoupUtils {

    public static String baseUrl = "https://s.2.taobao.com/list/list.htm";

    public static RequestDataBean getEntityData(Map<String, String> data){
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

            Log.e("TAG", "elementsSize:"+elements.size());

            for(Element element : elements){
                Log.e("TAG", "element:"+element.outerHtml());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
