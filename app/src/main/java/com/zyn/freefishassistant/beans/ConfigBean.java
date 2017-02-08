package com.zyn.freefishassistant.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名 ConfigBean
 * 搜索配置信息的实体类
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/8
 * 版权声明 Created by ZengYinan
 */

public class ConfigBean {
    private KeyMapBean mKeyMapBean;
    private List<LimitMapBean> mLimitMapBeen;

    public ConfigBean(){
        mLimitMapBeen = new ArrayList<LimitMapBean>();
    }

    public KeyMapBean getKeyMapBean() {
        return mKeyMapBean;
    }

    public void setKeyMapBean(KeyMapBean keyMapBean) {
        mKeyMapBean = keyMapBean;
    }

    public List<LimitMapBean> getLimitMapBeen() {
        return mLimitMapBeen;
    }

    public void setLimitMapBeen(List<LimitMapBean> limitMapBeen) {
        mLimitMapBeen = limitMapBeen;
    }
}
