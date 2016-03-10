package com.ziben365.ocapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.AlbumAdapter;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.model.ProjectAlbum;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
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
 * on 2016/2/26.
 * email  1956766863@qq.com
 * <p/>
 * 每个专辑的列表
 */
public class AlbumActivity extends BaseActivity implements RefreshRecyclerView.LoadingListener {

    @InjectView(R.id.back_icon)
    ImageView backIcon;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @InjectView(R.id.id_refresh_recyclerView)
    RefreshRecyclerView mRefreshRecyclerView;

    private AppProgressDialog progressDialog;

    private List<ProjectAlbum> albumList = new ArrayList<>();
    private AlbumAdapter adapter;
    private String name, url_img,co_id;
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.inject(this);

        name = getIntent().getStringExtra("title");
        co_id = getIntent().getStringExtra("id");
        url_img = getIntent().getStringExtra("url_logo");


        toolbar.setTitle(name);
        collapsingToolbarLayout.setTitle("");
        collapsingToolbarLayout.setTitleEnabled(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + url_img + "!w300")
                .crossFade().centerCrop().into(backIcon);

        albumList.clear();
        adapter = new AlbumAdapter(this, albumList);
        mRefreshRecyclerView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRefreshRecyclerView.setLayoutManager(llm);
        mRefreshRecyclerView.setPullRefreshEnabled(false);
        mRefreshRecyclerView.setLoadingMoreEnabled(true);
        mRefreshRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRefreshRecyclerView.setLoadingListener(this);

        progressDialog = new AppProgressDialog(this);

        applyData();


    }

    private void applyData() {
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("co_id", co_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_GET_COL_MORE,
                RequestTag.REQUEST_TAG_GET_COL_MORE, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectAlbum> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectAlbum>>() {
                                        }.getType());
                                albumList.addAll(data);
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
        param.put("co_id", co_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_GET_COL_MORE,
                RequestTag.REQUEST_TAG_GET_COL_MORE, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectAlbum> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectAlbum>>() {
                                        }.getType());
                                albumList.clear();
                                albumList.addAll(data);
                                adapter.notifyDataSetChanged();
                                mRefreshRecyclerView.refreshComplete();
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
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("co_id", co_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_GET_COL_MORE,
                RequestTag.REQUEST_TAG_GET_COL_MORE, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectAlbum> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectAlbum>>() {
                                        }.getType());
                                albumList.addAll(data);
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
    protected void onStop() {
        ProjectItemClickUtil.dismiss();
        super.onStop();
    }
}
