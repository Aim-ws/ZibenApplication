package com.ziben365.ocapp.model;

import java.io.Serializable;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2016/1/7.
 * email  1956766863@qq.com
 */
public class Tag implements Serializable{

    public boolean checked;
    public String tagName;
    public int id;

    public Tag(int i, String s, boolean b) {
        this.id = i;
        this.tagName = s;
        this.checked = b;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
