package com.zyn.freefishassistant.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.zyn.freefishassistant.R;
import com.zyn.freefishassistant.adapters.LimitListAdapter;
import com.zyn.freefishassistant.base.BaseActivity;
import com.zyn.freefishassistant.beans.ConfigBean;
import com.zyn.freefishassistant.beans.KeyMapBean;
import com.zyn.freefishassistant.utils.ConfigUtils;
import com.zyn.freefishassistant.utils.Contast;
import com.zyn.freefishassistant.utils.SearchDataUtils;
import com.zyn.freefishassistant.widget.DividerLine;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件名 LimitManagerActivity
 * 监控管理
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/9
 * 版权声明 Created by ZengYinan
 */

public class LimitManagerActivity extends BaseActivity {

    private Toolbar tl_bar;
    private RecyclerView rlv_list;
    private LimitListAdapter mLimitListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        loadData();
        initListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListener() {
        //点击修改
        mLimitListAdapter.setOnClickListener(new LimitListAdapter.OnClickListener() {
            @Override
            public void onClick(int pos) {
                ConfigBean configBean = Contast.searchData.get(pos);
                Intent intent = new Intent(LimitManagerActivity.this, UpdateLimitActivity.class);
                intent.putExtra("configBean", configBean);
                startActivityForResult(intent, 200);
            }
        });

        //长按删除
        mLimitListAdapter.setOnLongClickListener(new LimitListAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(final int pos) {
                AlertDialog.Builder isDelete = new AlertDialog.Builder(mContext);
                isDelete.setMessage("是否确认删除该条过滤信息？");
                isDelete.setNegativeButton("取消", null);
                isDelete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConfigBean configBean = Contast.searchData.get(pos);
                        //删除数据库中的
                        ConfigUtils.deleteSearchConfigById(mContext, configBean.getKeyMapBean().getId());
                        //删除缓存的
                        Contast.searchData.remove(pos);
                        mLimitListAdapter.updateData(SearchDataUtils.parseData(Contast.searchData));//刷新数据
                        dialog.dismiss();
                    }
                });
                isDelete.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        //在退出管理页面的时候进行保存
        super.onDestroy();
    }

    private void loadData() {
        mLimitListAdapter = new LimitListAdapter(mContext, SearchDataUtils.parseData(Contast.searchData));
        rlv_list.setAdapter(mLimitListAdapter);
    }

    private void initData() {
        tl_bar.setTitle("监控管理");
        tl_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tl_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        setContentView(R.layout.activity_limit_manager);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        rlv_list = (RecyclerView) findViewById(R.id.rlv_list);

        rlv_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rlv_list.setHasFixedSize(true);
        //添加分割线
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(0xFFe9e9e9);
        rlv_list.addItemDecoration(dividerLine);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 200){
            //表示有更改
            Log.e("TAG", "有更改");
            mLimitListAdapter.updateData(SearchDataUtils.parseData(Contast.searchData));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
