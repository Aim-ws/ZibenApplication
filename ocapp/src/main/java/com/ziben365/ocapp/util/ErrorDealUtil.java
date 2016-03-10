package com.ziben365.ocapp.util;

import android.content.Context;
import android.content.Intent;

import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.ui.LoginActivity;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/3/4.
 * email  1956766863@qq.com
 */
public class ErrorDealUtil {

    private static final int ERROR_CODE = 2;

    public static void dealError(Context context, int code) {
        if (code == ERROR_CODE) {
            SPUtils.removeUser(context);
            SPUtils.put(context, SPKeys.KEY_USER_TOKEN_TAG,"");
            context.sendBroadcast(new Intent(NotifyAction.ACTION_UPDATE_USER_INFO));
            context.sendBroadcast(new Intent(NotifyAction.ACTION_UPDATE_USER_GOLD));
            JPushInterface.setAliasAndTags(context, "", null, new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {

                }
            });
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }
}
