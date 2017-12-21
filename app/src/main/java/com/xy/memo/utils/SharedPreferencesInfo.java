package com.xy.memo.utils;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/*
 * 功能描述：sharepreference工具类
 *
 * 创建标识：xy 2017 11 29
 */
public class SharedPreferencesInfo {
    public static final String FUEL_PREFERENCES = "x_memo_data"; // shareinfo 名称
    public static final String IS_FIRST_USE = "isFirstUse"; // 是否第一次使用
    public static final String IS_VERTICAL_LAYOUT = "isVerticalLayout"; // 是否为纵向布局
    public static final String TAGS = "tags"; // 保存所有的便签夹，json格式string

    /**
     * @Description: 保存String类型的字符串到shareinfo。
     * @param context
     * @param key 存储的名称
     * @param values   要存储的字符串
     * @return: void
     */
    public static void saveTagString(Context context, String key, String values) {
        SharedPreferences preferences = context.getSharedPreferences(
                FUEL_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, values);
        editor.commit();
    }

    /**
     * @Description: 从shareinfo里面获取字符串。
     * @param context
     * @param key 要获取的key名称。
     * @return   
     * @return: String 得到的字符串。获取失败返回null。
     */
    public static String getTagString(Context context, String key) {
        String value = "";
        value = context.getSharedPreferences(FUEL_PREFERENCES, Context.MODE_PRIVATE).getString(key, "");
        return value;
    }
    
    /**
     * @Description: 保存long类型到shareinfo。
     * @param context
     * @param key 存储的名称
     * @param values   要存储long值
     * @return: void
     */
    public static void saveTagLong(Context context, String key, long values) {
        SharedPreferences preferences = context.getSharedPreferences(
                FUEL_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putLong(key, values);
        editor.commit();
    }
    
    /**
     * @Description: 获取long数据
     * @param context
     * @param key 要获取的key
     * @return   
     * @return: long 返回获取到的整形值。获取失败返回0.
     */
    public static long getTagLong(Context context, String key) {
        return context.getSharedPreferences(FUEL_PREFERENCES,
                Context.MODE_PRIVATE).getLong(key, 0l);
    }

    /**
     * @Description: 保存整形数据到shareinfo
     * @param context
     * @param key 要保存的key。
     * @param values   要保存的整形值
     * @return: void
     */
    public static void saveTagInt(Context context, String key, int values) {
        SharedPreferences preferences = context.getSharedPreferences(
                FUEL_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putInt(key, values);
        editor.commit();
    }

    /**
     * @Description: 获取整形数据
     * @param context
     * @param key 要获取的key
     * @return   
     * @return: int 返回获取到的整形值。获取失败返回0.
     */
    public static int getTagInt(Context context, String key) {
        return context.getSharedPreferences(FUEL_PREFERENCES,
                Context.MODE_PRIVATE).getInt(key, 0);
    }

    /**
     * @Description: 保存布尔类型的数据到shareinfo
     * @param context
     * @param key 要保存的key。
     * @param values   要保存的布尔值。
     * @return: void
     */
    public static void saveTagBoolean(Context context, String key, boolean values) {
        SharedPreferences preferences = context.getSharedPreferences(
                FUEL_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(key, values);
        editor.commit();
    }

    /**
     * @Description: 从shareinfo获取布尔值。
     * @param context
     * @param key 要获取的key。
     * @return   
     * @return: boolean 获取到的布尔值。获取失败返回false。
     */
    public static boolean getTagBoolean(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences(FUEL_PREFERENCES,
                Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }
    
    /**
     * @Description: 保存set<String>集合类型数据
     * @param context
     * @param key 要保存的key
     * @param values 要保存的值
     * @return   
     * @return: set<String> 返回获取到的set集合。获取失败返回null.
     */
	public static void setTagSet(Context context,String key,Set<String> values){
    	SharedPreferences preferences = context.getSharedPreferences(
                FUEL_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }
    
    /**
     * @Description: 获取set<String>集合类型数据
     * @param context
     * @param key 要获取的key
     * @return   
     * @return: set<String> 返回获取到的set集合。获取失败返回null.
     */
	public static Set<String> getTagSet(Context context,String key){
    	return context.getSharedPreferences(FUEL_PREFERENCES,
                Context.MODE_PRIVATE).getStringSet(key, null);
    }

	/**
     * @Description: 删除对应Key的数据
     * @param context
     * @param key 要删除的key
     * @return   
     * @return: void
     */
    public static void removeData(Context context,String key){
    	SharedPreferences preferences = context.getSharedPreferences(
                FUEL_PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }
    
}
