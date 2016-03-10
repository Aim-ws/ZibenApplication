package com.ziben365.ocapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.ui.AddProjectActivity;
import com.ziben365.ocapp.ui.LoginActivity;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.widget.detector.FabCircleImageView;
import com.ziben365.ocapp.widget.detector.ObservableNestedScrollView;


/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/11.
 * email  1956766863@qq.com
 */
public class CardListFragment extends Fragment implements View.OnClickListener {
    public static final int CHANNEL_CHOICE = 0x0;     //精选
    public static final int CHANNEL_UP_TO_DATE = 0x1;     //最新
    public static final int CHANNEL_COLLECTION = 0x2;     //合集
    public static final int CHANNEL_TWITTERE = 0x3;       //推友
    public static final int CHANNEL_CHARTS = 0x4;       //榜单

    TabLayout tabLayout;
    ViewPager mViewPager;
    ObservableNestedScrollView mNestedScrollView;
    FabCircleImageView mFabCircleImageView;
    private int[] codes;
    private String[] titles;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cardlist, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mFabCircleImageView = (FabCircleImageView) view.findViewById(R.id.id_fab_circle_imageView);


        codes = new int[]{
                CHANNEL_CHOICE,
                CHANNEL_UP_TO_DATE,
                CHANNEL_COLLECTION,
                CHANNEL_TWITTERE,
                CHANNEL_CHARTS
        };

        titles = new String[]{
                getResources().getString(R.string.tab_1),
                getResources().getString(R.string.tab_2),
                getResources().getString(R.string.tab_3),
                getResources().getString(R.string.tab_4),
                getResources().getString(R.string.tab_5),
        };

        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_1), true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_3));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_4));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_5));

        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(mViewPager);
        mFabCircleImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String u_id = (String) SPUtils.get(getActivity(), SPKeys.KEY_USER_ID_TAG,"");
        if (TextUtils.isEmpty(u_id)){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else{
            startActivity(new Intent(getActivity(), AddProjectActivity.class));
        }
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (codes[position]) {
                case CHANNEL_CHOICE:
                    return ChannelChoiceFragment.newInstance(titles[position], CHANNEL_CHOICE);
                case CHANNEL_UP_TO_DATE:
                    return ChannelLatestFragment.newInstance(titles[position], CHANNEL_UP_TO_DATE);
                case CHANNEL_COLLECTION:
                    return ChannelAlbumFragment.newInstance(titles[position], CHANNEL_COLLECTION);
                case CHANNEL_TWITTERE:
                    return ChannelTwitterFragment.newInstance(titles[position], CHANNEL_TWITTERE);
                case CHANNEL_CHARTS:
                    return ChannelChartsFragment.newInstance(titles[position], CHANNEL_CHARTS);
            }
            return null;
        }

        @Override
        public int getCount() {
            return codes.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
