package com.ziben365.ocapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.StringUtil;
import com.ziben365.ocapp.util.TimeCount;
import com.ziben365.ocapp.util.WindowUtil;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/23.
 * email  1956766863@qq.com
 * register user
 */
public class RegisterActivity extends BaseActivity {

    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_edit_mobile)
    EditText mEditMobile;
    @InjectView(R.id.id_btn_yz)
    TextView mBtnYz;
    @InjectView(R.id.id_edit_yz_code)
    EditText mEditYzCode;
    @InjectView(R.id.id_edit_password)
    EditText mEditPassword;
    @InjectView(R.id.id_checkBox)
    CheckBox mCheckBox;
    @InjectView(R.id.id_text_input_layout_mobile)
    TextInputLayout mTextInputLayoutMobile;
    @InjectView(R.id.id_btn_register)
    Button mBtnRegister;

    private TimeCount timeCount;
    private String mobile, pass, code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        initEvent();

    }

    private void initEvent() {
        timeCount = new TimeCount(60 * 1000, 1000);
        timeCount.setButton(mBtnYz);
        mBtnYz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCode();
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        mobile = mEditMobile.getText().toString().trim();
        code = mEditYzCode.getText().toString().trim();
        pass = mEditPassword.getText().toString().trim();
        if (StringUtils.isEmpty(mobile)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtil.isMobile(mobile)) {
            Toast.makeText(this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(pass)) {
            Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(this, "密码小于6位，重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length() > 16) {
            Toast.makeText(this, "密码大于16位，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mCheckBox.isChecked()){
            Toast.makeText(this,"请勾选服务条款!",Toast.LENGTH_SHORT).show();
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("mobile", mobile);
        param.put("password", pass);
        param.put("code", code);
        VolleyRequest.volleyRequestPost(Link.USER_REGISTER, RequestTag.REQUEST_TAG_REGISTER,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        L.i(""+o.toString());
                        String result = o.toString();
                        if (StringUtils.isNotEmpty(result)){
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS){
                                onBackPressed();
                            }
                            Toast.makeText(RegisterActivity.this,GsonUtil.pareMsg(result),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

    }

    /**
     * 请求验证码
     */
    private void requestCode() {
        mobile = mEditMobile.getText().toString().trim();
        if (StringUtils.isEmpty(mobile)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtil.isMobile(mobile)) {
            Toast.makeText(this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("mobile", mobile);
        timeCount.start();
        VolleyRequest.volleyRequestPost(Link.USER_MOBILE_RANDNUM,
                RequestTag.REQUEST_TAG_VERIFY_CODE,
                new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        L.i("object:" + o.toString());
                        Toast.makeText(RegisterActivity.this, GsonUtil.pareMsg(o.toString()),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    private void initView() {
        mEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    mTextInputLayoutMobile.setError("密码不能小于6位");
                    mTextInputLayoutMobile.setErrorEnabled(true);
                } else {
                    mTextInputLayoutMobile.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtil.isMobile(s.toString())) {
                    mEditMobile.setError("请输入正确的手机号");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
