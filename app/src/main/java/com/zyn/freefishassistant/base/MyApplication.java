package com.zyn.freefishassistant.base;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件名 MyApplication
 * Application
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/7
 * 版权声明 Created by ZengYinan
 */

public class MyApplication extends Application {

    private List<Activity> activityList = new LinkedList<Activity>();

    private ExecutorService es;//线程池

    public void execRunnable(Runnable r){
        es.execute(r);
    }

    private static MyApplication instance;
    public static MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        es = Executors.newFixedThreadPool(3);
    }

    //退出
    public void exit(){
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //将Activity从容器中移除
    public void removeActivity(Activity activity){ activityList.remove(activity); }

    //清空Activity
    public void clearActivity(){
        activityList.clear();
    }


}
