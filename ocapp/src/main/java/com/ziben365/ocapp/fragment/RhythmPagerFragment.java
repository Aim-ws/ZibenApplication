package com.ziben365.ocapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.MainActivity;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.RhythmAdapter;
import com.ziben365.ocapp.adapter.RhythmPagerAdapter;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.model.ProjectCard;
import com.ziben365.ocapp.refresh.extra.PullToRefreshViewPager;
import com.ziben365.ocapp.refresh.library.PullToRefreshBase;
import com.ziben365.ocapp.util.AnimatorUtils;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.view.AppProgressDialog;
import com.ziben365.ocapp.widget.rhythm.IRhythmItemListener;
import com.ziben365.ocapp.widget.rhythm.RhythmLayout;
import com.ziben365.ocapp.widget.rhythm.ViewPagerScroller;

import org.json.JSONArray;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/11.
 * email  1956766863@qq.com
 */
public class RhythmPagerFragment extends Fragment implements PullToRefreshBase.OnRefreshListener<ViewPager> {
    private View mMainView;     //控制背景变化
    private RhythmLayout mRhythmLayout;    //钢琴布局
    private PullToRefreshViewPager mPullToRefreshViewPager;    //刷新viewpager

    private ViewPager mViewPager;     //接收PullToRefreshViewPager中的ViewPager控件

    private RhythmPagerAdapter mPagerAdapter;    //viewpager的适配器

    private int mPreColor;    // 记录选项的颜色值

    private boolean mHasNext = true;   //判断是否还有下一条的标志

    private boolean mIsRequesting;   //是否正在请求

    private ArrayList<ProjectCard> mCardList = new ArrayList<>();

    private RhythmAdapter mRhythmAdapter;   //钢琴布局的adapter适配器

    private Handler mHandler = new Handler();     //更新handler

    private MainActivity activity;
    private AppProgressDialog progressDialog;

    private int page = 1;

