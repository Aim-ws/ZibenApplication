package com.ziben365.ocapp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.util.ChoiceCityUtils;
import com.ziben365.ocapp.util.ErrorDealUtil;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.TakePhotoUitl;
import com.ziben365.ocapp.util.UploadUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.widget.CircleImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/11.
 * email  1956766863@qq.com
 * <p/>
 * 编辑个人资料
 * edit profile information
 */
public class EditProfileActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_logo)
    CircleImageView mLogo;
    @InjectView(R.id.id_user_nick)
    TextView mUserNick;
    @InjectView(R.id.id_user_name)
    TextView mUserName;
    @InjectView(R.id.rb_man)
    RadioButton rbMan;
    @InjectView(R.id.rb_woman)
    RadioButton rbWoman;
    @InjectView(R.id.rg_gender)
    RadioGroup rgGender;
    @InjectView(R.id.id_user_city)
    TextView mUserCity;
    @InjectView(R.id.id_user_mobile)
    TextView mUserMobile;
    @InjectView(R.id.id_user_email)
    TextView mUserEmail;
    @InjectView(R.id.id_user_register)
    TextView mUserRegister;

    public static final int EDIT_NICK = 0x52;
    public static final int EDIT_NAME = 0x53;
    public static final int EDIT_EMAIL = 0x54;
    @InjectView(R.id.id_complete)
    TextView mComplete;

    private String u_logo, u_nick, u_name, u_city, u_gender, u_mobile, u_email, u_reg_time;
    private String u_province = "";
    private String logoPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.inject(this);

        initToolbar();
        initEvent();
        initView();

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

    private void initEvent() {
        rgGender.setOnCheckedChangeListener(this);
        mUserCity.setOnClickListener(this);
        mLogo.setOnClickListener(this);
        mUserNick.setOnClickListener(this);
        mUserEmail.setOnClickListener(this);
        mUserName.setOnClickListener(this);
        mComplete.setOnClickListener(this);

    }

    private void initView() {
        u_logo = (String) SPUtils.get(this, SPKeys.KEY_USER_IMAGE_TAG, "");
        u_name = (String) SPUtils.get(this, SPKeys.KEY_USER_NAME_TAG, "");
        u_nick = (String) SPUtils.get(this, SPKeys.KEY_USER_NICK_TAG, "");
        u_gender = (String) SPUtils.get(this, SPKeys.KEY_USER_GENDER_TAG, "");
        u_city = (String) SPUtils.get(this, SPKeys.KEY_USER_AREA_TAG, "");
        u_email = (String) SPUtils.get(this, SPKeys.KEY_USER_EMAIL_TAG, "");
        u_reg_time = (String) SPUtils.get(this, SPKeys.KEY_USER_REG_TIME_TAG, "");
        u_mobile = (String) SPUtils.get(this, SPKeys.KEY_USER_MOBILE_TAG, "");
        if (StringUtils.isNotEmpty(u_logo)) {
            if (u_logo.contains("http://")) {
                Glide.with(DemoApplication.applicationContect).load(u_logo)
                        .crossFade().centerCrop().into(mLogo);
            } else {
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + u_logo + "!w100")
                        .crossFade().centerCrop().into(mLogo);
            }
        }
        mUserName.setText(u_name);
        mUserNick.setText(u_nick);
        mUserRegister.setText(u_reg_time);
        mUserCity.setText(u_city);
        mUserMobile.setText(u_mobile);
        mUserEmail.setText(u_email);
        if ("0".endsWith(u_gender)) {
            rbMan.setChecked(true);
        }
        if ("1".endsWith(u_gender)) {
            rbWoman.setChecked(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TakePhotoUitl.RESULT_REQUEST_PICK_PHOTO:
                    TakePhotoUitl.cutOutPhoto(EditProfileActivity.this, Uri.fromFile(TakePhotoUitl.sdcardTempFile));
                    break;
                case TakePhotoUitl.RESULT_REQUEST_PHOTO:
                    logoPath = TakePhotoUitl.sdcardTempFile.getAbsolutePath();
                    TakePhotoUitl.sendToNext(mLogo, data);

                    break;
                case TakePhotoUitl.RESULT_REQUEST_SELECT_SINGLE:
                    Bitmap bitmap = BitmapFactory.decodeFile(TakePhotoUitl.sdcardTempFile.getAbsolutePath());
                    logoPath = TakePhotoUitl.sdcardTempFile.getAbsolutePath();
                    mLogo.setImageBitmap(bitmap);
                    break;
                case EDIT_NICK:
                    mUserNick.setText(data.getStringExtra("text"));
                    break;
                case EDIT_NAME:
                    mUserName.setText(data.getStringExtra("text"));
                    break;
                case EDIT_EMAIL:
                    mUserEmail.setText(data.getStringExtra("text"));
                    break;
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_man:
                u_gender = "0";
                break;
            case R.id.rb_woman:
                u_gender = "1";
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_user_city:
                showCityDialog();
                break;
            case R.id.id_logo:
                TakePhotoUitl.showPictureDialog(EditProfileActivity.this);
                break;
            case R.id.id_user_name:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("title", "真实姓名");
                intent.putExtra("flag", EDIT_NAME);
                startActivityForResult(intent, EDIT_NAME);
                break;
            case R.id.id_user_nick:
                Intent intent1 = new Intent(this, EditActivity.class);
                intent1.putExtra("title", "昵称");
                intent1.putExtra("flag", EDIT_NICK);
                startActivityForResult(intent1, EDIT_NICK);
                break;
            case R.id.id_user_email:
                Intent intent2 = new Intent(this, EditActivity.class);
                intent2.putExtra("title", "邮箱");
                intent2.putExtra("flag", EDIT_EMAIL);
                startActivityForResult(intent2, EDIT_EMAIL);
                break;
            case R.id.id_complete:
                complete();
                break;
        }
    }

    /**
     * 完成
     */
    private void complete() {
        u_name = mUserName.getText().toString().trim();
        u_nick = mUserNick.getText().toString().trim();
        u_city = mUserCity.getText().toString().trim();
        if (TextUtils.isEmpty(logoPath) && TextUtils.isEmpty(u_logo)) {
            Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(u_name)) {
            Toast.makeText(this, "请输入真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(u_nick)) {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(u_gender)) {
            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(u_city)) {
            Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(logoPath)) {
            submitLogo();
        } else {
            submitUserData();
        }

    }

    /**
     * 上传logo头像
     */
    private void submitLogo() {
        final ProgressDialog progressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("正在上传用户头像");
        progressDialog.show();
        //提交logo
        UploadUtils.getInstance().clearArray();
        UploadUtils.getInstance().uploadImage(TakePhotoUitl.sdcardTempFile.getAbsolutePath(),
                QiNiuConfig.getToken(),
                new UploadUtils.OnUploadImageListener() {
                    @Override
                    public void onSuccess(String fileUrl) {
                        progressDialog.dismiss();
                        u_logo = UploadUtils.getInstance().getmImages().get(0);
                        submitUserData();
                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                    }

                    @Override
                    public void onProgress(String s, double progress) {
                        progressDialog.setProgress((int) progress);
                    }
                });
    }

    /**
     * 提交用户数据
     */
    private void submitUserData() {
        final ProgressDialog progressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("正在提交数据，请稍后...");
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("logo", u_logo);
        param.put("real_name", u_name);
        param.put("nick_name", u_nick);
        param.put("gender", u_gender);
        param.put("area", u_city);
        param.put("province", u_province);
        param.put("token", SPUtils.getToken(this));

        VolleyRequest.volleyRequestPost(Link.USER_PERFECT_INFO, RequestTag.REQUEST_TAG_USER_INFO,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        L.i(o.toString());
                        progressDialog.dismiss();
                        String result = o.toString();
                        if (StringUtils.isNotEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                success();

                            } else {
                                ErrorDealUtil.dealError(EditProfileActivity.this, GsonUtil.pareCode(o.toString()));
                            }
                            Toast.makeText(EditProfileActivity.this, GsonUtil.pareMsg(result),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

    }

    private void success() {
        SPUtils.put(this, SPKeys.KEY_USER_IMAGE_TAG, u_logo);
        SPUtils.put(this, SPKeys.KEY_USER_NAME_TAG, u_name);
        SPUtils.put(this, SPKeys.KEY_USER_NICK_TAG, u_nick);
        SPUtils.put(this, SPKeys.KEY_USER_GENDER_TAG, u_gender);
        SPUtils.put(this, SPKeys.KEY_USER_AREA_TAG, u_city);
        Intent intent = new Intent(NotifyAction.ACTION_UPDATE_USER_INFO);
        sendBroadcast(intent);
        onBackPressed();
    }

    private void showCityDialog() {
        ChoiceCityUtils ccUtils = new ChoiceCityUtils(this);
        ccUtils.setOnUpdateSelectCityListener(new ChoiceCityUtils.OnUpdateSelectCityListener() {
            @Override
            public void onUpdateCity(String province, String city) {
                u_province = province;
                u_city = city;
                mUserCity.setText(province + " " + city);
            }
        });
    }


}
