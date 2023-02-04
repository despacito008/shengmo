package com.aiwujie.shengmo.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 290243232 on 2016/12/17.
 */
public class JsonObjectUtil {
    public static JSONObject parse(String json){
        JSONObject obj=null;
        try {
            obj=new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
