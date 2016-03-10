package com.ziben365.ocapp.model;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/13.
 * email  1956766863@qq.com
 */
public class ProjectStatus {

    public boolean isChecked;
    public String status_name;
    public int id;

    public ProjectStatus(int i, String s, boolean b) {
        this.id = i;
        this.status_name = s;
        this.isChecked = b;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
