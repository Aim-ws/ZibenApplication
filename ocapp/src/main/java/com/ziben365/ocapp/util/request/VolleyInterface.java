package com.ziben365.ocapp.util.request;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/16.
 * email  1956766863@qq.com
 */
public abstract class VolleyInterface<T> {
    private Response.Listener<T> listener;
    private Response.ErrorListener errorListener;

    public abstract void onSuccess(T t);

    public abstract void onError(VolleyError error);

    /**
     * 初始化监听器
     * @return
     */
    public Response.Listener<T> loadingListener() {
        listener = new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                onSuccess(response);
            }
        };
        return listener;
    }

    /**
     * 初始化监听器
     * @return
     */
    public Response.ErrorListener errorListener() {
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        };

        return errorListener;
    }
}
