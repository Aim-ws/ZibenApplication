package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsRecyclerAdapter;
import com.ziben365.ocapp.inter.util.UserPersonalDealUtil;
import com.ziben365.ocapp.model.ProjectDetail;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.widget.CircleImageView;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/3/7.
 * email  1956766863@qq.com
 */
public class PPraiseAdapter extends AbsRecyclerAdapter<ProjectDetail.ProjectPraise> {


    public PPraiseAdapter(Context context, List<ProjectDetail.ProjectPraise> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PraiseViewHolder(mInflater.inflate(R.layout.row_p_detail_praise, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProjectDetail.ProjectPraise praise = mData.get(position);
        PraiseViewHolder viewHolder = (PraiseViewHolder) holder;
        viewHolder.mPName.setText(TextUtils.isEmpty(praise.nick_name) ? "匿名" : praise.nick_name);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + praise.logo + "!w100")
                .crossFade().into(viewHolder.mAvatar);
        viewHolder.mContent.setOnClickListener(new OnPraiseClickListener(praise));
    }


    static class PraiseViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mAvatar;
        TextView mPName;
        RelativeLayout mContent;

        public PraiseViewHolder(View itemView) {
            super(itemView);
            mAvatar = (CircleImageView) itemView.findViewById(R.id.id_avatar);
            mPName = (TextView) itemView.findViewById(R.id.id_p_name);
            mContent = (RelativeLayout) itemView.findViewById(R.id.id_content);
        }
    }

    private class OnPraiseClickListener implements View.OnClickListener {
        ProjectDetail.ProjectPraise praise;

        public OnPraiseClickListener(ProjectDetail.ProjectPraise praise) {
            this.praise = praise;
        }

        @Override
        public void onClick(View v) {
            UserPersonalDealUtil.dealUser(mContext, praise.id);
        }
    }
}
