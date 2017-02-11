package com.zyn.freefishassistant.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件名 LimitMapBean
 * 描述
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/8
 * 版权声明 Created by ZengYinan
 */

public class LimitMapBean implements Parcelable {
    private String id;
    private String keyword_id;
    private String key;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public String getKeyword_id() {
        return keyword_id;
    }

    public void setKeyword_id(String keyword_id) {
        this.keyword_id = keyword_id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.keyword_id);
        dest.writeString(this.key);
        dest.writeString(this.value);
    }

    public LimitMapBean() {
    }

    protected LimitMapBean(Parcel in) {
        this.id = in.readString();
        this.keyword_id = in.readString();
        this.key = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<LimitMapBean> CREATOR = new Parcelable.Creator<LimitMapBean>() {
        @Override
        public LimitMapBean createFromParcel(Parcel source) {
            return new LimitMapBean(source);
        }

        @Override
        public LimitMapBean[] newArray(int size) {
            return new LimitMapBean[size];
        }
    };
}
