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
import com.ziben365.ocapp.model.UserMessage;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;

import java.util.HashMap;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/3/8.
 * email  1956766863@qq.com
 */
public class UserMessageDealUtil {
    public static final String ACTION_PROJECT = "project";
    public static final String ACTION_MESSAGE = "message";

    /**
     * 处理用户消息
     *
     * @param context 上下文环境
     * @param message message对象
     * @param change  是否要改变消息状态
     */
    public static void dealMessage(Context context, UserMessage message, boolean change) {
        if (null == message) return;
        if (TextUtils.isEmpty(message.action)) return;
        if (TextUtils.isEmpty(message.action_value)) return;
        if (ACTION_PROJECT.equals(message.action)) {
            ProjectItemClickUtil.itemClickDetails((Activity) context, message.action_value);
        }
        if (ACTION_MESSAGE.equals(message.action)) {

        }
        if (change) {
            changeMessageStatus(context, message);
        }
    }


    /**
     * @param context
     * @param action
     * @param action_value
     */
    public static void dealMessage(Context context, String action, String action_value) {
        if (TextUtils.isEmpty(action)) return;
        if (TextUtils.isEmpty(action_value)) return;
        if (ACTION_PROJECT.equals(action)) {
            ProjectItemClickUtil.itemClickDetails((Activity) context, action_value);
        }
        if (ACTION_MESSAGE.equals(action)) {

        }

    }

    /**
     * 改变消息状态
     *
     * @param context 上下文环境
     * @param message message对象
     */
    private static void changeMessageStatus(final Context context, UserMessage message) {
        String token = SPUtils.getToken(context);
        if (TextUtils.isEmpty(token)) return;
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("msg_id", message.id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_MSG_INFO,
                RequestTag.REQUEST_TAG_USER_MSG_INFO, param,
                new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                Intent intent = new Intent(NotifyAction.ACTION_USER_MESSAGE_CHANGE);
                                context.sendBroadcast(intent);
                            } else {
                                Toast.makeText(context, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
}
