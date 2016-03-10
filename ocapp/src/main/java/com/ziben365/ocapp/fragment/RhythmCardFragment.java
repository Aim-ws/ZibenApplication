package com.ziben365.ocapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsBaseFragment;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.inter.util.UserPersonalDealUtil;
import com.ziben365.ocapp.model.ProjectCard;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2015/12/15.
 * email  1956766863@qq.com
 */
public class RhythmCardFragment extends AbsBaseFragment {
    @InjectView(R.id.id_ranking)
    TextView mRanking;
    @InjectView(R.id.id_date)
    TextView mDate;
    @InjectView(R.id.id_imageView)
    ImageView mImageView;
    @InjectView(R.id.id_name)
    TextView mName;
    @InjectView(R.id.id_description)
    TextView mDescription;
    @InjectView(R.id.id_logo)
    CircleImageView mLogo;
    @InjectView(R.id.id_recommend_person)
    TextView mRecommendPerson;
    @InjectView(R.id.id_recommend_reason)
    TextView mRecommendReason;
    @InjectView(R.id.id_root_view)
    RelativeLayout mRootView;
    private ProjectCard entity;

    public static Fragment getInstance(ProjectCard entity) {
        RhythmCardFragment instance = new RhythmCardFragment();
        Bundle b = new Bundle();
        b.putSerializable("obj", entity);
        instance.setArguments(b);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.row_channel_chiose, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        entity = (ProjectCard) bundle.getSerializable("obj");
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + entity.banner).crossFade().centerCrop().into(mImageView);
        mRanking.setText("No.1");
        mDate.setText("2015-12-31");
        mRecommendPerson.setText((TextUtils.isEmpty(entity.nick_name) ? "推荐人" : entity.nick_name) + "说:");

        mRanking.setVisibility(View.GONE);
        mDate.setVisibility(View.GONE);
        mRootView.setBackgroundColor(Color.TRANSPARENT);

        if (!TextUtils.isEmpty(entity.avatar)) {
            Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + entity.avatar + "!w100").crossFade().centerCrop().into(mLogo);
        } else {
            mLogo.setImageResource(R.mipmap.ic_default_avatar);
        }
        mName.setText(entity.pname);
        mDescription.setText(entity.intro);
        mRecommendReason.setText(entity.recreason);
        mRecommendReason.setEllipsize(TextUtils.TruncateAt.END);
        mRecommendReason.setMaxLines(6);
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), ProjectDetailsActivity.class);
                intent.putExtra("obj","");
                getActivity().startActivity(intent);*/
                ProjectItemClickUtil.itemClickDetails(getActivity(), entity.pid);
            }
        });
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPersonalDealUtil.dealUser(getActivity(), entity.user_id);
            }
        });
    }

    @Override
    public void onStop() {
        ProjectItemClickUtil.dismiss();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
