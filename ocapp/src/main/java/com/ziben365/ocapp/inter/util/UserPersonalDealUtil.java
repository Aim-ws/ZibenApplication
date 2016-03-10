package com.ziben365.ocapp.inter.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ziben365.ocapp.ui.PersonalCenterActivity;
import com.ziben365.ocapp.util.L;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/3/8.
 * email  1956766863@qq.com
 */
public class UserPersonalDealUtil {

    public static void dealUser(Context context, String u_id) {
        L.i("--------u_id---------"+u_id);
        if (TextUtils.isEmpty(u_id)) {
            return;
        }
        Intent intent = new Intent(context, PersonalCenterActivity.class);
        intent.putExtra(PersonalCenterActivity.KEY_PERSONAL_UID, u_id);
        context.startActivity(intent);
    }
}
