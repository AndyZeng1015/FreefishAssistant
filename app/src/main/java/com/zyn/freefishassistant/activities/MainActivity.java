package com.zyn.freefishassistant.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zyn.freefishassistant.R;
import com.zyn.freefishassistant.adapters.GoodsListAdapter;
import com.zyn.freefishassistant.base.BaseActivity;
import com.zyn.freefishassistant.base.MyApplication;
import com.zyn.freefishassistant.beans.ConfigBean;
import com.zyn.freefishassistant.beans.GoodsDetailBean;
import com.zyn.freefishassistant.beans.ItemParams;
import com.zyn.freefishassistant.beans.LimitMapBean;
import com.zyn.freefishassistant.utils.ConfigUtils;
import com.zyn.freefishassistant.utils.Contast;
import com.zyn.freefishassistant.utils.JsoupUtils;
import com.zyn.freefishassistant.utils.SearchDataUtils;
import com.zyn.freefishassistant.utils.SharedPreferencesUtils;
import com.zyn.freefishassistant.utils.ToastUtil;
import com.zyn.freefishassistant.widget.DividerLine;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MainActivity extends BaseActivity {

    private Toolbar tl_bar;
    private DrawerLayout dl_layout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView rlv_list;
    private TextView tv_nodata;
    private NavigationView ngv_menu;

    private List<GoodsDetailBean> mGoodsDetailBeanList = new ArrayList<>();

    private GoodsListAdapter mGoodsListAdapter;
    private ProgressDialog mDialog;

    private Lock lock;//锁对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        loadData();
        initListener();
    }

    private void initListener() {
        mGoodsListAdapter.setOnItemClickListener(new GoodsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(GoodsDetailBean goodsDetailBean, int pos) {
                Intent intent = new Intent("android.intent.action.MAIN");
                ComponentName componentName = new ComponentName("com.taobao.idlefish", "com.taobao.fleamarket.detail.activity.ItemDetailActivity");
                intent.setComponent(componentName);
                ItemParams itemParams = new ItemParams();
                itemParams.setItemId(goodsDetailBean.getGoodsId());
                intent.putExtra("ItemParams", itemParams);
                String url = "http://h5.xianyu.tb.cn/xianyu/share-item.html?itemId=%d";
                url = String.format(url, Long.valueOf(goodsDetailBean.getGoodsId()));
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                try {

                    startActivityForResult(intent, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showToast(mContext, "安装闲鱼即可跳转app直接购买商品");
                    Intent webIntent = new Intent(MainActivity.this, WebViewActivity.class);
                    url = "https://h5.m.taobao.com/2shou/newDetail.html?page=item&id="+ goodsDetailBean.getGoodsId();
                    webIntent.putExtra("url", url);
                    startActivityForResult(webIntent, 200);
                }
            }
        });

        ngv_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setCheckable(false);
                switch (item.getItemId()){
                    case R.id.manager:
                        //监控管理
                        startActivity(LimitManagerActivity.class, false);
                        break;
                    case R.id.add:
                        //添加监控
                        startActivity(AddLimitActivity.class, false);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 201){
                if(mGoodsDetailBeanList.size()>0){
                    tv_nodata.setVisibility(View.GONE);
                }else{
                    tv_nodata.setVisibility(View.VISIBLE);
                }
                mGoodsListAdapter.updateListDataAndRefresh(mGoodsDetailBeanList);
                mDialog.dismiss();
            }
        }
    };

    private void loadData() {
        if(Contast.searchData.size() > 0){

            mGoodsDetailBeanList.clear();
            mDialog.show();

            for(final HashMap<String, String> map : SearchDataUtils.parseData(Contast.searchData)){
                //读取上次搜索的第一个id
                MyApplication.getInstance().execRunnable(new Runnable() {
                    @Override
                    public void run() {
                        String lastIds = SharedPreferencesUtils.getString(mContext, "FreefishAssistant", map.get("q"), "");
                        List<GoodsDetailBean> goodsDetailBeanList = JsoupUtils.getEntityData(mContext, map, lastIds);
                        Log.e("TAG",map.get("q")+"：执行完成");
                        if(goodsDetailBeanList != null && goodsDetailBeanList.size()>0){
                            //保存这次搜索的第一个id
                            SharedPreferencesUtils.saveString(mContext, "FreefishAssistant", map.get("q"), goodsDetailBeanList.get(0).getGoodsId());
                            mGoodsDetailBeanList.addAll(goodsDetailBeanList);
                        }
                        mHandler.sendEmptyMessage(201);
                    }
                });
            }
        }
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
            loadData();
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

        if(mGoodsDetailBeanList.size() > 0){
            tv_nodata.setVisibility(View.GONE);
        }else{
            tv_nodata.setVisibility(View.VISIBLE);
        }
        mGoodsListAdapter = new GoodsListAdapter(mContext, mGoodsDetailBeanList);
        rlv_list.setAdapter(mGoodsListAdapter);
    }

    private void initView() {
        lock = new ReentrantLock();
        setContentView(R.layout.activity_main);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        dl_layout = (DrawerLayout) findViewById(R.id.dl_layout);
        rlv_list = (RecyclerView) findViewById(R.id.rlv_list);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        ngv_menu = (NavigationView) findViewById(R.id.ngv_menu);
        rlv_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rlv_list.setHasFixedSize(true);
        //添加分割线
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(0xFFe9e9e9);
        rlv_list.addItemDecoration(dividerLine);

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("正在加载数据...");
        mDialog.setCancelable(false);
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
