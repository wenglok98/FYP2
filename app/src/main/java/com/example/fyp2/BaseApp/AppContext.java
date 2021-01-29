package com.example.fyp2.BaseApp;

import android.content.Context;


import com.example.fyp2.Utils.SharedPreferenceUtil;

import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppContext {

    private static Map<String, Object> hashMap = null;
    private static Object object = null;
    public static Context context;
    public static int CID = 0;
    public static String APPKEY = "1c369c73b3efc";
    public static String APPSECRET = "1eebb5f4806a89391ac56dfcd051830b";
    //    public static String XS_API_KEY = "rasaggg7dd673372caad86d542ea46201276";
    public static String XS_API_KEY = "purplecanedemo";


    //public static String api = SharedPreferenceUtil.get("api_key","").toString();
    //public static String XS_API_KEY2 = api ; //generic;


    private static final String TAG = "AppContext";
    private List<Map<String, Object>> data;

    public static Map<String, Object> getHashMap() {
        return hashMap;
    }

    public static void setHashMap(Map<String, Object> map) {
        hashMap = map;
    }

    public static Context getAppContext() {
        return context;
    }

    public static Object getObject() {
        return object;
    }

    public static void setObject(Object object) {
        AppContext.object = object;
    }
}
