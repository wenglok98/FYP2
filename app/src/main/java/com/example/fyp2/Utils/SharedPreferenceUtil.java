package com.example.fyp2.Utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.example.fyp2.BaseApp.AppContext;

import java.util.ArrayList;

public class SharedPreferenceUtil {
    private static final String SHARED_NAME = "save";

    public static void put(String key, Object value) {
        SharedPreferences preference = AppContext.getAppContext().getSharedPreferences(SHARED_NAME, 0);
        Editor editor = preference.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.commit();
    }

    public static Object get(String key, Object defaultValue) {
        SharedPreferences preference = AppContext.getAppContext().getSharedPreferences(SHARED_NAME, 0);
        if (defaultValue instanceof String) {
            return preference.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return preference.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Float) {
            return preference.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return preference.getBoolean(key, (Boolean) defaultValue);
        } else {
//            return preference.get(key, (Object) defaultValue);
            throw new RuntimeException("错误类型");
        }
    }

    public static void setArrayPrefs(String ArrayName, ArrayList<String>array, Context mContext){
        SharedPreferences preferences = AppContext.getAppContext().getSharedPreferences(SHARED_NAME,0);
        Editor editor = preferences.edit();
        editor.putInt(ArrayName + "_size", array.size());
        for (int i=0;i<array.size();i++)
            editor.putString(ArrayName + "_" + i ,array.get(i));
        editor.apply();
    }

    public static ArrayList<String> getArrayPrefs(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        ArrayList<String> array = new ArrayList<>(size);
        for(int i=0;i<size;i++)
            array.add(prefs.getString(arrayName + "_" + i, null));
        return array;
    }

    public static void clear() {
        SharedPreferences preference = AppContext.getAppContext().getSharedPreferences(SHARED_NAME, 0);
        Editor editor = preference.edit();
        editor.clear().apply();
    }

}
