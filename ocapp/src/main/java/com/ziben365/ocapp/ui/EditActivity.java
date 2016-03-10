package com.ziben365.ocapp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.util.ErrorDealUtil;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;

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
 */
public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.id_title)
    TextView mTitle;
    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_edit)
    EditText mEdit;
    @InjectView(R.id.id_btn_ok)
    Button mBtnOk;
    private int flag;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        flag = intent.getIntExtra("flag", 0);
        mTitle.setText("编辑" + title);
        mEdit.setHint(title);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (flag == EditProfileActivity.EDIT_EMAIL) {
            mEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBtnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String text = mEdit.getText().toString().trim();
        if (flag == EditProfileActivity.EDIT_NICK) {
            if (!TextUtils.isEmpty(text)) {
                checkNickName(text);
            } else {
                Toast.makeText(this, "请输入邮箱号", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!TextUtils.isEmpty(text)) {
                intent.putExtra("text", text);
                setResult(RESULT_OK, intent);
                onBackPressed();
            } else {
                Toast.makeText(this, "请输入" + title, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 检测用户昵称是否存在
     *
     * @param nick
     */
    private void checkNickName(final String nick) {
        final ProgressDialog pd = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage("正在检测用户昵称，请稍后...");
        pd.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("nick_name", nick);
        param.put("token", SPUtils.getToken(this));
        VolleyRequest.volleyRequestPost(Link.USER_CHECK_NCIK, RequestTag.REQUEST_TAG_CHECK_NICKNAME,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        pd.dismiss();
                        String result = o.toString();
                        L.i(result);
                        if (StringUtils.isNotEmpty(result)){
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS){
                                Intent intent = new Intent();
                                intent.putExtra("text", nick);
                                setResult(RESULT_OK, intent);
                                onBackPressed();
                            }
                            else{
                                ErrorDealUtil.dealError(EditActivity.this,GsonUtil.pareCode(result));
                            }
                            Toast.makeText(EditActivity.this,GsonUtil.pareMsg(result),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
}
