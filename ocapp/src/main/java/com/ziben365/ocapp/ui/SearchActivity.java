package com.ziben365.ocapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.SearchAdapter;
import com.ziben365.ocapp.adapter.SearchRecordAdapter;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.model.ProjectSearch;
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
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/12.
 * email  1956766863@qq.com
 */
public class SearchActivity extends BaseActivity implements RefreshRecyclerView.LoadingListener, SearchRecordAdapter.OnItemRecordClickListener {
    private static final String FILE_DATA = "app_search_data";
    private static final String KEY_SIZE = "_size";
    private static final String KEY_NAME = "__name";


    @InjectView(R.id.id_btn_search)
    TextView mBtnSearch;
    @InjectView(R.id.id_recyclerView)
    RefreshRecyclerView mRecyclerView;
    @InjectView(R.id.id_all_recyclerView)
    RecyclerView mAllRecyclerView;
    @InjectView(R.id.id_clear)
    TextView mClear;
    @InjectView(R.id.id_my_recyclerView)
    RecyclerView mMyRecyclerView;
    @InjectView(R.id.id_record_layout)
    LinearLayout mRecordLayout;
    @InjectView(R.id.id_search)
    EditText mSearch;

    private List<String> allRecordList = new ArrayList<>();
    private SearchRecordAdapter localRecordAdapter, allRecordAdapter;

    private AppProgressDialog progressDialog;

    private int page = 1;
    private String keyword;


    /**
     * 本地搜索记录
     */
    private ArrayList<String> localRecordList = new ArrayList<>();

    private ArrayList<ProjectSearch> list = new ArrayList<>();
    private SearchAdapter searchAdapter;

    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);

        progressDialog = new AppProgressDialog(this);

        list.clear();
        searchAdapter = new SearchAdapter(this, list);
        mRecyclerView.setAdapter(searchAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setLoadingListener(this);
        mAllRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        localRecordAdapter = new SearchRecordAdapter(this, localRecordList);
        allRecordAdapter = new SearchRecordAdapter(this, allRecordList);
        localRecordAdapter.setOnItemRecordClickListener(this);
        allRecordAdapter.setOnItemRecordClickListener(this);
        mAllRecyclerView.setAdapter(allRecordAdapter);
        mMyRecyclerView.setAdapter(localRecordAdapter);

        sp = getSharedPreferences(FILE_DATA, MODE_PRIVATE);
        updateLocalRecord();

        allRecordAdapter.notifyDataSetChanged();

        applyAllRecord();

        initEvent();
    }

    private void applyAllRecord() {
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("p", "1");
        VolleyRequest.volleyRequestPost(Link.PROJECT_SEARCH_HISTORY,
                RequestTag.REQUEST_TAG_SEARCH_HISTORY, param,
                new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        progressDialog.dismiss();
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<String> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<String>>() {
                                        }.getType());
                                allRecordList.clear();
                                allRecordList.addAll(data);
                                allRecordAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void updateLocalRecord() {
        localRecordList.clear();
        int size = sp.getInt(KEY_SIZE, 0);
        for (int i = 0; i < size; i++) {
            localRecordList.add(sp.getString(KEY_NAME + i, null));
        }
        localRecordAdapter.notifyDataSetChanged();
    }

    private void initEvent() {
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode()
                        == KeyEvent.KEYCODE_ENTER)) {
                    keyword = mSearch.getText().toString().trim();
                    if (TextUtils.isEmpty(keyword)) {
                        Toast.makeText(SearchActivity.this, "请输入关键词", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    search();
                    return true;
                }
                return false;
            }
        });
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = mSearch.getText().toString().trim();

                if (TextUtils.isEmpty(keyword)) {
                    Toast.makeText(SearchActivity.this, "请输入关键词", Toast.LENGTH_SHORT).show();
                    return;
                }
                search();
            }
        });
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localRecordList.clear();
                localRecordAdapter.notifyDataSetChanged();
                sp.edit().clear().commit();
            }
        });
    }

    /**
     * 搜索
     *
     * @param
     */
    private void search() {
        page = 1;
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("keywords", keyword);
        param.put("p", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_SEARCH,
                RequestTag.REQUEST_TAG_SEARCH, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        saveRecord(keyword);
                        progressDialog.dismiss();
                        mSearch.setText("");
                        String result = o.toString();
                        L.i("------result------" + result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectSearch> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectSearch>>() {
                                        }.getType());
                                list.clear();
                                list.addAll(data);
                                searchAdapter.notifyDataSetChanged();
                            }
                        }
                        mRecordLayout.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void saveRecord(String msg) {
        localRecordList.add(0, msg);
        HashSet<String> hs = new HashSet<>(localRecordList);
        localRecordList.clear();
        localRecordList.addAll(hs);
        int count = localRecordList.size();
        for (int i = 0; i < count; i++) {
            sp.edit().putString(KEY_NAME+i,localRecordList.get(i)).commit();
        }
        sp.edit().putInt(KEY_SIZE, localRecordList.size()).commit();
        updateLocalRecord();
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        page = 1;
        HashMap<String, String> param = new HashMap<>();
        param.put("keywords", keyword);
        param.put("p", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_SEARCH,
                RequestTag.REQUEST_TAG_SEARCH, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i("------result------" + result);
                        mRecordLayout.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectSearch> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectSearch>>() {
                                        }.getType());
                                list.clear();
                                list.addAll(data);
                                searchAdapter.notifyDataSetChanged();
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
        param.put("keywords", keyword);
        param.put("p", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_SEARCH,
                RequestTag.REQUEST_TAG_SEARCH, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i("------result------" + result);
                        mRecordLayout.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectSearch> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectSearch>>() {
                                        }.getType());
                                list.clear();
                                list.addAll(data);
                                searchAdapter.notifyDataSetChanged();
                                mRecyclerView.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                });
    }

    @Override
    public void onItemRecordClick(String keywords) {
        mSearch.setText(keywords);
        this.keyword = keywords;
        search();
    }


}
