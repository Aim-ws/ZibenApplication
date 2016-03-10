package com.ziben365.ocapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.fragment.ChannelUserFragment;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.widget.CircleImageView;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/7.
 * email  1956766863@qq.com
 */
public class PersonalCenterActivity extends BaseActivity {


    @InjectView(R.id.id_user_avatar)
    CircleImageView mUserAvatar;
    @InjectView(R.id.id_user_name)
    TextView mUserName;
    @InjectView(R.id.id_user_city)
    TextView mUserCity;
    @InjectView(R.id.id_personal_gender)
    ImageView mPersonalGender;
    @InjectView(R.id.id_first)
    LinearLayout mFirst;
    @InjectView(R.id.id_layout_header)
    LinearLayout mLayoutHeader;
    @InjectView(R.id.id_edit_user_info)
    TextView mEditUserInfo;
    @InjectView(R.id.tabLayout)
    TabLayout mbLayout;
    @InjectView(R.id.id_container)
    FrameLayout mContainer;
    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_collapseToolbar)
    CollapsingToolbarLayout mCollapseToolbar;

    public static final int CHANNEL_RECOMMEND = 0x10;
    public static final int CHANNEL_LIKE = 0x11;
    public static final String KEY_PERSONAL_UID = "key_personal_uid";
    private String uid;

    public static final String FILE_USER_DATA_NAME = "file_user_data";
    public static final String KEY_USER_RECOMMEND = "key_user_recommend";
    public static final String KEY_USER_LIKE = "key_user_like";

    private UserInfo userInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persoanl_center);
        ButterKnife.inject(this);
        uid = getIntent().getStringExtra(KEY_PERSONAL_UID);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mCollapseToolbar.setTitleEnabled(false);

        mbLayout.addTab(mbLayout.newTab().setText("TA的推荐").setIcon(R.mipmap.icon_center_recommend));
        mbLayout.addTab(mbLayout.newTab().setText("TA的收藏").setIcon(R.mipmap.icon_center_like));

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.id_container, ChannelUserFragment.newInstance(CHANNEL_RECOMMEND, uid)).commit();

        initEvent();

        applyUserInfo();

    }

    private void applyUserInfo() {
        HashMap<String, String> param = new HashMap<>();
        param.put("uid", uid);
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_PROJECT_UERINFO,
                RequestTag.REQUEST_TAG_USER_PROJECT_USERINFO,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)){
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS){
                                JSONObject object = GsonUtil.pareJSONObject(result).optJSONObject(RequestCode.KEY_ARRAY);
                                userInfo = GsonUtil.getInstance().fromJson(object.toString(),UserInfo.class);
                                updateUserInfo();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    private void updateUserInfo() {
        if (null!=userInfo){
            mUserName.setText(TextUtils.isEmpty(userInfo.nick_name) ? "匿名" : userInfo.nick_name);
            mUserCity.setText(TextUtils.isEmpty(userInfo.area) ? "未知" : userInfo.area);
            if (!TextUtils.isEmpty(userInfo.logo)){
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + userInfo.logo + "!w100").crossFade().centerCrop().into(mUserAvatar);
            }else{
                mUserAvatar.setImageResource(R.mipmap.ic_default_avatar);
            }
            if (TextUtils.isEmpty(userInfo.gender)){
                mPersonalGender.setVisibility(View.GONE);
            }else{
                if ("0".endsWith(userInfo.gender)){
                    mPersonalGender.setImageResource(R.mipmap.icon_personal_gender_man);
                }
                if ("1".endsWith(userInfo.gender)){
                    mPersonalGender.setImageResource(R.mipmap.icon_personal_gender_women);
                }
            }

        }
    }

    private void initEvent() {
        mbLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (tab.getPosition()) {
                    case 0:
                        ft.replace(R.id.id_container, ChannelUserFragment.newInstance(CHANNEL_RECOMMEND, uid)).commit();
                        break;
                    case 1:
                        ft.replace(R.id.id_container, ChannelUserFragment.newInstance(CHANNEL_LIKE, uid)).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onStart() {
        getSharedPreferences(FILE_USER_DATA_NAME, MODE_PRIVATE).edit().clear().commit();
        super.onStart();
    }


    class UserInfo{
        public String id;     //": "837",
        public String logo;     // "upload/userlogo/201602251147405406.jpg",
        public String nick_name;     // "财神客栈老板娘",
        public String real_name;     // "测试"
        public String gender;     // "0",
        public String province;     // "",
        public String area;     // "福州市"
    }
}