    /**
     * 钢琴控件的切换监听
     */
    private IRhythmItemListener iRhythmItemListener = new IRhythmItemListener() {
        @Override
        public void onRhythmItemChanged(int paramInt) {

        }

        @Override
        public void onSelected(final int paramInt) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setCurrentItem(paramInt);
                }
            }, 200L);
        }

        @Override
        public void onStartSwipe() {

        }
    };

    /**
     * viewpager的切换监听
     */
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switchPager(position);
            if (mHasNext && (position > mCardList.size()) && !mIsRequesting) {
                mPullToRefreshViewPager.setRefreshing();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rhythm, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void initView(View view) {
        activity = (MainActivity) getActivity();
        progressDialog = new AppProgressDialog(activity);

        mMainView = view.findViewById(R.id.relayout);
        mRhythmLayout = (RhythmLayout) view.findViewById(R.id.rhythm_layout_pager);
        mPullToRefreshViewPager = (PullToRefreshViewPager) view.findViewById(R.id.pull_to_refresh_viewpager);

        mViewPager = mPullToRefreshViewPager.getRefreshableView();
        //设置viewpager的滚动速率
        setViewPagerScrollSpeed(mViewPager, 400);
        //设置ScrollView滚动动画延迟执行的时间
        mRhythmLayout.setScrollRhythmStartDelayTime(400);

        //设置钢琴布局的高度 高度为钢琴布局item的宽度+10dp
        int height = (int) mRhythmLayout.getRhythmItemWidth() + (int) TypedValue.applyDimension(1, 10.0F, getResources().getDisplayMetrics());
        mRhythmLayout.getLayoutParams().height = height;
        ((RelativeLayout.LayoutParams) mPullToRefreshViewPager.getLayoutParams()).bottomMargin = height;

        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        mCardList.clear();
        fetchData();
        //设置监听事件
        initListener();


    }

    private void fetchData() {
        /*ArrayList<ProjectCard> cardList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cardList.add(new ProjectCard());
        }
        //mPreColor = mColors[0];   //初始化颜色
        initColor(0);
        //设置adapter
        updateAdapter(cardList);*/

//        initColor(0);
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_DAY_SELECT,
                RequestTag.REQUEST_TAG_PROJECT_DAY_SELECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        progressDialog.dismiss();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectCard> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectCard>>() {
                                        }.getType());
                                updateAdapter(data);
                                //默认选中第一个选项
                                switchPager(0);
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });


    }

    private void initColor(int i) {
        mPreColor = getActivity().getResources().getColor(R.color.colorAppbar);
        getView().setBackgroundColor(getResources().getColor(R.color.colorAppbar));
        activity.getToolbar().setBackgroundColor(getResources().getColor(R.color.colorAppbar));
    }

    /**
     * 更新adapter的数据
     *
     * @param cardList
     */
    private void updateAdapter(ArrayList<ProjectCard> cardList) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        if (cardList.isEmpty()) {
            this.mMainView.setBackgroundColor(mPreColor);
            return;
        }

        int size = cardList.size();
        if (mPagerAdapter == null) {
            mPagerAdapter = new RhythmPagerAdapter(getChildFragmentManager(), cardList);
            mViewPager.setAdapter(mPagerAdapter);
        } else {
            mPagerAdapter.addCardList(cardList);
            mPagerAdapter.notifyDataSetChanged();
        }

        addCardIconsToDock(cardList);

        this.mCardList = mPagerAdapter.getCardList();


        if (mViewPager.getCurrentItem() == size - 1)
            mViewPager.setCurrentItem(1 + mViewPager.getCurrentItem(), true);
    }

    /**
     * 设置钢琴的图标icon
     *
     * @param cardList
     */
    private void addCardIconsToDock(final ArrayList<ProjectCard> cardList) {
        if (mRhythmAdapter == null) {
            resetRhythmLayout(cardList);
            return;
        }
        mRhythmAdapter.addCardList(cardList);
        mRhythmAdapter.notifyDataSetChanged();
    }

    /**
     * 重置 初始化钢琴控件
     *
     * @param cardList
     */
    private void resetRhythmLayout(ArrayList<ProjectCard> cardList) {
        if (getActivity() == null)
            return;
        if (cardList == null) {
            cardList = new ArrayList<>();
        }
        mRhythmAdapter = new RhythmAdapter(getActivity(), mRhythmLayout, cardList);
        mRhythmLayout.setAdapter(mRhythmAdapter);
    }


    /**
     * pager的切换
     *
     * @param position
     */
    private void switchPager(int position) {
        //执行动画，改变升起的钢琴按钮
        mRhythmLayout.showRhythmAtPosition(position);
        //得到当前的背景颜色
        String currentBackColor = mCardList.get(position).bgcolor.replace("0x", "#");
        int currColor = Color.parseColor(currentBackColor);
        MainActivity.rhythmColor = currColor;

        //执行颜色转换动画
        AnimatorUtils.showBackgroundColorAnimation(this.mMainView, mPreColor, currColor, 400);
        mPreColor = currColor;
//        activity.switchRhythmColor(position);
        getView().setBackgroundColor(mPreColor);
        activity.getToolbar().setBackgroundColor(mPreColor);
        //设置nav的背景颜色
//        activity.getNavView().setBackgroundColor(mPreColor);
//        activity.getNavView().getHeaderView(0).setBackgroundColor(mPreColor);

    }


    /**
     * c
     * 初始化监听
     */
    private void initListener() {
        //设置控件的监听
        mRhythmLayout.setRhythmListener(iRhythmItemListener);
        mPullToRefreshViewPager.setOnRefreshListener(this);
        mViewPager.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * 设置viewpager的滚动速度
     *
     * @param mViewPager 控件
     * @param speed      //速度
     */
    private void setViewPagerScrollSpeed(ViewPager mViewPager, int speed) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(mViewPager.getContext(), new OvershootInterpolator(0.6F));
            field.set(mViewPager, viewPagerScroller);
            viewPagerScroller.setDuration(speed);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh(PullToRefreshBase<ViewPager> refreshView) {
        //刷新数据
        if (this.mIsRequesting)
            return;
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {//最右
            mIsRequesting = true;
            loadMore();
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchData();
                    mPullToRefreshViewPager.onRefreshComplete();
                    mIsRequesting = false;
                }
            }, 2000);*/

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {//最左
            refresh();
            mIsRequesting = true;
            mPullToRefreshViewPager.onRefreshComplete();
            mIsRequesting = false;
        }
    }

    private void refresh() {
        page = 1;

        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_DAY_SELECT,
                RequestTag.REQUEST_TAG_PROJECT_DAY_SELECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectCard> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectCard>>() {
                                        }.getType());
                                mCardList.clear();
                                mRhythmAdapter.getCardList().clear();
                                updateAdapter(data);
                                mPullToRefreshViewPager.onRefreshComplete();
                                mIsRequesting = false;
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                });
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        ++page;
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_DAY_SELECT,
                RequestTag.REQUEST_TAG_PROJECT_DAY_SELECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectCard> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectCard>>() {
                                        }.getType());
                                updateAdapter(data);
                                mPullToRefreshViewPager.onRefreshComplete();
                                mIsRequesting = false;
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                });
    }


}
