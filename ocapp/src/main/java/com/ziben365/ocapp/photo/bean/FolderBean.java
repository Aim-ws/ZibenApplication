package com.ziben365.ocapp.photo.bean;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/18.
 * email  1956766863@qq.com
 */
public class FolderBean {

    //当前文件夹的路径
    public String dir;
    public String firstImgPath;
    public String name;
    public int count;

    public String getDir() {
        return dir;
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }



    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = dir.indexOf("/");
        this.name = dir.substring(lastIndexOf+1);
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
