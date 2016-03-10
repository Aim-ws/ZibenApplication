package com.ziben365.ocapp.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2015/12/23.
 * email  1956766863@qq.com
 * 常用工具类
 */
public class StringUtil {

    /**
     * 移动号段：
     * 134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188
     * 联通号段：
     * 130 131 132 145 155 156 175 176 185 186
     * 电信号段：
     * 133 153 177 180 181 189
     * 虚拟运营商:
     * 170
     *
     * 第一位是1，第二位是3，4，5，7，8，其他的是0~9任意数字
     * @param mobileNum
     * @return
     */
    public static boolean isMobile(String mobileNum){
        String regex = "[1][34578]\\d{9}";  //手机号正则式
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobileNum);
        return m.matches();
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static String formatTime(long milliseconds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date(milliseconds * 1000L));
        return time;
    }
    public static String formatTime(long milliseconds,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String time = sdf.format(new Date(milliseconds * 1000L));
        return time;
    }
    public static String formatSystemTime(long milliseconds,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String time = sdf.format(new Date(milliseconds));
        return time;
    }

    /**
     * 获取当前版本号
     * @return
     */
    public static String getAppVersion(Context context){
        String version = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * md加密
     *
     * @param string
     * @return 2015-4-24 Administrator
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();

    }
}
