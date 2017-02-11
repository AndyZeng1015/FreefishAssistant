package com.zyn.freefishassistant.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    public static SharedPreferences getSharedPreferenced(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    /**
     * 保存boolean型到SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String spName, String key, boolean value) {
        getSharedPreferenced(context, spName).edit().putBoolean(key, value).commit();
    }

    /**
     * 从SharedPreferences读取boolean型
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String spName, String key, boolean defValue) {
        return getSharedPreferenced(context, spName).getBoolean(key, defValue);
    }

    /**
     * 保存String型到SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String spName, String key, String value) {
        getSharedPreferenced(context, spName).edit().putString(key, value).commit();
    }

    /**
     * 从SharedPreferebces中获取String
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String spName, String key, String defValue) {
        return getSharedPreferenced(context, spName).getString(key, defValue);
    }

    public static void saveInt(Context context, String spName, String key, int value) {
        getSharedPreferenced(context, spName).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String spName, String key, int defValue) {
        return getSharedPreferenced(context, spName).getInt(key, defValue);
    }

    public static void saveLong(Context context, String spName, String key, long value) {
        getSharedPreferenced(context, spName).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String spName, String key, long defValue) {
        return getSharedPreferenced(context, spName).getLong(key, defValue);
    }
}
