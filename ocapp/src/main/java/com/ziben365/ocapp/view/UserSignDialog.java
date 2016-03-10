package com.ziben365.ocapp.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.ScreenUtils;
import com.ziben365.ocapp.util.StringUtil;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;

import java.util.HashMap;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/3/3.
 * email  1956766863@qq.com
 */
public class UserSignDialog extends Dialog {
    private LayoutInflater inflater;
    private View convertView;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView goldTextView;
    private TextView nextGoldTextView;
    private TextView btnTextView;
    private int sign_num;


    public UserSignDialog(Context context) {
        this(context, R.style.myDialogTheme);
    }

    public UserSignDialog(Context context, int themeResId) {
        super(context, themeResId);
        inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.dialog_user_sign, null);
        setContentView(convertView);
        setCancelable(true);

        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ScreenUtils.getWindowScreenWidth(context) - 100;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        initView(context);
    }

    private void initView(final Context context) {
        titleTextView = (TextView) convertView.findViewById(R.id.id_user_sign_title);
        dateTextView = (TextView) convertView.findViewById(R.id.id_user_sign_date);
        goldTextView = (TextView) convertView.findViewById(R.id.id_user_sign_gold);
        nextGoldTextView = (TextView) convertView.findViewById(R.id.id_user_sign_next);
        btnTextView = (TextView) convertView.findViewById(R.id.id_user_sign_btn);

        titleTextView.setText("今日签到");
        if (sign_num == 0) {
            dateTextView.setText("每日签到送金币");
        } else {
            dateTextView.setText("连续签到" + sign_num + "天");
        }
        goldTextView.setText("获得金币" + (sign_num + 1) + "枚");
        nextGoldTextView.setText("明日连续签到可获得" + (sign_num + 2) + "枚");
        btnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sign(context);
                dismiss();
            }
        });

    }

    private void sign(final Context context) {
        String token = SPUtils.getToken(DemoApplication.applicationContect);
        if (TextUtils.isEmpty(token)) return;
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_SIGN,
                RequestTag.REQUEST_TAG_USER_SIGN, param,
                new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(result);
                        if (TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                long time = System.currentTimeMillis();
                                String currentTime = StringUtil.formatSystemTime(time, "yyyy-MM-dd");
                                L.i("================" + currentTime);
                                String u_id = (String) SPUtils.get(context, SPKeys.KEY_USER_ID_TAG, "");
                                if (!TextUtils.isEmpty(u_id)) {
                                    SPUtils.put(context, u_id, currentTime);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    public void setSign_num(int sign_num) {
        this.sign_num = sign_num;
    }
}
