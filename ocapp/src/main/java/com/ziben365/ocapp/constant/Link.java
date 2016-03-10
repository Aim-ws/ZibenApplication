package com.ziben365.ocapp.constant;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/14.
 * email  1956766863@qq.com
 */
public class Link {

    private static final String APP_BASE_URL = "http://test.api.ziben365.com";

    /*****用户登录******/
    public static final String USER_LOGIN = APP_BASE_URL + "/UnicornLogin/index";
    /*****用户第三方登录******/
    public static final String USER_OPEN_LOGIN = APP_BASE_URL + "/UnicornLogin/open";
    /*****用户获取验证码******/
    public static final String USER_MOBILE_RANDNUM= APP_BASE_URL + "/Unicornregister/mobile_verify_randnum";
    /*****用户快速注册******/
    public static final String USER_REGISTER = APP_BASE_URL + "/Unicornregister/reg_second";
    /*****检测用户昵称是否存在******/
    public static final String USER_CHECK_NCIK = APP_BASE_URL + "/Unicornuser/check_nickname";
    /*****完善用户资料信息******/
    public static final String USER_PERFECT_INFO = APP_BASE_URL + "/Unicornuser/user_info";


    /*****项目标签******/
    public static final String PROJECT_TAGS = APP_BASE_URL + "/unicornprj/pro_tags";
    /*****项目推荐理由******/
    public static final String PROJECT_RECOMMEND_REASON = APP_BASE_URL + "/unicornprj/pro_rec_reason";
    /*****推荐项目******/
    public static final String PROJECT_ADD_PROJECT = APP_BASE_URL + "/Unicornuser/addpro";
    /*****发现----精选项目******/
    public static final String PROJECT_FIND_SELECT_PROJECT = APP_BASE_URL + "/unicornprj/selpro";
    /*****发现----最新项目******/
    public static final String PROJECT_FIND_LATEST_PROJECT = APP_BASE_URL + "/unicornprj/latest_pro";
    /*****发现----合集******/
    public static final String PROJECT_FIND_COLLECT_PROJECT = APP_BASE_URL + "/unicornprj/procollects";
    /**********项目详情*************/
    public static final String PROJECT_DETAILS_PROJECT = APP_BASE_URL + "/unicornprj/proinfo";
    /************发表项目评论***************/
    public static final String PROJECT_RELEASE_COMMENT = APP_BASE_URL + "/Unicorncomment/addcomment";
    /************回复项目评论***************/
    public static final String PROJECT_REPLY_COMMENT = APP_BASE_URL + "/Unicorncomment/replycomment";
    /************项目评论列表***************/
    public static final String PROJECT_COMMENT_LIST = APP_BASE_URL + "/Unicornprj/commentlist";
    /************项目评论点赞***************/
    public static final String PROJECT_COMMENT_PRAISE = APP_BASE_URL + "/Unicorncomment/praise";
    /************每日精选***************/
    public static final String PROJECT_DAY_SELECT = APP_BASE_URL + "/Unicornprj/daysel";
    /************意见反馈***************/
    public static final String PERSONAL_FEEDBACK = APP_BASE_URL + "/Unicornprj/feedback";
    /************精选分类***************/
    public static final String PERSONAL_SEL_CATE = APP_BASE_URL + "/Unicornprj/Selprobytags";
    /************分类列表***************/
    public static final String PERSONAL_GET_SEL_INDUS = APP_BASE_URL + "/Unicornprj/get_sel_indus";
    /************合辑列表***************/
    public static final String PROJECT_GET_COL_MORE = APP_BASE_URL + "/Unicornprj/get_col_more";
    /************搜索***************/
    public static final String PROJECT_SEARCH = APP_BASE_URL + "/Unicornprj/search";
    /************大家搜索历史***************/
    public static final String PROJECT_SEARCH_HISTORY = APP_BASE_URL + "/Unicornprj/searchhistory";
    /************删除收藏/收藏***************/
    public static final String PROJECT_FAVORITES = APP_BASE_URL + "/Unicornfavorites/favorites";
    /************我的收藏***************/
    public static final String PROJECT_MY_FAVORITES = APP_BASE_URL + "/Unicornfavorites/my_favor_list";
    /************我的推荐***************/
    public static final String PROJECT_MY_RECOMMEND = APP_BASE_URL + "/Unicornuser/my_recommend";
    /************我的金币***************/
    public static final String PROJECT_MY_COINS = APP_BASE_URL + "/Unicornuser/my_coins";
    /************我的消息***************/
    public static final String PROJECT_MY_MESSAGE = APP_BASE_URL + "/Unicornmsg";
    /************用户的详情***************/
    public static final String PROJECT_USER_PROJECT_LIST = APP_BASE_URL + "/Unicornusrinfo/prolist";
    /************用户的详情***************/
    public static final String PROJECT_USER_PROJECT_UERINFO = APP_BASE_URL + "/Unicornusrinfo/get_usrinfo";
    /************项目点赞***************/
    public static final String PROJECT_USER_PROJECT_ADD_PRAISE = APP_BASE_URL + "/Unicornfavorites/add_praise";
    /************推友***************/
    public static final String PROJECT_USER_PROJECT_FRIEND_LIST = APP_BASE_URL + "/Unicornusrinfo/friendlist";
    /************榜单***************/
    public static final String PROJECT_USER_PROJECT_OFFICIAL_LIST = APP_BASE_URL + "/Unicornprj/official_list";
    /************签到***************/
    public static final String PROJECT_USER_SIGN = APP_BASE_URL + "/Unicornuser/sign";
    /************签到信息***************/
    public static final String PROJECT_USER_SIGN_INFO = APP_BASE_URL + "/Unicornuser/sign_info";
    /************消息状态***************/
    public static final String PROJECT_USER_MSG_INFO = APP_BASE_URL + "/Unicornmsg/get_msg_info";
    /************分享计数***************/
    public static final String PROJECT_SHARE = APP_BASE_URL + "/Unicornprj/share";
    /************分享***************/
    public static final String PROJECT_MOB_SHARE = APP_BASE_URL + "/Unicornprj/preview?id=";

}
