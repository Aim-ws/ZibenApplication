package com.ziben365.ocapp.util;

import android.util.Log;

import com.ziben365.ocapp.DemoApplication;


/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/10.
 * email  1956766863@qq.com
 * log 输出
 */
public class L {

    public static void e(String text){
        if (DemoApplication.isDebug){
            Log.e(DemoApplication.class.getSimpleName(),text);
        }
    }
    public static void e(String TAG,String text){
        if (DemoApplication.isDebug){
            Log.e(TAG,text);
        }
    }
    public static void i(String text){
        if (DemoApplication.isDebug){
            Log.i(DemoApplication.class.getSimpleName(),text);
        }
    }
    public static void i(String TAG,String text){
        if (DemoApplication.isDebug){
            Log.i(TAG,text);
        }
    }
    public static void d(String text){
        if (DemoApplication.isDebug){
            Log.d(DemoApplication.class.getSimpleName(),text);
        }
    }
    public static void d(String TAG,String text){
        if (DemoApplication.isDebug){
            Log.e(TAG,text);
        }
    }
    public static void w(String text){
        if (DemoApplication.isDebug){
            Log.w(DemoApplication.class.getSimpleName(),text);
        }
    }
    public static void w(String TAG,String text){
        if (DemoApplication.isDebug){
            Log.w(TAG,text);
        }
    }

}
