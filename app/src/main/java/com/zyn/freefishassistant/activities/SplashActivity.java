package com.zyn.freefishassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.zyn.freefishassistant.R;
import com.zyn.freefishassistant.base.BaseActivity;

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
        new Thread(){
            public void run() {
                //睡眠3秒
                SystemClock.sleep(3000);
                startActivity(MainActivity.class, true);
            };
        }.start();
    }
}
