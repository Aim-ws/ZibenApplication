package com.ziben365.ocapp.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Stack;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/2/19.
 * email  1956766863@qq.com
 */
public class ActivityManagerUtil {
    private static ActivityManagerUtil instance;

    private Stack<Activity> activityStack;

    private ActivityManagerUtil(){
        activityStack = new Stack<>();
    }

    public static ActivityManagerUtil getInstance(){
        if (instance == null){
            synchronized (ActivityManagerUtil.class){
                if (instance == null){
                    instance = new ActivityManagerUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 把一个activity压入栈中
     * @param act
     */
    public void pushActivity(Activity act){
        if (activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.push(act);
    }

    /**
     * 把一个activity弹出栈
     * @param act
     */
    public void popActivity(Activity act){
        if (activityStack != null && activityStack.size() > 0) {
            if (act != null) {
                act.finish();
                activityStack.remove(act);
                act = null;
            }
        }
    }

    /**
     * 获取栈顶的activity，先进后出原则
     * @return
     */
    public Activity getLastActivity(){
        return activityStack.lastElement();
    }

    /**
     * 关闭所有的activity
     */
    public void finishAllActivity(){
        if (activityStack!=null){
            while (activityStack.size() > 0){
                Activity act = getLastActivity();
                if (act == null) break;
                popActivity(act);
            }
        }
    }
    /**
     * 关闭所有的activity保留当前的activity
     */
    public void finishAllActivity(Activity targetAct){
        if (activityStack!=null){
            while (activityStack.size() > 0){
                Activity act = getLastActivity();
                if (act == null) continue;
                if (targetAct == act) continue;
                popActivity(act);
            }
        }
    }

    /**
     * 判断应用是否已经启动
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }


}
