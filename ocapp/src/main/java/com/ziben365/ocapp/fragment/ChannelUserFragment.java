package com.ziben365.ocapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.ChannelPersonalAdapter;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.model.UserLike;
import com.ziben365.ocapp.model.UserRecommend;
import com.ziben365.ocapp.ui.PersonalCenterActivity;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.view.AppProgressDialog;
import com.ziben365.ocapp.widget.refresh.ProgressStyle;
import com.ziben365.ocapp.widget.refresh.recycler.RefreshRecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/14.
 * email  1956766863@qq.com
 */
public class ChannelUserFragment extends Fragment implements RefreshRecyclerView.LoadingListener {

    @InjectView(R.id.id_refresh_recyclerView)
    RefreshRecyclerView mRefreshRecyclerView;
    @InjectView(R.id.id_hint)
    TextView mHint;
    @InjectView(R.id.id_go_to)
    TextView mGoTo;
    @InjectView(R.id.id_empty_1)
    LinearLayout mEmpty1;
    @InjectView(R.id.id_empty)
    FrameLayout mEmpty;
    @InjectView(R.id.id_empty_image)
    ImageView mEmptyImage;


    private ArrayList<UserRecommend> userRecommends;
    private ArrayList<UserLike> userLikes;
    private ChannelPersonalAdapter adapter;
    private int channel = PersonalFragment.CHANNEL_RECOMMEND;
    private AppProgressDialog progressDialog;
    private String uid;

    private SharedPreferences sp;


    private int page = 1;
    private int type = 0;     //type----0 推荐，type-------1收藏

