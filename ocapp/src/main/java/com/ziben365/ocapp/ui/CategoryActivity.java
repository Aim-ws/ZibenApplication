package com.ziben365.ocapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.CategoryAdapter;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.model.ProjectSelect;
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
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/22.
 * email  1956766863@qq.com
 */
public class CategoryActivity extends BaseActivity implements RefreshRecyclerView.LoadingListener {


    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_recyclerView)
    RefreshRecyclerView mRecyclerView;

    private List<ProjectSelect> cardList;
    private CategoryAdapter adapter;

    private AppProgressDialog progressDialog;
    private String industry;
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.inject(this);

        initToolbar();
    }

    private void initToolbar() {
        progressDialog = new AppProgressDialog(this);

        industry = getIntent().getStringExtra("title");
        mToolbar.setTitle(industry);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initData();
    }

    private void initData() {
        cardList = new ArrayList<>();
        adapter = new CategoryAdapter(this, cardList);
        adapter.setHeaderEnbled(false);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
        applyData();

    }

    private void applyData() {
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("industry", industry);
        VolleyRequest.volleyRequestPost(Link.PERSONAL_GET_SEL_INDUS,
                RequestTag.REQUEST_TAG_GET_SEL_INDUS, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                        String result = o.toString();
                        L.i("-------------"+result);
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
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        page = 1;
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("industry", industry);
        VolleyRequest.volleyRequestPost(Link.PERSONAL_GET_SEL_INDUS,
                RequestTag.REQUEST_TAG_GET_SEL_INDUS, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
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
        ++page;
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("industry", industry);
        VolleyRequest.volleyRequestPost(Link.PERSONAL_GET_SEL_INDUS,
                RequestTag.REQUEST_TAG_GET_SEL_INDUS, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
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
    protected void onStop() {
        ProjectItemClickUtil.dismiss();
        super.onStop();
    }
}
