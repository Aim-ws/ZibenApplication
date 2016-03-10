package com.ziben365.ocapp.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2016/2/26.
 * email  1956766863@qq.com
 */
public class WindowUtil {

    private static SystemBarTintManager tintManager;
    @TargetApi(21)
    public static void initWindow(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintColor(activity.getWindow().getNavigationBarColor());
            tintManager.setStatusBarTintEnabled(true);
        }
    }
}
