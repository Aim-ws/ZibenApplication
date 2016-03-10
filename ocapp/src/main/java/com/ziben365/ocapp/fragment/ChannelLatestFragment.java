package com.ziben365.ocapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.ChannelChoiceAdapter;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.inter.OnItemProjectClickListener;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.model.Project;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.view.AppProgressDialog;
import com.ziben365.ocapp.widget.refresh.ProgressStyle;
import com.ziben365.ocapp.widget.refresh.recycler.RefreshRecyclerView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/10.
 * email  1956766863@qq.com
 */
public class ChannelLatestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        RefreshRecyclerView.LoadingListener,OnItemProjectClickListener {


    @InjectView(R.id.id_refresh_recyclerView)
    RefreshRecyclerView mRefreshRecyclerView;

    private ChannelChoiceAdapter adapter;
    private ArrayList<Project> entities;

    private LinearLayoutManager layoutManager;

    private AppProgressDialog progressDialog;


    private int channel = CardListFragment.CHANNEL_UP_TO_DATE;
    private int page = 1;

    public static ChannelLatestFragment newInstance(String title, int channel) {
        ChannelLatestFragment instance = new ChannelLatestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("channel", channel);
        bundle.putString("title", title);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_channel_card, null);
        ButterKnife.inject(this, convertView);
        return convertView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        channel = getArguments() == null ? CardListFragment.CHANNEL_UP_TO_DATE : getArguments().getInt("channel");
        progressDialog = new AppProgressDialog(getActivity());
        initData();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (entities.size() == 0){
            String str_data = (String) SPUtils.get(getActivity(),SPKeys.KEY_PROJECT_FIND_CHOICE_TAG,"");
            if (StringUtils.isNotEmpty(str_data)){
                ArrayList<Project> data = GsonUtil.getInstance().fromJson(str_data,
                        new TypeToken<ArrayList<Project>>() {
                        }.getType());
                entities.clear();
                entities.addAll(data);
                adapter.notifyDataSetChanged();
                loadData(1);
            }else{
                loadData(0);
            }
        }
    }


    /**
     * 初始化数据
     */
    private void initData() {

        //初始化容器
        entities = new ArrayList<>();
        entities.clear();

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //设置垂直排列
        mRefreshRecyclerView.setLayoutManager(layoutManager);
        //加载每条的动画
        mRefreshRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置下拉刷新
        mRefreshRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRefreshRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRefreshRecyclerView.setLoadingMoreEnabled(true);
        mRefreshRecyclerView.setLoadingListener(this);

        adapter = new ChannelChoiceAdapter(getActivity(), entities, channel);
        adapter.setOnItemProjectClickListener(this);
        mRefreshRecyclerView.setAdapter(adapter);


    }


    /**
     * 加载数据
     * @param i
     */
    private void loadData(int i) {
        if (null!=progressDialog && i == 0) progressDialog.show();
        page = 1;
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_FIND_LATEST_PROJECT, RequestTag.REQUEST_TAG_FIND_LATEST_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        if (null!=progressDialog) progressDialog.dismiss();
                        String result = o.toString();
                        L.i("----------" + ChannelLatestFragment.class.getSimpleName() + "-----------" + result);
                        if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                            JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray("data");
                            SPUtils.put(getActivity(), SPKeys.KEY_PROJECT_FIND_CHOICE_TAG,array.toString());
                            ArrayList<Project> data = GsonUtil.getInstance().fromJson(array.toString(),
                                    new TypeToken<ArrayList<Project>>() {
                                    }.getType());
                            entities.clear();
                            entities.addAll(data);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        if (null!=progressDialog) progressDialog.dismiss();
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


    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        page = 1;
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_FIND_LATEST_PROJECT, RequestTag.REQUEST_TAG_FIND_LATEST_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i("----------" + channel + "-----------" + result);
                        if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                            JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray("data");
                            SPUtils.put(getActivity(), SPKeys.KEY_PROJECT_FIND_CHOICE_TAG,array.toString());
                            ArrayList<Project> data = GsonUtil.getInstance().fromJson(array.toString(),
                                    new TypeToken<ArrayList<Project>>() {
                                    }.getType());
                            entities.clear();
                            entities.addAll(data);
                            adapter.notifyDataSetChanged();
                            mRefreshRecyclerView.refreshComplete();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    @Override
    public void onLoadMore() {
        LoadMore();
    }

    private void LoadMore() {
        ++page;
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_FIND_LATEST_PROJECT, RequestTag.REQUEST_TAG_FIND_LATEST_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i("----------" + channel + "-----------" + result);
                        if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                            JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray("data");
                            ArrayList<Project> data = GsonUtil.getInstance().fromJson(array.toString(),
                                    new TypeToken<ArrayList<Project>>() {
                                    }.getType());
                            entities.addAll(data);
                            adapter.notifyDataSetChanged();
                            mRefreshRecyclerView.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    @Override
    public void onItemProjectClick(String p_id) {
        ProjectItemClickUtil.itemClickDetails(getActivity(),p_id);
    }
}
