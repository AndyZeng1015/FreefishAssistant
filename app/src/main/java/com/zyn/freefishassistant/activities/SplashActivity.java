package com.zyn.freefishassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.zyn.freefishassistant.R;
import com.zyn.freefishassistant.base.BaseActivity;
import com.zyn.freefishassistant.base.MyApplication;
import com.zyn.freefishassistant.beans.ConfigBean;
import com.zyn.freefishassistant.beans.LimitMapBean;
import com.zyn.freefishassistant.utils.ConfigUtils;
import com.zyn.freefishassistant.utils.Contast;
import com.zyn.freefishassistant.utils.JsoupUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名 SplashActivity
 * 描述
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/7
 * 版权声明 Created by ZengYinan
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        loadMain();
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
    }

    private void loadMain(){
        MyApplication.getInstance().execRunnable(new Runnable() {
            @Override
            public void run() {
                //加载搜索数据
                Contast.searchData.clear();
                Contast.searchData = ConfigUtils.getSearchConfig(mContext);
                startActivity(MainActivity.class, true);
            }
        });
    }
}
