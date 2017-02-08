package com.zyn.freefishassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 文件名 DataBaseHelper
 * SQLite数据库的帮助类，该类属于扩展类,主要承担数据库初始化和版本升级使用,其他核心全由核心父类完成
 * 版本信息，版本号 v1.0
 * 创建日期 2016/10/27
 * 版权声明 Created by ZengYinan
 */
public class DataBaseHelper extends SDCardSQLiteOpenHelper{
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE [keywords_map] ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, [content] TEXT);");
        db.execSQL("CREATE TABLE [limit_map]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [keyword_id] INTEGER, [key] NVARCHAR, [value] NVARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
