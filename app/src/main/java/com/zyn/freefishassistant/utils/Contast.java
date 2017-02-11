package com.zyn.freefishassistant.utils;

import com.zyn.freefishassistant.beans.ConfigBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名 Contast
 * 存放常量
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/8
 * 版权声明 Created by ZengYinan
 */

public class Contast {
    public static final String BASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.zyn.freefishAssistant/";//存放位置的基础路径
    public static List<ConfigBean> searchData = new ArrayList<ConfigBean>();//搜索配置数据
}
