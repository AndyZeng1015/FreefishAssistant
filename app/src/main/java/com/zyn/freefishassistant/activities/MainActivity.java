package com.zyn.freefishassistant.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.zyn.freefishassistant.utils.JsoupUtils;
import com.zyn.freefishassistant.utils.ToastUtil;
import com.zyn.freefishassistant.widget.DividerLine;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {

    private Toolbar tl_bar;
    private DrawerLayout dl_layout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView rlv_list;
    private TextView tv_nodata;

    private List<GoodsDetailBean> mGoodsDetailBeanList = new ArrayList<>();

    private GoodsListAdapter mGoodsListAdapter;
    private ProgressDialog mDialog;

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
                ComponentName componentName = new ComponentName("com.taobao.fleamarket", "com.taobao.fleamarket.detail.activity.ItemDetailActivity");
                intent.setComponent(componentName);
                ItemParams itemParams = new ItemParams();
                itemParams.setItemId(goodsDetailBean.getGoodsId());
                intent.putExtra("ItemParams", itemParams);
                String url = "https://h5.m.taobao.com/2shou/newDetail.html?page=item&id=%d";
                url = String.format(url, Long.valueOf(goodsDetailBean.getGoodsId()));
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                try {

                    startActivityForResult(intent, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showToast(mContext, "安装闲鱼即可跳转app直接购买商品");
                    Intent webIntent = new Intent(MainActivity.this, WebViewActivity.class);
                    webIntent.putExtra("url", url);
                    startActivityForResult(webIntent, 200);
                }
            }
        });
    }

    private void loadData() {
        //进入主界面后读取配置信息
//        mGoodsDetailBeanList.clear();
//        List<ConfigBean> configBeanList = ConfigUtils.getSearchConfig(mContext);
//        for(ConfigBean configBean : configBeanList){
//            Map<String, String> searchData = new HashMap<String, String>();
//            searchData.put("p", configBean.getKeyMapBean().getKeyword());
//            for(LimitMapBean limitMapBean : configBean.getLimitMapBeen()){
//                searchData.put(limitMapBean.getKey(), limitMapBean.getValue());
//            }
//            mGoodsDetailBeanList.addAll(JsoupUtils.getEntityData(searchData));
//        }
        new AsyncTask<Void, Void, List<GoodsDetailBean>>(){

            @Override
            protected void onPreExecute() {
                mDialog.show();
            }

            @Override
            protected List<GoodsDetailBean> doInBackground(Void... voids) {
                Map<String, String> data = new HashMap<String, String>();
                data.put("q", "iphone6s");
                data.put("ist", "0");
                data.put("spm", "2007.1000337.6.2.nEgtu7");
                List<GoodsDetailBean> goodsDetailBeanList = JsoupUtils.getEntityData(data);
                return goodsDetailBeanList;
            }

            @Override
            protected void onPostExecute(List<GoodsDetailBean> goodsDetailBeen) {
                if(goodsDetailBeen.size() > 0){
                    tv_nodata.setVisibility(View.GONE);
                }else{
                    tv_nodata.setVisibility(View.VISIBLE);
                }
                //Collections.sort(goodsDetailBeen);
                mGoodsListAdapter.updateListDataAndRefresh(goodsDetailBeen);
                mDialog.dismiss();
            }
        }.execute();
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
        setContentView(R.layout.activity_main);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        dl_layout = (DrawerLayout) findViewById(R.id.dl_layout);
        rlv_list = (RecyclerView) findViewById(R.id.rlv_list);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
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
