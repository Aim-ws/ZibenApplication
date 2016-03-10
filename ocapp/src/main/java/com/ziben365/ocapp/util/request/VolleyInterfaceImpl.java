package com.ziben365.ocapp.util.request;

import com.android.volley.VolleyError;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/16.
 * email  1956766863@qq.com
 */
public class VolleyInterfaceImpl<T> extends VolleyInterface<T> {
    private VolleyRequestCallBack<T> mCallBack;

    /**
     * 构造方法
     * @param callBack
     *
     */
    public VolleyInterfaceImpl(VolleyRequestCallBack<T> callBack){
        this.mCallBack = callBack;
    }

    @Override
    public void onSuccess(T t) {
        if (null!=mCallBack){
            mCallBack.onSuccess(t);
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (null!=mCallBack){
            mCallBack.onError(error);
        }
    }
}
