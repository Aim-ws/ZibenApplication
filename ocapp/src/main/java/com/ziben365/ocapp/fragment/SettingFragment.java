package com.ziben365.ocapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.ui.EditProfileActivity;
import com.ziben365.ocapp.ui.FeedbackActivity;
import com.ziben365.ocapp.ui.LoginActivity;
import com.ziben365.ocapp.util.ClearManager;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.StringUtil;

import java.io.File;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/31.
 * email  1956766863@qq.com
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    @InjectView(R.id.id_tv_version)
    TextView mTvVersion;
    @InjectView(R.id.id_tv_logout)
    TextView mTvLogout;
    @InjectView(R.id.id_edit_user_info)
    TextView mEditUserInfo;
    @InjectView(R.id.id_toggle_msg)
    ToggleButton mToggleMsg;
    @InjectView(R.id.id_toggle_letter_msg)
    ToggleButton mToggleLetterMsg;
    @InjectView(R.id.id_tv_score)
    TextView mTvScore;
    @InjectView(R.id.id_tv_feedback)
    TextView mTvFeedback;
    @InjectView(R.id.id_tv_clear_cache)
    TextView mTvClearCache;
    @InjectView(R.id.id_tv_about_us)
    TextView mTvAboutUs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTvVersion.setText(getResources().getText(R.string.setting_8) + " V" +
                StringUtil.getAppVersion(getActivity()));


        initEvent();
    }

    private void initEvent() {

        mEditUserInfo.setOnClickListener(this);
        mTvScore.setOnClickListener(this);
        mTvFeedback.setOnClickListener(this);
        mTvClearCache.setOnClickListener(this);
        mTvAboutUs.setOnClickListener(this);
        mTvVersion.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);

        boolean b = (boolean) SPUtils.get(getActivity(), SPKeys.KEY_USER_JPUSH_RECEIVER_TAG, true);
        if (b) mToggleMsg.setChecked(true);
        mToggleMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SPUtils.put(getActivity(), SPKeys.KEY_USER_JPUSH_RECEIVER_TAG, true);
                    if (JPushInterface.isPushStopped(DemoApplication.applicationContect)) {
                        JPushInterface.resumePush(DemoApplication.applicationContect);
                    }
                } else {
                    SPUtils.put(getActivity(), SPKeys.KEY_USER_JPUSH_RECEIVER_TAG, false);
                    if (!JPushInterface.isPushStopped(DemoApplication.applicationContect)) {
                        JPushInterface.stopPush(DemoApplication.applicationContect);
                    }
                }
            }
        });
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_edit_user_info:
                editUserInfo();
                break;
            case R.id.id_tv_score:
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.id_tv_feedback:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.id_tv_clear_cache:
                clearCache();
                break;
            case R.id.id_tv_about_us:

                break;
            case R.id.id_tv_version:

                break;
            case R.id.id_tv_logout:
                exitLogin();
                break;
        }
    }


    private void editUserInfo() {
        String uid = (String) SPUtils.get(getActivity(), SPKeys.KEY_USER_ID_TAG, "");
        if (TextUtils.isEmpty(uid)) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        }
    }

    private void exitLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                android.app.AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("退出提示");
        builder.setMessage("您确定要退出登录吗？");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SPUtils.removeUser(getActivity());
                SPUtils.put(getActivity(), SPKeys.KEY_USER_TOKEN_TAG, "");
                getActivity().sendBroadcast(new Intent(NotifyAction.ACTION_UPDATE_USER_INFO));
                JPushInterface.setAliasAndTags(getActivity(), "", null, new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {

                    }
                });
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 清理缓存
     */
    private void clearCache() {
        File cacheFile = getActivity().getExternalCacheDir();
        String cacheStr = ClearManager.getCacheSize(cacheFile);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.app_dialog);
        builder.setTitle("清理缓存");
        builder.setMessage("当前缓存为：" + cacheStr);
        builder.setPositiveButton("清理", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog d = builder.create();
        d.show();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
