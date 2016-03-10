package com.ziben365.ocapp.inter.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.model.ProjectDetail;
import com.ziben365.ocapp.ui.ProjectDetailsActivity;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.view.AppProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/2/25.
 * email  1956766863@qq.com
 */
public class ProjectItemClickUtil {
    private static AppProgressDialog progressDialog;

    public static void itemClickDetails(final Activity context, String p_id) {
        progressDialog = new AppProgressDialog(context);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
        HashMap<String, String> param = new HashMap<>();
        param.put("id", p_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_DETAILS_PROJECT, RequestTag.REQUEST_TAG_DETAILS_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                        String result = o.toString();
                        L.i("---------------project details------" + result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONObject object = GsonUtil.pareJSONObject(result).optJSONObject(RequestCode.KEY_ARRAY);
                                ProjectDetail pd = GsonUtil.getInstance().fromJson(object.toString(), ProjectDetail.class);
                                Intent intent = new Intent(context, ProjectDetailsActivity.class);
                                intent.putExtra("obj", pd);
                                context.startActivity(intent);
                            }
                        } else {
                            Toast.makeText(context, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    public static void itemClickDetails1(final Context context, String p_id, final boolean show) {
        HashMap<String, String> param = new HashMap<>();
        param.put("id", p_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_DETAILS_PROJECT, RequestTag.REQUEST_TAG_DETAILS_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i("---------------project details------" + result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONObject object = GsonUtil.pareJSONObject(result).optJSONObject(RequestCode.KEY_ARRAY);
                                ProjectDetail pd = GsonUtil.getInstance().fromJson(object.toString(), ProjectDetail.class);
                                Intent intent = new Intent(context, ProjectDetailsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("obj", pd);
                                context.startActivity(intent);
                            }
                        } else {
                            Toast.makeText(context, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    /**
     *
     * @param context  上下文环境
     * @param p_id      项目id
     * @param isBroadcast     是否发送广播更新
     */
    public static void itemClickDetails(final Activity context, String p_id, final boolean isBroadcast) {
        final AppProgressDialog progressDialog = new AppProgressDialog(context);
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
        HashMap<String, String> param = new HashMap<>();
        param.put("id", p_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_DETAILS_PROJECT, RequestTag.REQUEST_TAG_DETAILS_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                        String result = o.toString();
                        L.i("---------------project details------" + result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONObject object = GsonUtil.pareJSONObject(result).optJSONObject(RequestCode.KEY_ARRAY);
                                ProjectDetail pd = GsonUtil.getInstance().fromJson(object.toString(), ProjectDetail.class);
                                Intent intent = new Intent(context, ProjectDetailsActivity.class);
                                intent.putExtra("obj", pd);
                                intent.putExtra(NotifyAction.KEY_LIKE_BROADCAST,isBroadcast);
                                context.startActivity(intent);
                            }
                        } else {
                            Toast.makeText(context, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    /**
     * 关闭dialog
     */
    public static void dismiss(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
