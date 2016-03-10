package com.ziben365.ocapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/2/24.
 * email  1956766863@qq.com
 */
public class FeedbackActivity extends BaseActivity {

    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_edit)
    EditText mEdit;
    @InjectView(R.id.id_btn_ok)
    Button mBtnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initEvent();
    }

    private void initEvent() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    /**
     * 提交意见
     */
    private void submit() {
        String msg = mEdit.getText().toString().trim();
        String token = SPUtils.getToken(this);
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("content",msg);
        VolleyRequest.volleyRequestPost(Link.PERSONAL_FEEDBACK,
                RequestTag.REQUEST_TAG_FEEDBACK,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)){
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS){
                                Toast.makeText(FeedbackActivity.this,GsonUtil.pareMsg(result),Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
}
