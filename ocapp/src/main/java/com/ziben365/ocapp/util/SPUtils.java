package com.ziben365.ocapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ziben365.ocapp.model.UserInformation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/23.
 * email  1956766863@qq.com
 */
public class SPUtils {

    /***
     * 里面所有的commit操作使用了SharedPreferencesCompat.apply进行了替代，目的是尽可能的使用apply代替commit
     * 首先说下为什么，因为commit方法是同步的，并且我们很多时候的commit操作都是UI线程中，毕竟是IO操作，尽可能异步；
     * 所以我们使用apply进行替代，apply异步的进行写入
     */
    private static final String FILE_NAME = "app_share_data";

    /**
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, "");
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, 0);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, false);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, 0);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, 0);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 保存用户信息
     *
     * @param context
     * @param userInformation
     */
    public static void saveUser(Context context, UserInformation userInformation) {
        put(context, SPKeys.KEY_USER_ID_TAG, userInformation.id);
        put(context, SPKeys.KEY_USER_NAME_TAG, userInformation.real_name);
        put(context, SPKeys.KEY_USER_NICK_TAG, userInformation.nick_name);
        put(context, SPKeys.KEY_USER_IMAGE_TAG, userInformation.logo);
        put(context, SPKeys.KEY_USER_GENDER_TAG, String.valueOf(userInformation.gender));
        put(context, SPKeys.KEY_USER_AREA_TAG, userInformation.area);
        put(context, SPKeys.KEY_USER_MOBILE_TAG, userInformation.mobile);
        put(context, SPKeys.KEY_USER_EMAIL_TAG, userInformation.email);
        put(context, SPKeys.KEY_USER_REG_TIME_TAG, userInformation.reg_time);
    }

    /**
     * 清除用户信息
     *
     * @param context
     */
    public static void removeUser(Context context) {
        put(context, SPKeys.KEY_USER_ID_TAG, "");
        put(context, SPKeys.KEY_USER_NAME_TAG, "");
        put(context, SPKeys.KEY_USER_NICK_TAG, "");
        put(context, SPKeys.KEY_USER_IMAGE_TAG, "");
        put(context, SPKeys.KEY_USER_GENDER_TAG, "");
        put(context, SPKeys.KEY_USER_MOBILE_TAG, "");
        put(context, SPKeys.KEY_USER_EMAIL_TAG, "");
        put(context, SPKeys.KEY_USER_AREA_TAG, "");
        put(context, SPKeys.KEY_USER_REG_TIME_TAG, "");


    }

    public static String getToken(Context context) {
        if (contains(context, SPKeys.KEY_USER_TOKEN_TAG)) {
            L.i("----------------" + get(context, SPKeys.KEY_USER_TOKEN_TAG, ""));
            return (String) get(context, SPKeys.KEY_USER_TOKEN_TAG, "");
        }
        return "";
    }


    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找所有的apply
         *
         * @return
         */
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}
