package com.ziben365.ocapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.model.UserData;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.StringUtil;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyInterfaceImpl;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.util.request.VolleyRequestCallBack;
import com.ziben365.ocapp.view.AppProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/23.
 * email  1956766863@qq.com
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener, Handler.Callback {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_CODE = 0x100;
    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_editUsername)
    EditText mEditUsername;
    @InjectView(R.id.id_passWord)
    EditText mPassWord;
    @InjectView(R.id.id_btn_login)
    Button mBtnLogin;
    @InjectView(R.id.id_tv_register)
    TextView mTvRegister;
    @InjectView(R.id.id_tv_forget)
    TextView mTvForget;
    @InjectView(R.id.id_other_login)
    TextView mOtherLogin;
    @InjectView(R.id.id_image_qq)
    ImageView mImageQq;
    @InjectView(R.id.id_image_wechat)
    ImageView mImageWechat;
    @InjectView(R.id.id_image_sina)
    ImageView mImageSina;
    private String username, password;

    private Handler handler;

    private AppProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        ShareSDK.initSDK(getApplicationContext());
        handler = new Handler(this);
        progressDialog = new AppProgressDialog(this);

        //标题居中
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initEvent();

    }

    private void initEvent() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mImageQq.setOnClickListener(this);
        mImageSina.setOnClickListener(this);
        mImageWechat.setOnClickListener(this);
    }


    /**
     * 登录方法
     */
    private void login() {
        username = mEditUsername.getText().toString().trim();
        password = mPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, R.string.login_empty_username, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtil.isMobile(username)) {
            Toast.makeText(this, R.string.login_error_username, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.login_empty_password, Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        Map<String, String> param = new HashMap<>();
        param.put("mobile", username);
        param.put("password", StringUtil.md5(password + "ziben365"));
        VolleyRequest.volleyRequestPost(Link.USER_LOGIN, RequestTag.REQUEST_TAG_LOGIN, param,
                new VolleyInterfaceImpl(new VolleyRequestCallBack() {
                    @Override
                    public void onSuccess(Object o) {
                        L.i("------------------" + o.toString());
                        progressDialog.dismiss();
                        if (GsonUtil.pareCode(o.toString()) == RequestCode.SUCCESS) {
                            pareData(o.toString());
                        }
                        {
                            Toast.makeText(LoginActivity.this, GsonUtil.pareMsg(o.toString()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError volleyError) {
                        progressDialog.dismiss();
                    }
                }));
    }

    /**
     * 解析数据
     *
     * @param s
     */
    private void pareData(String s) {
        JSONObject object = GsonUtil.pareJSONObject(s).optJSONObject("data");
        L.i("--------object--------" + object.toString());
        UserData userData = GsonUtil.getInstance().fromJson(object.toString(), UserData.class);
        if (null == userData) {
            return;
        }
        JPushInterface.setAliasAndTags(this, userData.getUser_info().id, null, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                L.i("code----"+i+"---msg----"+s);
            }
        });
        L.i("--------token--------" + userData.token);
        SPUtils.put(this, SPKeys.KEY_USER_TOKEN_TAG, userData.token);
        SPUtils.saveUser(this, userData.user_info);
        //更新用户信息广播
        Intent intent = new Intent(NotifyAction.ACTION_UPDATE_USER_INFO);
        sendBroadcast(intent);
        //通知签到广播
        Intent loginIntent = new Intent(NotifyAction.ACTION_USER_SIGN);
        sendBroadcast(loginIntent);
        finish();
    }


    /**
     * 立即注册 点击监听
     *
     * @param view
     */
    public void onRegisterClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_image_qq:
                Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
                authorize(qq);
                break;
            case R.id.id_image_wechat:
                Platform wechat = ShareSDK.getPlatform(this, Wechat.NAME);
                authorize(wechat);

                break;
            case R.id.id_image_sina:
                Platform sina = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                authorize(sina);

                break;
        }
    }

    /**
     * 授权
     *
     * @param plat
     */
    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }

        plat.setPlatformActionListener(this);
        // true关闭SSO授权 ,false 打开SSO授权
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[]{platform.getName(), res};
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                // 取消授权
                Toast.makeText(LoginActivity.this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                // 授权失败
                Toast.makeText(LoginActivity.this, R.string.auth_error, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                // 授权成功
                Toast.makeText(LoginActivity.this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
                Object[] objs = (Object[]) msg.obj;
                String platform = (String) objs[0];
                L.i("platform:" + platform);

                Platform plat = null;
                if (Wechat.NAME.equals(platform)) {
                    plat = ShareSDK.getPlatform(platform);
                    thirdLogin(plat, "4");
                }
                if (QQ.NAME.equals(platform)) {
                    plat = ShareSDK.getPlatform(platform);
                    thirdLogin(plat, "1");
                }
                if (SinaWeibo.NAME.equals(platform)) {
                    plat = ShareSDK.getPlatform(platform);
                    thirdLogin(plat, "3");
                }

            }
            break;

        }
        return false;
    }

    /**
     * 微信 type = 4 QQ type = 1
     *
     * @param plat 2015-6-29 Administrator
     * @param type
     */
    private void thirdLogin(Platform plat, String type) {
        // TODO Auto-generated method stub
        if (null == plat) {
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            return;
        }
        /*RequestParams params = new RequestParams();
        params.addBodyParameter("uid", plat.getDb().getUserId());
        params.addBodyParameter("nickname", plat.getDb().getUserName());
        params.addBodyParameter("pic", plat.getDb().getUserIcon());
        params.addBodyParameter("type", type);
        UtilHttp.sendPost(ConstantUrl.LOGIN_OPEN, params, 0, this);*/
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("uid", plat.getDb().getUserId());
        param.put("nickname", plat.getDb().getUserName());
        param.put("pic", plat.getDb().getUserIcon());
        param.put("type", type);
        VolleyRequest.volleyRequestPost(Link.USER_OPEN_LOGIN,
                RequestTag.REQUEST_TAG_OPEN_LOGIN, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        L.i("------------------" + o.toString());
                        progressDialog.dismiss();
                        if (GsonUtil.pareCode(o.toString()) == RequestCode.SUCCESS) {
                            pareData(o.toString());
                        }
                        {
                            Toast.makeText(LoginActivity.this, GsonUtil.pareMsg(o.toString()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });
    }
}
