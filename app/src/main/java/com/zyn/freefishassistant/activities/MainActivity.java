package com.zyn.freefishassistant.activities;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zyn.freefishassistant.R;
import com.zyn.freefishassistant.base.BaseActivity;
import com.zyn.freefishassistant.base.MyApplication;
import com.zyn.freefishassistant.utils.JsoupUtils;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private Toolbar tl_bar;
    private DrawerLayout dl_layout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Map<String, HashMap<String, String>> searchData = new HashMap<String, HashMap<String, String>>();//搜索的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        loadData();
    }

    private void loadData() {
        MyApplication.getInstance().execRunnable(new Runnable() {
            @Override
            public void run() {
                Map<String, String> data = new HashMap<String, String>();
                data.put("q", "iphone6s");
                data.put("ist", "0");
                data.put("divisionId", "510100");
                data.put("spm", "2007.1000337.6.2.nEgtu7");
                JsoupUtils.getEntityData(data);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh){
            //重新获取数据

        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        tl_bar.setTitle("闲鱼监控助手");
        tl_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tl_bar);
        mDrawerToggle = new ActionBarDrawerToggle(this,dl_layout,tl_bar,R.string.open, R.string.close);

        dl_layout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //进入主界面后读取配置信息

    }

    private void initView() {
        setContentView(R.layout.activity_main);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        dl_layout = (DrawerLayout) findViewById(R.id.dl_layout);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 将系统当前的时间赋值给exitTime
                exitTime = System.currentTimeMillis();
            } else {
                MyApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
