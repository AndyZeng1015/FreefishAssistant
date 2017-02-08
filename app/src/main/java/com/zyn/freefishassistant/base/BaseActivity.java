package com.zyn.freefishassistant.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 文件名 BaseActivity
 * Activity的基类
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/7
 * 版权声明 Created by ZengYinan
 */

public class BaseActivity extends AppCompatActivity {

    protected BaseActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        MyApplication.getInstance().addActivity(this);
    }

    protected void startActivity(Class activity, boolean isClose){
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
        if(isClose){
            mContext.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    protected void startActivity(Class activity, String key, Bundle bundle, boolean isClose){
        Intent intent = new Intent(mContext, activity);
        intent.putExtra(key, bundle);
        startActivity(intent);
        if(isClose){
            mContext.finish();
        }
    }

}
