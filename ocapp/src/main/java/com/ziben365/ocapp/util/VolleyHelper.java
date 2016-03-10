package com.ziben365.ocapp.util;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;

import org.json.JSONObject;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/15.
 * email  1956766863@qq.com
 */
public class VolleyHelper {


    /*******
     * 请求项目推荐理由
     ******/
    public static void requestReason() {
        VolleyRequest.volleyRequestPost(Link.PROJECT_RECOMMEND_REASON, RequestTag.REQUEST_TAG_REASON,
                new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(VolleyHelper.class.getSimpleName() + "-------------reason---------------" + result);
                        int code = GsonUtil.pareCode(result);
                        if (code == RequestCode.SUCCESS) {
                            JSONObject array = GsonUtil.pareJSONObject(result).optJSONObject("data");
                            SPUtils.put(DemoApplication.applicationContect,
                                    SPKeys.KEY_PROJECT_RECOMMEND_REASON,
                                    array.toString());
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    public static void requestTag() {
        VolleyRequest.volleyRequestPost(Link.PROJECT_TAGS, RequestTag.REQUEST_TAG_TAG,
                new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(VolleyHelper.class.getSimpleName() + "-------------tag---------------" + result);
                        int code = GsonUtil.pareCode(result);
                        if (code == RequestCode.SUCCESS) {
                            JSONObject array = GsonUtil.pareJSONObject(result).optJSONObject("data");
                            SPUtils.put(DemoApplication.applicationContect,
                                    SPKeys.KEY_PROJECT_TAG, array.toString());
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
}
