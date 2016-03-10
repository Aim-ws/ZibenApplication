package com.ziben365.ocapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.model.ProjectTag;
import com.ziben365.ocapp.model.Tag;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.request.VolleyJSONObjectInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.widget.flowlayout.FlowLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/13.
 * email  1956766863@qq.com
 */
public class TagActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.id_complete)
    TextView mComplete;
    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_flow_layout)
    FlowLayout mFlowLayout;
    private List<Tag> mTags;
    private int count;
    private int select = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        ButterKnife.inject(this);

        count = getIntent().getIntExtra("count", 4);
        L.i("----count-----"+count);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mComplete.setOnClickListener(this);

        initData();
    }

    private void initData() {
        String tagStr = (String) SPUtils.get(DemoApplication.applicationContect, SPKeys.KEY_PROJECT_TAG, "");
        if (!TextUtils.isEmpty(tagStr)) {
            ProjectTag projectTag = GsonUtil.getInstance().fromJson(tagStr, ProjectTag.class);
            mTags = new ArrayList<>();
            for (int i = 0; i < projectTag.tags.length; i++) {
                mTags.add(new Tag(0, projectTag.tags[i], false));
            }
            addTag();
        } else {
            requestTag();
        }


    }

    public void requestTag() {
        VolleyRequest.volleyJSONObjectMsgpack(Link.PROJECT_TAGS, RequestTag.REQUEST_TAG_TAG,
                new VolleyJSONObjectInterface() {
                    @Override
                    public void onSuccess(Object t) {
                        JSONObject object = (JSONObject) t;
                        L.i("----------------------------" + object);
                        int code = GsonUtil.pareCode(object);
                        if (code == RequestCode.SUCCESS) {
                            JSONObject array = object.optJSONObject("data");
                            SPUtils.put(DemoApplication.applicationContect,
                                    SPKeys.KEY_PROJECT_TAG,
                                    array.toString());
                            initData();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    private void addTag() {
        select = 1;
        LayoutInflater inflater = LayoutInflater.from(this);
        mFlowLayout.removeAllViews();
        for (int i = 0; i < mTags.size(); i++) {
            View itemView = inflater.inflate(R.layout.row_tag, null);
            TextView tagView = (TextView) itemView.findViewById(R.id.id_tag_name);
            Tag t = mTags.get(i);
            if (t.checked) {
                select++;
                itemView.setBackgroundResource(R.drawable.shape_tag_pressed);
            } else {
                itemView.setBackgroundResource(R.drawable.shape_tag_normal);
            }
            tagView.setText(t.tagName);
            tagView.setOnClickListener(new TagViewClickListener(i, mTags.get(i)));
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 15;
            lp.leftMargin = 15;
            lp.topMargin = 10;
            lp.bottomMargin = 10;
            itemView.setLayoutParams(lp);
            mFlowLayout.addView(itemView, lp);
        }
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> tagNames = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < mTags.size(); i++) {
            if (mTags.get(i).checked) {
                tagNames.add(mTags.get(i).getTagName());
                ids.add(mTags.get(i).getId());
            }
        }

        Intent intent = new Intent();
        intent.putStringArrayListExtra("tagName", tagNames);
        intent.putIntegerArrayListExtra("tagId", ids);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    /**
     * 标签点击事件
     */
    class TagViewClickListener implements View.OnClickListener {
        int position;
        Tag mTag;

        public TagViewClickListener(int i, Tag tag) {
            this.position = i;
            this.mTag = tag;
        }

        @Override
        public void onClick(View v) {
            L.i("----------select------" + select);
            if (position < mTags.size()) {
                if (mTags.get(position).checked) {
                    mTags.get(position).setChecked(false);
                } else {
                    if (select > count) {
                        Toast.makeText(TagActivity.this, "最多只能选择" + count + "个标签", Toast.LENGTH_SHORT).show();
                    } else {
                        mTags.get(position).setChecked(true);
                    }
                }
            }
            addTag();
        }
    }
}
