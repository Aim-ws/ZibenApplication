package com.ziben365.ocapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.ChannelTwitterAdapter;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.model.ProjectTwitter;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.widget.detector.FabCircleImageView;
import com.ziben365.ocapp.widget.refresh.ProgressStyle;
import com.ziben365.ocapp.widget.refresh.recycler.RefreshRecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/31.
 * email  1956766863@qq.com
 * <p/>
 * 推友
 */
public class ChannelTwitterFragment extends Fragment implements RefreshRecyclerView.LoadingListener {
    @InjectView(R.id.id_header_hint)
    TextView mHeaderHint;
    @InjectView(R.id.id_refresh_recyclerView)
    RefreshRecyclerView mRefreshRecyclerView;
    @InjectView(R.id.id_fab_circle_imageView)
    FabCircleImageView mFabCircleImageView;



    private int page = 1;


//    @InjectView(R.id.id_fab_circle_imageView)
//    FabCircleImageView mFabCircleImageView;

    private List<ProjectTwitter> list;
    private ChannelTwitterAdapter adapter;

    public static ChannelTwitterFragment newInstance(String title, int channel) {
        ChannelTwitterFragment instance = new ChannelTwitterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("channel", channel);
        bundle.putString("title", title);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_twitter, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = new ArrayList<>();
        adapter = new ChannelTwitterAdapter(getActivity(), list);
        mRefreshRecyclerView.setAdapter(adapter);

        mRefreshRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRefreshRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRefreshRecyclerView.setLoadingMoreEnabled(true);
        mRefreshRecyclerView.setLoadingListener(this);
        mRefreshRecyclerView.setPullRefreshEnabled(true);
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        String result = (String) SPUtils.get(getActivity(), SPKeys.KEY_PROJECT_FIND_TWITTER_TAG, "");
        if (!TextUtils.isEmpty(result)) {
            ArrayList<ProjectTwitter> data = GsonUtil.getInstance().fromJson(
                    result, new TypeToken<ArrayList<ProjectTwitter>>() {
                    }.getType());
            list.clear();
            list.addAll(data);
            adapter.notifyDataSetChanged();
            refresh(0);
        } else {
            refresh(0);
        }


    }

    private void refresh(final int type) {
        page = 1;
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_PROJECT_FRIEND_LIST,
                RequestTag.REQUEST_TAG_USER_PROJECT_FRIEND_LIST,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                SPUtils.put(getActivity(), SPKeys.KEY_PROJECT_FIND_TWITTER_TAG, array.toString());
                                ArrayList<ProjectTwitter> data = GsonUtil.getInstance().fromJson(
                                        array.toString(), new TypeToken<ArrayList<ProjectTwitter>>() {
                                        }.getType());
                                list.clear();
                                list.addAll(data);
                                adapter.notifyDataSetChanged();
                                if (type == 1) {
                                    mRefreshRecyclerView.refreshComplete();
                                }
                            } else {
                                Toast.makeText(getActivity(), GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @Override
    public void onRefresh() {
        refresh(1);
    }

    @Override
    public void onLoadMore() {
        loadMore();
    }

    private void loadMore() {
        ++page;
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_PROJECT_FRIEND_LIST,
                RequestTag.REQUEST_TAG_USER_PROJECT_FRIEND_LIST,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectTwitter> data = GsonUtil.getInstance().fromJson(
                                        array.toString(), new TypeToken<ArrayList<ProjectTwitter>>() {
                                        }.getType());
                                list.addAll(data);
                                adapter.notifyDataSetChanged();
                                mRefreshRecyclerView.loadMoreComplete();
                            } else {
                                Toast.makeText(getActivity(), GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

    }
}
