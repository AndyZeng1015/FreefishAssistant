package com.zyn.freefishassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;
import com.zyn.freefishassistant.utils.Contast;

import java.io.File;

/**
 * 文件名 SDCardSQLiteOpenHelper
 * SQLite数据库针对SD卡的核心帮助类，该类主要用于直接设置,创建,升级数据库以及打开,关闭数据库资源
 * 版本信息，版本号 v1.0
 * 创建日期 2016/10/27
 * 版权声明 Created by ZengYinan
 */
public abstract class SDCardSQLiteOpenHelper {
    private final Context mContext;
    private final String mName;
    private final SQLiteDatabase.CursorFactory mFactory;
    private final int mNewVersion;

    private SQLiteDatabase mDatabase = null;
    private boolean mIsInitializing = false;


    public SDCardSQLiteOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version) {
        if (version < 1)
            throw new IllegalArgumentException("Version must be >= 1, was "
                    + version);

        mContext = context;
        mName = name;
        mFactory = factory;
        mNewVersion = version;
    }


    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
            return mDatabase;

        }

        if (mIsInitializing) {
            throw new IllegalStateException("getWritableDatabase called recursively");
        }

        boolean success = false;
        SQLiteDatabase db = null;
        try {
            mIsInitializing = true;
            if (mName == null) {
                db = SQLiteDatabase.create(null);
            } else {
                String path = getDatabasePath(mName).getPath();
                db = SQLiteDatabase.openOrCreateDatabase(path, mFactory);
            }

            int version = db.getVersion();
            if (version != mNewVersion) {
                db.beginTransaction();
                try {
                    if (version == 0) {
                        onCreate(db);
                    } else {
                        onUpgrade(db, version, mNewVersion);
                    }
                    db.setVersion(mNewVersion);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            onOpen(db);
            success = true;
            return db;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mIsInitializing = false;
            if (success) {
                if (mDatabase != null) {
                    try {
                        mDatabase.close();
                    } catch (Exception e) {
                    }
                }
                mDatabase = db;
            } else {
                if (db != null)
                    db.close();
            }
        }
        return db;
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        if (mDatabase != null && mDatabase.isOpen()) {
            return mDatabase; // The database is already open for business

        }

        if (mIsInitializing) {
            throw new IllegalStateException(
                    "getReadableDatabase called recursively");
        }

        try {
            return getWritableDatabase();
        } catch (SQLiteException e) {
            if (mName == null)
                throw e; // Can't open a temp database read-only!
        }

        SQLiteDatabase db = null;
        try {
            mIsInitializing = true;
            String path = getDatabasePath(mName).getPath();
            db = SQLiteDatabase.openDatabase(path, mFactory,
                    SQLiteDatabase.OPEN_READWRITE);
            if (db.getVersion() != mNewVersion) {
                throw new SQLiteException(
                        "Can't upgrade read-only database from version "
                                + db.getVersion() + " to " + mNewVersion + ": "
                                + path);
            }

            onOpen(db);
            mDatabase = db;
            return mDatabase;
        } finally {
            mIsInitializing = false;
            if (db != null && db != mDatabase)
                db.close();
        }
    }

    public synchronized void close() {
        if (mIsInitializing)
            throw new IllegalStateException("Closed during initialization");

        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    public File getDatabasePath(String name) {
        String EXTERN_PATH = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) == true) {
            String dbPath = "database/";
            EXTERN_PATH = Contast.BASE_PATH + dbPath;
            File f = new File(EXTERN_PATH);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        return new File(EXTERN_PATH + name);
    }

    public abstract void onCreate(SQLiteDatabase db);

    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion);

    public void onOpen(SQLiteDatabase db) {
    }
}
