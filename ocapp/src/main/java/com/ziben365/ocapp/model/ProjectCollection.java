package com.ziben365.ocapp.model;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/22.
 * email  1956766863@qq.com
 */
public class ProjectCollection {

    public String id;       //": "1",
    public String name;       //"优质专题",
    public String desc;       // "优质项目集合地",
    public String img;       //"upload/userlogo/201601201049153030.jpg"

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
