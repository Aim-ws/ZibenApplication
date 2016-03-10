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
 * <p>
 * Created by Administrator
 * on 2015/12/10.
 * email  1956766863@qq.com
 */
public class ChannelChoiceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        RefreshRecyclerView.LoadingListener, OnItemProjectClickListener {


    @InjectView(R.id.id_refresh_recyclerView)
    RefreshRecyclerView mRefreshRecyclerView;

    private ChannelChoiceAdapter adapter;
    private ArrayList<Project> entities;

    private LinearLayoutManager layoutManager;
    private int channel = CardListFragment.CHANNEL_CHOICE;

    private AppProgressDialog progressDialog;


    private int page = 1;

    public static ChannelChoiceFragment newInstance(String title, int channel) {
        ChannelChoiceFragment instance = new ChannelChoiceFragment();
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
        channel = getArguments() == null ? CardListFragment.CHANNEL_CHOICE : getArguments().getInt("channel");
        initData();

    }


    /**
     * 初始化数据
     */
    private void initData() {
        progressDialog = new AppProgressDialog(getActivity());

        //初始化容器
        entities = new ArrayList<>();
        adapter = new ChannelChoiceAdapter(getActivity(), entities, channel);
        adapter.setOnItemProjectClickListener(this);
        mRefreshRecyclerView.setAdapter(adapter);

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


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (entities.size() == 0) {
            String str_data = (String) SPUtils.get(getActivity(), SPKeys.KEY_PROJECT_FIND_LATEST_TAG, "");
            if (StringUtils.isNotEmpty(str_data)) {
                ArrayList<Project> data = GsonUtil.getInstance().fromJson(str_data,
                        new TypeToken<ArrayList<Project>>() {
                        }.getType());
                entities.clear();
                entities.addAll(data);
                adapter.notifyDataSetChanged();
                loadData(1);
            } else {
                loadData(0);
            }
        }
    }

    /**
     * 加载数据
     * @param i
     */
    private void loadData(int i) {
        if (null != progressDialog && i == 0) progressDialog.show();
        page = 1;
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_FIND_SELECT_PROJECT, RequestTag.REQUEST_TAG_FIND_SELECT_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        if (null != progressDialog) progressDialog.dismiss();
                        String result = o.toString();
                        if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                            JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray("data");
                            SPUtils.put(getActivity(), SPKeys.KEY_PROJECT_FIND_LATEST_TAG, array.toString());
                            ArrayList<Project> data = GsonUtil.getInstance().fromJson(array.toString(),
                                    new TypeToken<ArrayList<Project>>() {
                                    }.getType());
                            entities.clear();
                            entities.addAll(data);
                            L.i("--------------------1--------" + entities.size());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        if (null != progressDialog) progressDialog.dismiss();
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

    /**
     * 下拉刷新
     */
    private void refresh() {
        page = 1;
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_FIND_SELECT_PROJECT, RequestTag.REQUEST_TAG_FIND_SELECT_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i("----------" + ChannelChoiceFragment.class.getSimpleName() + "-----------" + result);
                        if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                            JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray("data");
                            SPUtils.put(getActivity(), SPKeys.KEY_PROJECT_FIND_LATEST_TAG, array.toString());
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
        loadMore();
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        ++page;
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_FIND_SELECT_PROJECT, RequestTag.REQUEST_TAG_FIND_SELECT_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i("----------" + ChannelChoiceFragment.class.getSimpleName() + "-----------" + result);
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

    /**
     *
     * @param p_id

    private void loadProjectDetail(String p_id) {
        progressDialog.show();
        L.i("-------p_id--------"+p_id);
        HashMap<String, String> param = new HashMap<>();
        param.put("id", p_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_DETAILS_PROJECT, RequestTag.REQUEST_DETAILS_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                        String result = o.toString();
                        L.i("---------------project details------"+result);
                        if (!TextUtils.isEmpty(result)){
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS){
                                JSONObject object = GsonUtil.pareJSONObject(result).optJSONObject(RequestCode.KEY_ARRAY);
                                ProjectDetail pd = GsonUtil.getInstance().fromJson(object.toString(),ProjectDetail.class);
                                Intent intent = new Intent(getActivity(), ProjectDetailsActivity.class);
                                intent.putExtra("obj",pd);
                                getActivity().startActivity(intent);
                            }
                        }else{
                            Toast.makeText(getActivity(),GsonUtil.pareMsg(result),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
    }

     */
}
