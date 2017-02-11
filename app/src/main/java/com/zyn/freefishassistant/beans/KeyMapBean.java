package com.zyn.freefishassistant.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件名 KeyMapBean
 * 描述
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/8
 * 版权声明 Created by ZengYinan
 */

public class KeyMapBean implements Parcelable {
    private String id;
    private String keyword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.keyword);
    }

    public KeyMapBean() {
    }

    protected KeyMapBean(Parcel in) {
        this.id = in.readString();
        this.keyword = in.readString();
    }

    public static final Parcelable.Creator<KeyMapBean> CREATOR = new Parcelable.Creator<KeyMapBean>() {
        @Override
        public KeyMapBean createFromParcel(Parcel source) {
            return new KeyMapBean(source);
        }

        @Override
        public KeyMapBean[] newArray(int size) {
            return new KeyMapBean[size];
        }
    };
}
