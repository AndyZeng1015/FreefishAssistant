package com.zyn.freefishassistant.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.zyn.freefishassistant.beans.ConfigBean;
import com.zyn.freefishassistant.beans.KeyMapBean;
import com.zyn.freefishassistant.beans.LimitMapBean;
import com.zyn.freefishassistant.db.DBManager;
import com.zyn.freefishassistant.db.SQLiteTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名 ConfigUtils
 * 描述
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/8
 * 版权声明 Created by ZengYinan
 */

public class ConfigUtils {
    /**
     * 读取搜索配置信息
     * @param mContext
     * @return
     */
    public static List<ConfigBean> getSearchConfig(Context mContext){

        List<ConfigBean> configBeanList = new ArrayList<ConfigBean>();
        List<KeyMapBean> keywords_list = new ArrayList<KeyMapBean>();
        List<LimitMapBean> limit_list = new ArrayList<LimitMapBean>();

        DBManager dbManager = DBManager.getInstance(mContext, "fishAssistant");//fishAssistant数据库名字
        SQLiteTemplate template = SQLiteTemplate.getInstance(dbManager, false);
        //获取到搜索关键字列表
        keywords_list = template.queryForList(
                        new SQLiteTemplate.RowMapper<KeyMapBean>() {

                            @Override
                            public KeyMapBean mapRow(Cursor cursor, int index) {
                                KeyMapBean keyMapBean = new KeyMapBean();
                                keyMapBean.setId(cursor.getString(cursor.getColumnIndex("_id")));
                                keyMapBean.setKeyword(cursor.getString(cursor.getColumnIndex("content")));
                                return keyMapBean;
                            }
                        },
                        "select _id,content from keywords_map",
                        null);

        for (KeyMapBean k : keywords_list) {
            //一个关键词的属性
            limit_list = template.queryForList(
                    new SQLiteTemplate.RowMapper<LimitMapBean>() {

                        @Override
                        public LimitMapBean mapRow(Cursor cursor, int index) {
                            LimitMapBean limitMapBean = new LimitMapBean();
                            limitMapBean.setId(cursor.getString(cursor.getColumnIndex("_id")));
                            limitMapBean.setKeyword_id(cursor.getString(cursor.getColumnIndex("keyword_id")));
                            limitMapBean.setKey(cursor.getString(cursor.getColumnIndex("key")));
                            limitMapBean.setValue(cursor.getString(cursor.getColumnIndex("value")));
                            return limitMapBean;
                        }
                    },
                    "select _id,keyword_id,key,value from limit_map where keyword_id=?", new String[]{k.getId()});
            ConfigBean c = new ConfigBean();
            c.setKeyMapBean(k);
            c.setLimitMapBeen(limit_list);
            configBeanList.add(c);
        }
        return configBeanList;
    }

    /**
     * 存储搜索配置信息
     * @param mContext
     * @param configBean
     */
    public static void saveSearchConfig(Context mContext, ConfigBean configBean){
        DBManager dbManager = DBManager.getInstance(mContext, "fishAssistant");
        SQLiteTemplate template = SQLiteTemplate.getInstance(dbManager, false);

        //先检查是否存在
        boolean isExit = template.isExistsByField("keywords_map", "content", configBean.getKeyMapBean().getKeyword());

        if(isExit){
            ToastUtil.showToastOnThread((Activity) mContext, "该关键字已存在！");
            return;
        }

        //保存关键字
        ContentValues content = new ContentValues();
        content.put("content", configBean.getKeyMapBean().getKeyword());
        template.insert("keywords_map", content);

        //获取到关键字的id
        String keyword_id = template.queryForObject(new SQLiteTemplate.RowMapper<String>(){

            @Override
            public String mapRow(Cursor cursor, int index) {
                return cursor.getString(cursor.getColumnIndex("_id"));
            }
        }, "select _id from keywords_map where content=?", new String[]{configBean.getKeyMapBean().getKeyword()});

        //保存关键字对应的限制条件
        for(LimitMapBean limitMapBean : configBean.getLimitMapBeen()){
            ContentValues content_limit = new ContentValues();
            content_limit.put("keyword_id", keyword_id);
            content_limit.put("key", limitMapBean.getKey());
            content_limit.put("value", limitMapBean.getValue());
            template.insert("limit_map", content_limit);
        }
    }


    /**
     * 删除搜索配置信息
     * @param mContext
     */
    public static void deleteSearchConfigById(Context mContext, String id){
        DBManager dbManager = DBManager.getInstance(mContext, "fishAssistant");
        SQLiteTemplate template = SQLiteTemplate.getInstance(dbManager, false);

        //删除限制
        template.deleteByField("limit_map", "keyword_id", id);
        //删除关键词
        template.deleteById("keywords_map", id);
    }

    /**
     * 修改搜索配置信息
     * @param mContext
     * @param configBean
     */
    public static void updateSearchConfigByOldKey(Context mContext, ConfigBean configBean){
        DBManager dbManager = DBManager.getInstance(mContext, "fishAssistant");
        SQLiteTemplate template = SQLiteTemplate.getInstance(dbManager, false);

        //获取到关键字的id
        String keyword_id = configBean.getKeyMapBean().getId();

        //先删除限制
        template.deleteByField("limit_map", "keyword_id", keyword_id);
        //再重新添加
        for(LimitMapBean limitMapBean : configBean.getLimitMapBeen()){
            ContentValues content_limit = new ContentValues();
            content_limit.put("keyword_id", keyword_id);
            content_limit.put("key", limitMapBean.getKey());
            content_limit.put("value", limitMapBean.getValue());
            template.insert("limit_map", content_limit);
        }

        //再修改关键字
        ContentValues keywordMapContent = new ContentValues();
        keywordMapContent.put("content", configBean.getKeyMapBean().getKeyword());
        template.updateById("keywords_map", keyword_id, keywordMapContent);
    }
}
