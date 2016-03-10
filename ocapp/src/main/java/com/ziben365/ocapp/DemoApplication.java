package com.ziben365.ocapp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ziben365.ocapp.cache.ACache;
import com.ziben365.ocapp.util.L;

import cn.jpush.android.api.JPushInterface;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/10.
 * email  1956766863@qq.com
 */
public class DemoApplication extends Application {
    /****
     *   七牛图片尺寸
     *
     * w100  宽 100 ，高按比例伸缩
     * w300   宽 300 ，高按比例伸缩
     * w480   宽 480 ，高按比例伸缩
     * w50  宽 50 ，高按比例伸缩
     * w600  宽 600 ，高按比例伸缩
     * w800  宽 800 ，高按比例伸缩
     * wh100  宽 100 ，高100
     * wh300   宽 300 ，高300
     * wh800   宽 800 ，高800
     *
     *
     */
    public static boolean isDebug = true;

    private static final String VOLLEY_TAG = "volleyPatters";

    private static RequestQueue mRequestQueue;

    public static float sScale;
    public static int sWidthDp;
    public static int sWidthPix;
    public static int sHeightPix;

    private static DemoApplication instance;

    public static Context applicationContect;

    private ACache mAcache;




    /**
     * 单例
     *
     * @return
     */
    public static DemoApplication getInstance(){
        if (instance == null){
            synchronized (DemoApplication.class){
                if (instance == null){
                    instance = new DemoApplication();
                }
            }
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        initImageLoader(getApplicationContext());

        applicationContect = getApplicationContext();

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mAcache = ACache.get(applicationContect);

        sScale = getResources().getDisplayMetrics().density;
        sWidthPix = getResources().getDisplayMetrics().widthPixels;
        sHeightPix = getResources().getDisplayMetrics().heightPixels;
        sWidthDp = (int) (sWidthPix / sScale);



    }

    /**
     * 检查是否接受极光推送
     */
    /*private void checkJpushReceiver() {
        boolean receiver = (boolean) SPUtils.get(applicationContect, SPKeys.KEY_USER_JPUSH_RECEIVER_TAG,true);
        if (JPushInterface.isPushStopped(applicationContect)){     //服务已停止
            if (receiver) JPushInterface.resumePush(applicationContect);     //
        }

    }*/


    /**
     * 开启debug模式
     *
     * @param debug
     */
    public static void openDebug(boolean debug) {
        DemoApplication.isDebug = debug;
    }


    /**
     * 获取request的队列实例
     *
     * @return
     */
    public static RequestQueue getRequestQueue() {

        return mRequestQueue;
    }

    /**
     * 将request加入requestQueue队列中
     *
     * @param request
     * @param TAG
     * @param <T>
     */
    public static <T> void addToRequestQueue(Request<T> request, String TAG) {
        request.setTag(TextUtils.isEmpty(TAG) ? VOLLEY_TAG : TAG);
        L.i(VOLLEY_TAG, "" + request.getUrl());
        getRequestQueue().add(request);
    }

    /**
     * 将request加入requestQueue队列中
     *
     * @param request
     * @param <T>
     */
    public static <T> void addToRequestQueue(Request<T> request) {
        request.setTag(VOLLEY_TAG);
        L.i(VOLLEY_TAG, "" + request.getUrl());
        getRequestQueue().add(request);
    }


    /**
     * 取消相应请求
     *
     * @param tag
     */
    public static void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * 获取缓存对象
     * @return
     */
    public ACache getAcache(){
        if (mAcache == null){
            mAcache = ACache.get(applicationContect);
        }
        return mAcache;
    }

    public static void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder() //
                .showImageOnLoading(R.mipmap.defaults_pic) //
                .showImageOnFail(R.mipmap.defaults_pic) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)//
                .defaultDisplayImageOptions(options)//
                .threadPriority(Thread.NORM_PRIORITY - 2)//
                .denyCacheImageMultipleSizesInMemory()//
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//
                .diskCacheSize(50 * 1024 * 1024) // 50Mb
                .diskCacheFileCount(300)//
                .tasksProcessingOrder(QueueProcessingType.LIFO) // .writeDebugLogs()
                .diskCacheExtraOptions(sWidthPix / 3, sWidthPix / 3, null)//
                .build();
        ImageLoader.getInstance().init(config);
    }


}
