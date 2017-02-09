package com.zyn.freefishassistant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zyn.freefishassistant.R;
import com.zyn.freefishassistant.base.BaseActivity;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * 文件名 WebViewActivity
 * 当没有安装闲鱼客户端的时候，以网页的形式展开
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/9
 * 版权声明 Created by ZengYinan
 */

public class WebViewActivity extends BaseActivity{

    private Toolbar tl_bar;
    private XWalkView wv_view;
    private String url;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        wv_view.setUIClient(new XWalkUIClient(wv_view){
            public void onPageLoadStarted(XWalkView view,String url){
                mDialog.show();
            }
            public void  onPageLoadStopped(XWalkView view,String url,XWalkUIClient.LoadStatus status) {
                mDialog.dismiss();
            }

        });
    }

    private void initData() {

        tl_bar.setTitle("宝贝详情");
        tl_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tl_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wv_view.load(url, null);
        // 开启调试
        //XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        setContentView(R.layout.activity_webview);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        wv_view = (XWalkView) findViewById(R.id.wv_view);
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("正在加载数据...");
        mDialog.setCancelable(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wv_view != null) {
            wv_view.pauseTimers();
            wv_view.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wv_view != null) {
            wv_view.resumeTimers();
            wv_view.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_view != null) {
            wv_view.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        if (wv_view != null) {
            wv_view.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (wv_view != null) {
            wv_view.onNewIntent(intent);
        }
    }
}
