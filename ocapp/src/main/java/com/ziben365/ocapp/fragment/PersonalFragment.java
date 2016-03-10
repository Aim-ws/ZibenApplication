package com.ziben365.ocapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.ui.EditProfileActivity;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/6.
 * email  1956766863@qq.com
 */
public class PersonalFragment extends Fragment {


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
    @InjectView(R.id.tabLayout)
    TabLayout tabLayout;

    public static final int CHANNEL_RECOMMEND = 0x10;
    public static final int CHANNEL_LIKE = 0x11;
    public static final int CHANNEL_DOLLAR = 0x12;
    public static final int CHANNEL_MESSAGE = 0x13;
    @InjectView(R.id.id_layout_header)
    LinearLayout mLayoutHeader;
    @InjectView(R.id.id_edit_user_info)
    TextView mEditUserInfo;
    @InjectView(R.id.id_container)
    FrameLayout idContainer;

    public static final String KEY_RECOMMEND = "key_recommend";
    public static final String KEY_LIKE = "key_like";
    public static final String KEY_TOTAL_GOLD = "key_total_gold";
    public static final String KEY_DAY_GOLD = "key_day_gold";
    public static final String KEY_RANK = "key_rank";
    public static final String KEY_MESSAGE = "key_message";

    public static final String FILE_DATA_NAME = "file_personal_data";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        IntentFilter filter = new IntentFilter(NotifyAction.ACTION_UPDATE_USER_INFO);
        getActivity().registerReceiver(updateUserInfoBroadcastReceiver, filter);

        tabLayout.addTab(tabLayout.newTab().setText("我的推荐").setIcon(R.mipmap.icon_center_recommend));
        tabLayout.addTab(tabLayout.newTab().setText("我的收藏").setIcon(R.mipmap.icon_center_like));
        tabLayout.addTab(tabLayout.newTab().setText("我的金币").setIcon(R.mipmap.icon_center_dollar));
        tabLayout.addTab(tabLayout.newTab().setText("我的消息").setIcon(R.mipmap.icon_center_message));

        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.id_container, ChannelPersonalFragment.newInstance(CHANNEL_RECOMMEND)).commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getChildFragmentManager();
                switch (tab.getPosition()) {
                    case 0:
                        fm.beginTransaction().replace(R.id.id_container,
                                ChannelPersonalFragment.newInstance(CHANNEL_RECOMMEND)).commit();
                        break;
                    case 1:
                        fm.beginTransaction().replace(R.id.id_container,
                                ChannelPersonalFragment.newInstance(CHANNEL_LIKE)).commit();
                        break;
                    case 2:
                        fm.beginTransaction().replace(R.id.id_container,
                                ChannelPersonalFragment.newInstance(CHANNEL_DOLLAR)).commit();

                        break;
                    case 3:
                        fm.beginTransaction().replace(R.id.id_container,
                                ChannelPersonalFragment.newInstance(CHANNEL_MESSAGE)).commit();

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
        initEvent();
        initData();
    }

    /**
     * 设置点击事件
     */
    private void initEvent() {
        mUserName.setOnClickListener(listener);
        mUserAvatar.setOnClickListener(listener);
        mUserCity.setOnClickListener(listener);
        mPersonalGender.setOnClickListener(listener);
    }

    private void initData() {
        String logo = (String) SPUtils.get(getActivity(), SPKeys.KEY_USER_IMAGE_TAG, "");
        String nick = (String) SPUtils.get(getActivity(), SPKeys.KEY_USER_NICK_TAG, "");
        String city = (String) SPUtils.get(getActivity(), SPKeys.KEY_USER_AREA_TAG, "");
        String gender = (String) SPUtils.get(getActivity(), SPKeys.KEY_USER_GENDER_TAG, "");
        if (TextUtils.isEmpty(logo) && TextUtils.isEmpty(nick) &&
                TextUtils.isEmpty(city) && TextUtils.isEmpty(gender)) {
            mEditUserInfo.setVisibility(View.VISIBLE);
            mLayoutHeader.setVisibility(View.GONE);
        } else {
            mLayoutHeader.setVisibility(View.VISIBLE);
            mEditUserInfo.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(logo)) {
            if (logo.contains("http://")) {
                Glide.with(DemoApplication.applicationContect).load(logo).crossFade().centerCrop().into(mUserAvatar);
            } else {
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + logo + "!w100").crossFade().centerCrop().into(mUserAvatar);
            }
        } else {
            mUserAvatar.setImageResource(R.mipmap.ic_default_avatar);
        }
        mUserName.setText(TextUtils.isEmpty(nick) ? "匿名" : nick);
        mUserCity.setText(TextUtils.isEmpty(city) ? "未知" : city);
        if (TextUtils.isEmpty(gender)) {
            mPersonalGender.setVisibility(View.GONE);
        } else {
            if ("0".endsWith(gender)) {
                mPersonalGender.setImageResource(R.mipmap.icon_personal_gender_man);
            }
            if ("1".endsWith(gender)) {
                mPersonalGender.setImageResource(R.mipmap.icon_personal_gender_women);
            }
        }
    }


    @Override
    public void onDestroyView() {
        //清空数据
        getActivity().getSharedPreferences(FILE_DATA_NAME, Context.MODE_PRIVATE).edit().clear().commit();
        getActivity().unregisterReceiver(updateUserInfoBroadcastReceiver);
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    BroadcastReceiver updateUserInfoBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().endsWith(NotifyAction.ACTION_UPDATE_USER_INFO)) {
                //清空数据
                getActivity().getSharedPreferences(FILE_DATA_NAME, Context.MODE_PRIVATE).edit().clear().commit();
                initData();
            }
        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        }
    };


}