    public static Fragment newInstance(int channel, String uid) {
        ChannelUserFragment instance = new ChannelUserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("channel", channel);
        bundle.putString("uid", uid);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_personal, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        sp = getActivity().getSharedPreferences(PersonalCenterActivity.FILE_USER_DATA_NAME, Context.MODE_PRIVATE);

        channel = getArguments().getInt("channel");
        uid = getArguments().getString("uid");
        progressDialog = new AppProgressDialog(getActivity());


        switch (channel) {
            case PersonalCenterActivity.CHANNEL_RECOMMEND:
                type = 0;
                userRecommends = new ArrayList<>();
                adapter = new ChannelPersonalAdapter(getActivity(), userRecommends, channel);
                mRefreshRecyclerView.setAdapter(adapter);

                String resource = sp.getString(PersonalCenterActivity.KEY_USER_RECOMMEND, "");
                if (TextUtils.isEmpty(resource)) {
                    applyData(0);
                } else {
                    ArrayList<UserRecommend> recommends = GsonUtil.getInstance().fromJson(
                            resource, new TypeToken<ArrayList<UserRecommend>>() {
                            }.getType());
                    userRecommends.clear();
                    userRecommends.addAll(recommends);
                    adapter.notifyDataSetChanged();
                    if (userRecommends.size() == 0) {
                        dealEmpty();
                    } else {
                        mEmpty.setVisibility(View.GONE);
                        mEmpty1.setVisibility(View.GONE);
                        mEmptyImage.setVisibility(View.GONE);
                    }
                    applyData(1);
                }
                break;
            case PersonalCenterActivity.CHANNEL_LIKE:
                type = 1;
                userLikes = new ArrayList<>();
                adapter = new ChannelPersonalAdapter(getActivity(), userLikes, channel);
                mRefreshRecyclerView.setAdapter(adapter);

                String resource1 = sp.getString(PersonalCenterActivity.KEY_USER_LIKE, "");
                if (TextUtils.isEmpty(resource1)) {
                    applyData(0);
                } else {
                    ArrayList<UserLike> likes = GsonUtil.getInstance().fromJson(
                            resource1, new TypeToken<ArrayList<UserLike>>() {
                            }.getType());
                    userLikes.clear();
                    userLikes.addAll(likes);
                    adapter.notifyDataSetChanged();
                    if (userLikes.size() == 0) {
                        dealEmpty();
                    } else {
                        mEmpty.setVisibility(View.GONE);
                        mEmpty1.setVisibility(View.GONE);
                        mEmptyImage.setVisibility(View.GONE);
                    }
                    applyData(1);
                }
                break;
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //设置垂直排列
        mRefreshRecyclerView.setLayoutManager(layoutManager);
        //加载每条的动画
        mRefreshRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置刷新
        mRefreshRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRefreshRecyclerView.setLoadingMoreEnabled(true);
        mRefreshRecyclerView.setLoadingListener(this);
        mRefreshRecyclerView.setPullRefreshEnabled(false);

    }

    /**
     * 请求数据
     *
     * @param i
     */
    private void applyData(int i) {
        page = 1;
        if (i == 0) progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("type", String.valueOf(type));
        param.put("uid", uid);
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_PROJECT_LIST,
                RequestTag.REQUEST_TAG_USER_PROJECT_LIST,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        progressDialog.dismiss();
                        L.i("---------------result------------" + result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                switch (channel) {
                                    case PersonalFragment.CHANNEL_RECOMMEND:
                                        sp.edit().putString(PersonalCenterActivity.KEY_USER_RECOMMEND, array.toString()).commit();
                                        ArrayList<UserRecommend> recommends = GsonUtil.getInstance().fromJson(
                                                array.toString(), new TypeToken<ArrayList<UserRecommend>>() {
                                                }.getType());
                                        userRecommends.clear();
                                        userRecommends.addAll(recommends);
                                        adapter.notifyDataSetChanged();
                                        if (userRecommends.size() == 0) {
                                            dealEmpty();
                                        } else {
                                            mEmpty.setVisibility(View.GONE);
                                            mEmpty1.setVisibility(View.GONE);
                                            mEmptyImage.setVisibility(View.GONE);
                                        }
                                        break;
                                    case PersonalFragment.CHANNEL_LIKE:
                                        sp.edit().putString(PersonalCenterActivity.KEY_USER_LIKE, array.toString()).commit();
                                        ArrayList<UserLike> likes = GsonUtil.getInstance().fromJson(
                                                array.toString(), new TypeToken<ArrayList<UserLike>>() {
                                                }.getType());
                                        userLikes.clear();
                                        userLikes.addAll(likes);
                                        adapter.notifyDataSetChanged();
                                        if (userLikes.size() == 0) {
                                            dealEmpty();
                                        } else {
                                            mEmpty.setVisibility(View.GONE);
                                            mEmpty1.setVisibility(View.GONE);
                                            mEmptyImage.setVisibility(View.GONE);
                                        }

                                        break;
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void dealEmpty() {
        mEmpty.setVisibility(View.VISIBLE);
        mEmpty1.setVisibility(View.GONE);
        mEmptyImage.setVisibility(View.VISIBLE);
        if (channel == PersonalCenterActivity.CHANNEL_RECOMMEND) {
            mEmptyImage.setImageResource(R.mipmap.ic_user_p_empty_1);
        }
        if (channel == PersonalCenterActivity.CHANNEL_LIKE) {
            mEmptyImage.setImageResource(R.mipmap.ic_user_p_empty_2);
        }
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        loadMore();
    }

    private void loadMore() {
        ++page;
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("type", String.valueOf(type));
        param.put("uid", uid);
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_PROJECT_LIST,
                RequestTag.REQUEST_TAG_USER_PROJECT_LIST,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i("---------------result------------" + result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                switch (channel) {
                                    case PersonalFragment.CHANNEL_RECOMMEND:
                                        ArrayList<UserRecommend> recommends = GsonUtil.getInstance().fromJson(
                                                array.toString(), new TypeToken<ArrayList<UserRecommend>>() {
                                                }.getType());
                                        userRecommends.addAll(recommends);
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case PersonalFragment.CHANNEL_LIKE:
                                        ArrayList<UserLike> likes = GsonUtil.getInstance().fromJson(
                                                array.toString(), new TypeToken<ArrayList<UserLike>>() {
                                                }.getType());
                                        userLikes.addAll(likes);
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case PersonalFragment.CHANNEL_MESSAGE:

                                        break;
                                }
                                adapter.notifyDataSetChanged();
                                mRefreshRecyclerView.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

    }

    @Override
    public void onStop() {
        ProjectItemClickUtil.dismiss();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

   /* private class LikeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NotifyAction.UPDATE_USER_LIKE)) {
                applyData();
            }
        }
    }*/


}
