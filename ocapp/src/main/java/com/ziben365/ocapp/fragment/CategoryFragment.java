package com.ziben365.ocapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.CategoryAdapter;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.model.ProjectSelect;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.view.AppProgressDialog;
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
 */
public class CategoryFragment extends Fragment implements RefreshRecyclerView.LoadingListener {


    @InjectView(R.id.id_recyclerView)
    RefreshRecyclerView mRecyclerView;
    private List<ProjectSelect> cardList;
    private CategoryAdapter adapter;
    private AppProgressDialog progressDialog;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressDialog = new AppProgressDialog(getActivity());

        cardList = new ArrayList<>();
        adapter = new CategoryAdapter(getActivity(), cardList);
        adapter.setHeaderEnbled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        applyData();

    }

    private void applyData() {
        progressDialog.show();
        HashMap<String,String> param = new HashMap<>();
        param.put("p",String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PERSONAL_SEL_CATE,
                RequestTag.REQUEST_TAG_SEL_CATE,param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectSelect> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectSelect>>() {
                                        }.getType());
                                cardList.clear();
                                cardList.addAll(data);
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
        HashMap<String,String> param = new HashMap<>();
        param.put("p",String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PERSONAL_SEL_CATE,
                RequestTag.REQUEST_TAG_SEL_CATE,param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                cardList.clear();
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectSelect> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectSelect>>() {
                                        }.getType());
                                cardList.addAll(data);
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
    public void onLoadMore() {
        loadMore();
    }

    private void loadMore() {
        ++ page;
        HashMap<String,String> param = new HashMap<>();
        param.put("p",String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PERSONAL_SEL_CATE,
                RequestTag.REQUEST_TAG_SEL_CATE,param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectSelect> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectSelect>>() {
                                        }.getType());
                                cardList.addAll(data);
                                adapter.notifyDataSetChanged();
                                mRecyclerView.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                });
    }
}
