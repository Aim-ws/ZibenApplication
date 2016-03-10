package com.ziben365.ocapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.ChannelAlbumAdapter;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.model.ProjectCollection;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.widget.refresh.ProgressStyle;
import com.ziben365.ocapp.widget.refresh.recycler.RefreshRecyclerView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/30.
 * email  1956766863@qq.com
 *
 * 专辑页面
 */
public class ChannelAlbumFragment extends Fragment implements RefreshRecyclerView.LoadingListener {

    @InjectView(R.id.id_recyclerView)
    RefreshRecyclerView mRecyclerView;

    private ChannelAlbumAdapter adapter;
    private List<ProjectCollection> list;

    public static ChannelAlbumFragment newInstance(String title, int channel) {
        ChannelAlbumFragment instance = new ChannelAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("channel", channel);
        bundle.putString("title", title);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_grid, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list = new ArrayList<>();
        adapter = new ChannelAlbumAdapter(getActivity(), list);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setAdapter(adapter);

        String str_data = (String) SPUtils.get(getActivity(),SPKeys.KEY_PROJECT_FIND_COLLECTION_TAG,"");
        if (StringUtils.isNotEmpty(str_data)){
            ArrayList<ProjectCollection> data = GsonUtil.getInstance().fromJson(
                    str_data, new TypeToken<ArrayList<ProjectCollection>>() {
                    }.getType());
            list.addAll(data);
            adapter.notifyDataSetChanged();
        }else{
            loadData();
        }

    }

    /**
     * 加载数据
     */
    private void loadData() {
        VolleyRequest.volleyRequestPost(Link.PROJECT_FIND_COLLECT_PROJECT,
                RequestTag.REQUEST_TAG_FIND_COLLECT_PROJECT, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(ChannelAlbumFragment.class.getSimpleName() + "     " + result);
                        if (StringUtils.isNotEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                SPUtils.put(getActivity(), SPKeys.KEY_PROJECT_FIND_COLLECTION_TAG,array.toString());
                                ArrayList<ProjectCollection> data = GsonUtil.getInstance().fromJson(
                                        array.toString(), new TypeToken<ArrayList<ProjectCollection>>() {
                                        }.getType());
                                list.clear();
                                list.addAll(data);
                                adapter.notifyDataSetChanged();
                                mRecyclerView.refreshComplete();
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
        loadData();
    }

    @Override
    public void onLoadMore() {

    }
}
