package com.ziben365.ocapp.util.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ziben365.ocapp.DemoApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/16.
 * email  1956766863@qq.com
 */
public class VolleyRequest {

    /**
     *  volley  get 方法  无参数
     * @param url      url地址
     * @param tag      标签
     * @param volleyInterface     监听回调
     */
    public static void volleyRequestGet(String url, String tag, VolleyInterface volleyInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                volleyInterface.loadingListener(),volleyInterface.errorListener());
        DemoApplication.addToRequestQueue(stringRequest,tag);
        DemoApplication.getRequestQueue().start();

    }

    /**
     *  volley get 方法 map参数
     * @param url      url地址
     * @param tag       标签
     * @param params     map参数
     * @param volleyInterface   监听回调
     */
    public static void volleyRequestGet(String url, String tag, final HashMap<String,String> params, VolleyInterface volleyInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                volleyInterface.loadingListener(),volleyInterface.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        DemoApplication.addToRequestQueue(stringRequest,tag);
        DemoApplication.getRequestQueue().start();
    }

    /**
     *  volley get 方法 map参数
     * @param context     上下文环境
     * @param url     url地址
     * @param tag     标签
     * @param params   map参数
     * @param volleyInterface     监听回调
     */
    public static void volleyRequestGet(Context context, String url, String tag, final HashMap<String,String> params, VolleyInterface volleyInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                volleyInterface.loadingListener(),volleyInterface.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        DemoApplication.addToRequestQueue(stringRequest,tag);
        DemoApplication.getRequestQueue().start();
    }

    /**
     *  volley  post 方法
     * @param url    url地址
     * @param tag    标签
     * @param paramMap    参数map
     * @param volleyInterface     监听回调
     */
    public static void volleyRequestPost(String url, String tag, final Map<String, String> paramMap, int paramFlag, VolleyInterfaceImpl volleyInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                volleyInterface.loadingListener(),volleyInterface.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paramMap;
            }
        };
        DemoApplication.addToRequestQueue(stringRequest,tag);
        DemoApplication.getRequestQueue().start();
    }

    /**
     *  volley  post 方法
     * @param url    url地址
     * @param tag    标签
     * @param paramMap    参数map
     * @param volleyInterface     监听回调
     */
    public static void volleyRequestPost(String url, String tag, final Map<String,String> paramMap, VolleyInterface volleyInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                volleyInterface.loadingListener(),volleyInterface.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paramMap;
            }
        };
        DemoApplication.addToRequestQueue(stringRequest,tag);
        DemoApplication.getRequestQueue().start();
    }
    /**
     *  volley  post 方法  无参数
     * @param url    url地址
     * @param tag    标签
     * @param volleyInterface     监听回调
     */
    public static void volleyRequestPost(String url, String tag,VolleyInterface volleyInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                volleyInterface.loadingListener(),volleyInterface.errorListener());
        DemoApplication.addToRequestQueue(stringRequest,tag);
        DemoApplication.getRequestQueue().start();
    }

    /**
     *
     * @param url
     * @param tag
     * @param volleyInterface
     */
    public static void volleyStream(String url, String tag,VolleyInterface volleyInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        InputStreamRequest isRequest = new InputStreamRequest(Request.Method.POST,url,
                volleyInterface.loadingListener(),volleyInterface.errorListener());
        DemoApplication.addToRequestQueue(isRequest,tag);
        DemoApplication.getRequestQueue().start();
    }

    /**
     * 不带参数的msgpack数据请求
     * @param url
     * @param tag
     * @param volleyInterface
     */
    public static void vollyJsonMsgpack(String url, String tag,VolleyInterface volleyInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        JsonStringRequest jsonStringRequest = new JsonStringRequest(Request.Method.POST,url,
                volleyInterface.loadingListener(),volleyInterface.errorListener());
        DemoApplication.addToRequestQueue(jsonStringRequest,tag);
        DemoApplication.getRequestQueue().start();
    }


    /**
     * 带参数的msgpack数据请求
     * @param url
     * @param tag
     * @param volleyInterface
     */
    public static void vollyJsonMsgpack(String url, String tag, final Map<String,String> params, VolleyInterface volleyInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        JsonStringRequest jsonStringRequest = new JsonStringRequest(Request.Method.POST,url,
                volleyInterface.loadingListener(),volleyInterface.errorListener())
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        DemoApplication.addToRequestQueue(jsonStringRequest,tag);
        DemoApplication.getRequestQueue().start();
    }

    public static void volleyJSONObjectMsgpack(String url, String tag, final Map<String,String> params, VolleyJSONObjectInterface volleyJSONObjectInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        JSONObjectRequest objectRequest = new JSONObjectRequest(Request.Method.POST,url,
                volleyJSONObjectInterface.loadingListener(),
                volleyJSONObjectInterface.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        DemoApplication.addToRequestQueue(objectRequest,tag);
        DemoApplication.getRequestQueue().start();
    }

    public static void volleyJSONObjectMsgpack(String url, String tag, VolleyJSONObjectInterface volleyJSONObjectInterface){
        DemoApplication.getRequestQueue().cancelAll(tag);
        JSONObjectRequest objectRequest = new JSONObjectRequest(Request.Method.POST,url,
                volleyJSONObjectInterface.loadingListener(),
                volleyJSONObjectInterface.errorListener());
        DemoApplication.addToRequestQueue(objectRequest,tag);
        DemoApplication.getRequestQueue().start();
    }

}
