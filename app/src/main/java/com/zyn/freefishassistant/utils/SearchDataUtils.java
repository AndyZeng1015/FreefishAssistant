package com.zyn.freefishassistant.utils;

import com.zyn.freefishassistant.beans.ConfigBean;
import com.zyn.freefishassistant.beans.LimitMapBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名 SearchDataUtils
 * 描述
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/11
 * 版权声明 Created by ZengYinan
 */

public class SearchDataUtils {

    /**
     * 将从数据库取出的数据转换为可搜索使用的数据
     * @param configBeanList
     * @return
     */
    public static List<HashMap<String, String>> parseData(List<ConfigBean> configBeanList){
        List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        for(ConfigBean configBean : configBeanList){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("q", configBean.getKeyMapBean().getKeyword());
            for(LimitMapBean limitMapBean : configBean.getLimitMapBeen()){
                map.put(limitMapBean.getKey(), limitMapBean.getValue());
            }
            data.add(map);
        }
        return data;
    }


}
