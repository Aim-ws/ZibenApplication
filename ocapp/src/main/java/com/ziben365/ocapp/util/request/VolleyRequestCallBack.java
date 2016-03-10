package com.ziben365.ocapp.util.request;

import com.android.volley.VolleyError;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/16.
 * email  1956766863@qq.com
 */
public interface VolleyRequestCallBack<T> {

    void onSuccess(T t);

    void onError(VolleyError volleyError);



}
