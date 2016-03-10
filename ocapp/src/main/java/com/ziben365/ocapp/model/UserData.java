package com.ziben365.ocapp.model;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2016/1/19.
 * email  1956766863@qq.com
 */
public class UserData {

    public String token;
    public int login_type;
    public UserInformation user_info;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLogin_type() {
        return login_type;
    }

    public void setLogin_type(int login_type) {
        this.login_type = login_type;
    }

    public UserInformation getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInformation user_info) {
        this.user_info = user_info;
    }
}
