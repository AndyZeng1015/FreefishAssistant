package com.zyn.freefishassistant.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.zyn.freefishassistant.R;
import com.zyn.freefishassistant.base.BaseActivity;
import com.zyn.freefishassistant.base.MyApplication;
import com.zyn.freefishassistant.beans.ConfigBean;
import com.zyn.freefishassistant.beans.KeyMapBean;
import com.zyn.freefishassistant.beans.LimitMapBean;
import com.zyn.freefishassistant.utils.ConfigUtils;
import com.zyn.freefishassistant.utils.Contast;
import com.zyn.freefishassistant.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文件名 AddLimitActivity
 * 描述
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/10
 * 版权声明 Created by ZengYinan
 */

public class AddLimitActivity extends BaseActivity {

    private Toolbar tl_bar;

    private TextInputEditText tiet_keyword;
    private TextInputEditText tiet_low_price;
    private TextInputEditText tiet_high_price;
    private SwitchCompat sc_is_show;
    private Button btn_save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存
                ConfigBean configBean = new ConfigBean();

                KeyMapBean keyMapBean = new KeyMapBean();
                keyMapBean.setKeyword(tiet_keyword.getText().toString().trim());
                configBean.setKeyMapBean(keyMapBean);

                List<LimitMapBean> limitMapBeanList = new ArrayList<LimitMapBean>();
                //最低价
                if(!tiet_low_price.getText().toString().equals("") && !tiet_low_price.getText().toString().equals("0")){
                    LimitMapBean low_price = new LimitMapBean();
                    low_price.setKey("start");
                    low_price.setValue(tiet_low_price.getText().toString().trim());
                    limitMapBeanList.add(low_price);
                }
                //最高价
                if(!tiet_high_price.getText().toString().equals("") && !tiet_high_price.getText().toString().equals("0")){
                    LimitMapBean low_price = new LimitMapBean();
                    low_price.setKey("end");
                    low_price.setValue(tiet_high_price.getText().toString().trim());
                    limitMapBeanList.add(low_price);
                }
                //是否显示
                LimitMapBean is_show = new LimitMapBean();
                is_show.setKey("is_Show");
                if(sc_is_show.isChecked()){
                    //显示
                    is_show.setValue("显示");
                }else{
                    //不显示
                    is_show.setValue("不显示");
                }
                limitMapBeanList.add(is_show);

                LimitMapBean spm = new LimitMapBean();
                spm.setKey("spm");
                spm.setValue("2007.1000337.6.2.nEgtu7");
                limitMapBeanList.add(spm);

                LimitMapBean list_type = new LimitMapBean();
                list_type.setKey("ist");
                list_type.setValue("0");
                limitMapBeanList.add(list_type);

                configBean.setLimitMapBeen(limitMapBeanList);

                final ConfigBean temp = configBean;

                MyApplication.getInstance().execRunnable(new Runnable() {
                    @Override
                    public void run() {
                        //保存
                        ConfigUtils.saveSearchConfig(mContext, temp);
                        //重新加载
                        Contast.searchData.clear();
                        List<ConfigBean> configBeanList = ConfigUtils.getSearchConfig(mContext);
                        for(ConfigBean configBean : configBeanList){
                            Contast.searchData.add(configBean);
                        }
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(mContext, "保存成功！");
                                AddLimitActivity.this.finish();
                            }
                        });
                    }
                });

            }
        });
    }

    private void initData() {
        tl_bar.setTitle("添加监控");
        tl_bar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tl_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        setContentView(R.layout.activity_limit_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        tiet_keyword = (TextInputEditText) findViewById(R.id.tiet_keyword);
        tiet_low_price = (TextInputEditText) findViewById(R.id.tiet_low_price);
        tiet_high_price = (TextInputEditText) findViewById(R.id.tiet_high_price);
        sc_is_show = (SwitchCompat) findViewById(R.id.sc_is_show);
        btn_save = (Button) findViewById(R.id.btn_save);
    }
}
