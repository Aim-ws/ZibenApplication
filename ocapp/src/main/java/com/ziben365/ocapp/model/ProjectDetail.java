package com.ziben365.ocapp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/2/24.
 * email  1956766863@qq.com
 */
public class ProjectDetail implements Serializable {

    public ProjectInfo info;
    public ArrayList<ProjectValuation> valuation;
    public ArrayList<ProjectComment> comments;
    public ArrayList<ProjectPraise> praise_person;


    public class ProjectInfo implements Serializable {
        public String pid;      //": "1",
        public String pname;      //"项目管理",
        public String logo;      // "upload/userlogo/201601201049153030.jpg",
        public String city;      // "安徽省 安庆市",
        public String tags;      // "互联网 通讯",
        public String product_status;      // "0",
        public String intro;      //"管理的机器",
        public String banner;      //"upload/userlogo/201601201049168081.jpg",
        public String pics;      //"upload/userlogo/201601201049167350.jpg;upload/userlogo/201601201049174008.jpg;upload/userlogo/201601201049193119.jpg",
        public String valuation;      // "99",
        public String pdesc;      //"管理项目的机器，不错的羡慕嫉妒恨，哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈",
        public String recreason;      // "管理项目的机器，不错的羡慕嫉妒恨，哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈"
        public String views;      //"179",
        public String praise;      // "2",
        public String comments;      // "30",
        public String share;      // "3",
        public String collects;
        public String secret_id;      //"4C9C"
    }


    public class ProjectValuation implements Serializable {
        public String id;      // "3",
        public String valuation;      // "99",
        public String add_time;      //"1453367884"
        public String real_name;   //": "wangshuai",
        public String logo;     //"upload/userlogo/201601221752058938.jpg"
        public String user_id;
    }

    public class ProjectPraise implements Serializable {
        public String id;      //": "1186",
        public String logo;      //"upload/userlogo/1456998568.png",
        public String nick_name;      //"Aim_ws",
        public String real_name;      //"Aim_ws"

    }


}
