package com.zyn.freefishassistant.beans;

import android.os.Parcel;
import android.os.Parcelable;

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

public class ConfigBean implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mKeyMapBean, flags);
        dest.writeList(this.mLimitMapBeen);
    }

    protected ConfigBean(Parcel in) {
        this.mKeyMapBean = in.readParcelable(KeyMapBean.class.getClassLoader());
        this.mLimitMapBeen = new ArrayList<LimitMapBean>();
        in.readList(this.mLimitMapBeen, LimitMapBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ConfigBean> CREATOR = new Parcelable.Creator<ConfigBean>() {
        @Override
        public ConfigBean createFromParcel(Parcel source) {
            return new ConfigBean(source);
        }

        @Override
        public ConfigBean[] newArray(int size) {
            return new ConfigBean[size];
        }
    };
}
