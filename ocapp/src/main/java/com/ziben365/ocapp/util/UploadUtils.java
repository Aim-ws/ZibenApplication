package com.ziben365.ocapp.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.ziben365.ocapp.DemoApplication;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/18.
 * email  1956766863@qq.com
 */
public class UploadUtils {

    private UploadManager mUploadManager = new UploadManager();
    private List<String> mImages;
    private static UploadUtils instance;

    public static UploadUtils getInstance() {
        if (instance == null) {
            synchronized (UploadUtils.class) {
                if (instance == null) {
                    instance = new UploadUtils();
                }
            }
        }
        return instance;
    }

    private UploadUtils() {

    }

    public void clearArray(){
        mImages = new ArrayList<>();
        mImages.clear();
    }

    public List<String> getmImages(){
        return mImages;
    }


    /**
     * 生成远程文件路径（全局唯一）
     *
     * @return
     */
    public String getFileUrlUUID() {
        String filePath = "upload/userlogo" + getFileName() + ".jpg";
        return filePath;
    }
    /**
     * 生成远程文件路径（全局唯一）
     * upload/userlogo
     * upload/unicorn/banner
     * upload/unicorn/pic
     *
     * @param bucketLocation
     *
     * @return
     */
    public String getFileUrlUUID(String bucketLocation) {
        String filePath = bucketLocation + getFileName() + ".jpg";
        return filePath;
    }

    private String getFileName() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = format.format(date);
        int random = new Random().nextInt(10000);
        str = str + random;
        L.i("时间格式:" + str);
        return str;
    }

    public void uploadImage(String filePath, String token, final OnUploadImageListener listener) {
        String fileUrlUUID = getFileUrlUUID();
        mImages.add(fileUrlUUID);
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(DemoApplication.applicationContect, "token有误!", Toast.LENGTH_SHORT).show();
            return;
        }
        mUploadManager.put(filePath, fileUrlUUID, token, new UpCompletionHandler() {
            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                L.i("responseInfo:"+responseInfo.toString());
                L.i("jsonObject:"+jsonObject.toString());
                if (responseInfo!=null){
                    if (responseInfo.statusCode == 200){
                        if (null!=listener){
                            listener.onSuccess(jsonObject.optString("key"));
                        }
                    }else{
                        if (null!=listener){
                            listener.onError(responseInfo.statusCode,responseInfo.error);
                        }
                    }
                }

            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String s, double v) {
                L.i("progress:"+v);
                if (null!=listener){
                    listener.onProgress(s,v * 100);
                }
            }
        }, null));


    }


    public interface OnUploadImageListener {
        void onSuccess(String fileUrl);

        void onError(int errorCode, String msg);

        void onProgress(String s,double progress);
    }


}
