package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.inter.OnCommentContentClickSpan;
import com.ziben365.ocapp.inter.OnCommentNameClickSpan;
import com.ziben365.ocapp.inter.util.UserPersonalDealUtil;
import com.ziben365.ocapp.model.ProjectComment;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.util.StringUtil;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2016/1/11.
 * email  1956766863@qq.com
 */
public class PCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnReplyCommentClickListener replyCommentClickListener;
    private OnReplyUserClickListener replyUserClickListener;
    private OnCommentPraiseClickListener praiseClickListener;
    private LayoutInflater mInflater;
    private List<ProjectComment> mData;
    private Context mContext;

    public PCommentAdapter(Context context, List<ProjectComment> data) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mData = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.row_p_detail_comment, null);
        return new CommentViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentViewHolder viewHolder = (CommentViewHolder) holder;
        ProjectComment comment = mData.get(position);
        if (!TextUtils.isEmpty(comment.logo)) {
            Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + comment.logo + "!w100")
                    .crossFade().centerCrop().into(viewHolder.mAvatar);
        }
        viewHolder.mUserName.setText(TextUtils.isEmpty(comment.nick_name) ? "匿名" : comment.nick_name);
        viewHolder.mPDate.setText(StringUtil.formatTime(Long.parseLong(comment.add_time), "yyyy-MM-dd HH:mm:ss"));
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (!TextUtils.isEmpty(comment.rev_relname)) {
            String str = "@" + comment.rev_relname;
            SpannableString ss = new SpannableString(str);
            OnCommentNameClickSpan clickSpan = new OnCommentNameClickSpan(mContext, comment, replyUserClickListener);
            ss.setSpan(clickSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(ss);
        }
        ssb.append(" ");
        SpannableString ss1 = new SpannableString(comment.content);
        OnCommentContentClickSpan contentClickSpan = new OnCommentContentClickSpan(mContext, comment, replyCommentClickListener);
        ss1.setSpan(contentClickSpan, 0, comment.content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss1);
        viewHolder.mPComment.setText(ssb);
        viewHolder.mPraise.setText(comment.num);
        viewHolder.mPReply.setOnClickListener(new RelyCommentClickListener(comment));
        viewHolder.mPComment.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.mPraise.setOnClickListener(new PraiseCommentClickListener(comment, position));
        viewHolder.mAvatar.setOnClickListener(new onUserClickListener(comment.user_id));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_p_detail_comment.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView mAvatar;
        TextView mPraise;
        TextView mUserName;
        TextView mPComment;
        TextView mPReply;
        TextView mPDate;
//        RelativeLayout mLayoutDate;


        public CommentViewHolder(View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.id_avatar);
//            mLayoutDate = (RelativeLayout) itemView.findViewById(R.id.id_layout_date);
            mPraise = (TextView) itemView.findViewById(R.id.id_praise);
            mUserName = (TextView) itemView.findViewById(R.id.id_user_name);
            mPComment = (TextView) itemView.findViewById(R.id.id_p_comment);
            mPReply = (TextView) itemView.findViewById(R.id.id_p_reply);
            mPDate = (TextView) itemView.findViewById(R.id.id_p_date);
        }
    }

/*

    static class FooterHolderView extends RecyclerView.ViewHolder {
        ImageView imageView;

        public FooterHolderView(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.foot_image);
            AnimationDrawable ad = (AnimationDrawable) imageView.getDrawable();
            ad.start();
        }
    }

*/

    class RelyCommentClickListener implements View.OnClickListener {
        private ProjectComment c;


        public RelyCommentClickListener(ProjectComment comment) {
            this.c = comment;
        }

        @Override
        public void onClick(View v) {
            //回复评论
            if (replyCommentClickListener != null) {
                replyCommentClickListener.onReplyCommentClick(v, c);
            }
        }
    }

    class PraiseCommentClickListener implements View.OnClickListener {

        private ProjectComment p_comment;
        private int position;

        public PraiseCommentClickListener(ProjectComment comment, int position) {
            this.p_comment = comment;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (praiseClickListener != null) {
                praiseClickListener.onCommentPraiseClick(v, p_comment, position);
            }
        }
    }

    public void setOnReplyCommentClickListener(OnReplyCommentClickListener clickListener) {
        this.replyCommentClickListener = clickListener;
    }

    public void setOnReplyUserClickListener(OnReplyUserClickListener listener) {
        this.replyUserClickListener = listener;
    }

    public void setOnCommentPraiseClickListener(OnCommentPraiseClickListener listener) {
        this.praiseClickListener = listener;
    }

    public interface OnReplyCommentClickListener {
        void onReplyCommentClick(View v, ProjectComment comment);
    }

    public interface OnReplyUserClickListener {
        void onReplyUserClick(View v, ProjectComment comment);
    }

    public interface OnCommentPraiseClickListener {
        void onCommentPraiseClick(View v, ProjectComment comment, int position);
    }

    private class onUserClickListener implements View.OnClickListener {
        private String user_id;

        public onUserClickListener(String user_id) {
            this.user_id = user_id;
        }

        @Override
        public void onClick(View v) {
            UserPersonalDealUtil.dealUser(mContext, user_id);
        }
    }
}
