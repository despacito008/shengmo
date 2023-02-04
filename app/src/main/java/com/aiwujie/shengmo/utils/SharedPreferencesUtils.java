package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.aiwujie.shengmo.application.MyApp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 290243232 on 2016/12/19.
 */
public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "personData";

    public static final String COMMENT_SORT = "comment_sort_type";
    public static final String HOT_SORT = "hot_sort_type";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */

    public static void setParam(Context context , String key, Object object){
        if (object == null || object.getClass() == null) {
            return;
        }
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }
        editor.apply();
    }




    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        if (context == null) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }

    public static  void clearParam(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            if (sp.contains(key)){
                sp.edit().remove(key).commit();
            }
            return;

    }

    public static String geParam(Context context, String key, String defaultString) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

            return sp.getString(key, defaultString);


    }

    /**
     * 用于保存集合
     *
     * @param map map数据
     * @return 保存结果
     */
    public static <K, V> boolean putHashMapData(Map<K, V> map) {
        boolean result;
        SharedPreferences sp = MyApp.getInstance().getSharedPreferences("SIGN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString("recognizeUserMap", json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 用于取出集合
     *
     * @return HashMap
     */
    public static <V> HashMap<String, V> getHashMapData(Class<V> clsV) {
        SharedPreferences sp = MyApp.getInstance().getSharedPreferences("SIGN", Context.MODE_PRIVATE);
        String json = sp.getString("recognizeUserMap", "");
        HashMap<String, V> map = new HashMap<>();
        Gson gson = new Gson();
        /*JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String entryKey = entry.getKey();
            JsonObject value = (JsonObject) entry.getValue();
            map.put(entryKey, gson.fromJson(value, clsV));
        }*/
        JsonParser jsonParser = new JsonParser();
        JsonObject obj= jsonParser.parse(json).getAsJsonObject();


        map.put("recognizeUserMap", gson.fromJson(obj, clsV));

        LogUtil.d("getHashMapData-------------------" + obj.toString());

        return map;
    }


     /* 保存List
     * @param tag
     * @param datalist
     */
    public static <T> void addDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        SharedPreferences sp = MyApp.instance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static <T> List<T> getDataList(String tag) {
        List<T> datalist=new ArrayList<T>();
        SharedPreferences sp = MyApp.instance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;

    }


}
