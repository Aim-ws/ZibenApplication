package com.ziben365.ocapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.PPraiseAdapter;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.model.ProjectDetail;
import com.ziben365.ocapp.util.GsonUtil;
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
 * on 2016/3/7.
 * email  1956766863@qq.com
 */
public class ProjectPraiseActivity extends BaseActivity implements RefreshRecyclerView.LoadingListener {
    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_recyclerView)
    RefreshRecyclerView mRecyclerView;

    private AppProgressDialog progressDialog;
    private ArrayList<ProjectDetail.ProjectPraise> praises = new ArrayList<>();
    private PPraiseAdapter adapter;

    private static final int STATE_REFRESH = 0x11;
    private static final int STATE_LOAD = 0x12;

    private String p_id;
    private int page;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_praise);
        ButterKnife.inject(this);

        p_id = getIntent().getStringExtra("p_id");

        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void initRecyclerView() {
        praises.clear();
        adapter = new PPraiseAdapter(this, praises);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setLoadingListener(this);

        progressDialog = new AppProgressDialog(this);
        page = 1;
        refresh(true, STATE_REFRESH);

    }

    private void refresh(boolean b, final int state) {
        if (b) progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("id", p_id);
        VolleyRequest.volleyRequestPost("", "", param, new VolleyInterface() {
            @Override
            public void onSuccess(Object o) {
                String result = o.toString();
                progressDialog.dismiss();
                if (!TextUtils.isEmpty(result)) {
                    if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                        JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                        ArrayList<ProjectDetail.ProjectPraise> data = GsonUtil.getInstance().fromJson(
                                array.toString(), new TypeToken<ArrayList<ProjectDetail.ProjectPraise>>() {
                                }.getType());
                        if (state == STATE_REFRESH) {
                            praises.clear();
                            praises.addAll(data);
                            adapter.notifyDataSetChanged();
                            mRecyclerView.refreshComplete();
                        }
                        if (state == STATE_LOAD) {
                            praises.addAll(data);
                            adapter.notifyDataSetChanged();
                            mRecyclerView.loadMoreComplete();
                        }
                    } else {
                        Toast.makeText(ProjectPraiseActivity.this, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        refresh(false, STATE_REFRESH);
    }

    @Override
    public void onLoadMore() {
        ++page;
        refresh(false, STATE_LOAD);
    }
}
