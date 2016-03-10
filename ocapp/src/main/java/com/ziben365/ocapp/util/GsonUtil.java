package com.ziben365.ocapp.util;

import com.google.gson.Gson;
import com.ziben365.ocapp.constant.RequestCode;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2015/12/23.
 * email  1956766863@qq.com
 */
public class GsonUtil {

    private static Gson gson;

    /**
     * 获取gson
     * @return
     */
    public static Gson getInstance(){
        if (gson == null){
            synchronized (GsonUtil.class){
                if (gson == null){
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

    /**
     * 获取JSONObject对象
     * @param str
     * @return
     */
    public static JSONObject pareJSONObject(String str){
        JSONObject object = null;
        try {
            object = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static int pareCode(String str){
        try {
            JSONObject object = new JSONObject(str);
            return pareCode(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int pareCode(JSONObject object) {
        int code = object.optInt(RequestCode.KEY_CODE);
        return code;
    }

    public static String pareMsg(String str){
        JSONObject object ;
        try {
            object = new JSONObject(str);
            return pareMsg(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String pareMsg(JSONObject object) {
        String msg = object.optString(RequestCode.KEY_MSG);
        return msg;
    }

}
