package com.ziben365.ocapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsRecyclerAdapter;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.fragment.ChannelPersonalFragment;
import com.ziben365.ocapp.fragment.PersonalFragment;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.inter.util.UserMessageDealUtil;
import com.ziben365.ocapp.model.UserLike;
import com.ziben365.ocapp.model.UserMessage;
import com.ziben365.ocapp.model.UserRecommend;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.view.GoldRuleDialog;
import com.ziben365.ocapp.widget.RoundedImageView;

import java.util.HashMap;
import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/3/1.
 * email  1956766863@qq.com
 */
public class ChannelPersonalAdapter extends AbsRecyclerAdapter {
    private int channel;

    public ChannelPersonalAdapter(Context context, List data, int channel) {
        super(context, data);
        this.channel = channel;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (channel) {
            case PersonalFragment.CHANNEL_RECOMMEND:
                viewHolder = new RecommendViewHolder(mInflater.inflate(R.layout.row_personal_recommend, null));
                break;
            case PersonalFragment.CHANNEL_LIKE:
                viewHolder = new LikeViewHolder(mInflater.inflate(R.layout.row_personal_like, null));
                break;
            case PersonalFragment.CHANNEL_MESSAGE:
                viewHolder = new MessageViewHolder(mInflater.inflate(R.layout.row_personal_message, null));
                break;
            case PersonalFragment.CHANNEL_DOLLAR:
                viewHolder = new GoldViewHolder(mInflater.inflate(R.layout.row_personal_gold, null));
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (channel) {
            case PersonalFragment.CHANNEL_RECOMMEND:
                updateRecommend(holder, position);
                break;
            case PersonalFragment.CHANNEL_LIKE:
                updateLike(holder, position);
                break;
            case PersonalFragment.CHANNEL_MESSAGE:
                updateMessage(holder, position);
                break;
            case PersonalFragment.CHANNEL_DOLLAR:
                updateMyGold(holder, position);
                break;
        }
    }

    private void updateMyGold(RecyclerView.ViewHolder holder, int position) {
        ChannelPersonalFragment.MyGold mygold = (ChannelPersonalFragment.MyGold) mData.get(position);
        GoldViewHolder viewHolder = (GoldViewHolder) holder;
        viewHolder.mRankingNum.setText(mygold.pos);

        SpannableStringBuilder totalSsb = new SpannableStringBuilder();
        totalSsb.append("金币总数:");
        SpannableString totalSs = new SpannableString(mygold.coins);
        ForegroundColorSpan totalFcs = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_gold));
        totalSs.setSpan(totalFcs, 0, mygold.coins.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        totalSsb.append(totalSs).append("枚");
        viewHolder.mTotalGold.setText(totalSsb);

        SpannableStringBuilder daySsb = new SpannableStringBuilder();
        daySsb.append("今日收入:");
        SpannableString daySs = new SpannableString(mygold.today_income);
        ForegroundColorSpan dayFcs = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_gold));
        daySs.setSpan(dayFcs, 0, mygold.today_income.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        daySsb.append(daySs).append("枚");
        viewHolder.mDayGold.setText(daySsb);

        viewHolder.mRuleGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GoldRuleDialog(mContext).show();
            }
        });

    }

    /**
     * @param rank
     * @param total_gold
     * @param day_gold
     */
    private void updateMyGold(String rank, String total_gold, String day_gold) {


    }

    private void updateMessage(RecyclerView.ViewHolder holder, int position) {
        MessageViewHolder viewHolder = (MessageViewHolder) holder;
        UserMessage message = (UserMessage) mData.get(position);
        //type-----0  系统消息notice
        //type-----1  项目消息project
        //status ----- 0 未读, 1 已读
        if ("0".equals(message.type)) {
            viewHolder.mPLabelImage.setImageResource(R.mipmap.ic_p_notice_normal);
        } else {
            viewHolder.mPLabelImage.setImageResource(R.mipmap.ic_p_message_normal);
        }
        if ("0".equals(message.status)) {
            viewHolder.mPMessage.setTextColor(mContext.getResources().getColor(R.color.color_text_33));
        } else {
            viewHolder.mPMessage.setTextColor(mContext.getResources().getColor(R.color.color_text_99));
        }
        viewHolder.mPMessage.setText(message.title + " " + message.content);
        viewHolder.mContent.setOnClickListener(new OnMessageItemClickListener(message, position));
    }

    private void updateLike(RecyclerView.ViewHolder holder, int position) {
        LikeViewHolder viewHolder = (LikeViewHolder) holder;
        UserLike like = (UserLike) mData.get(position);
        viewHolder.mPName.setText(like.pname);
        viewHolder.mPDesc.setText(like.intro);
        viewHolder.mPScan.setText(like.views);
        viewHolder.mPLike.setText(like.praise);
        viewHolder.mPComment.setText(like.comments);
        viewHolder.mPShare.setText(like.praise);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + like.banner + "!w100")
                .crossFade().centerCrop().into(viewHolder.mPImage);
        viewHolder.mContent.setOnClickListener(new OnItemClickListener(like.pid));
    }

    private void updateRecommend(RecyclerView.ViewHolder holder, int position) {
        RecommendViewHolder viewHolder = (RecommendViewHolder) holder;
        UserRecommend recommend = (UserRecommend) mData.get(position);
        viewHolder.mPName.setText(recommend.pname);
        viewHolder.mPScan.setText(recommend.views);
        viewHolder.mPLike.setText(recommend.praise);
        viewHolder.mPComment.setText(recommend.comments);
        viewHolder.mPDollar.setText(recommend.praise);
        viewHolder.mPDesc.setText(recommend.intro);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + recommend.logo + "!w100")
                .crossFade().centerCrop().into(viewHolder.mPLogo);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + recommend.banner + "!w480")
                .crossFade().centerCrop().into(viewHolder.mPImage);
        viewHolder.mContent.setOnClickListener(new OnItemClickListener(recommend.pid));
    }

    private class RecommendViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView mPLogo;
        TextView mPName;
        TextView mPDesc;
        RelativeLayout mPHeader;
        ImageView mPImage;
        TextView mPDollar;
        TextView mPComment;
        TextView mPLike;
        TextView mPScan;
        LinearLayout mContent;

        public RecommendViewHolder(View itemView) {
            super(itemView);
            mPComment = (TextView) itemView.findViewById(R.id.id_p_comment);
            mPDollar = (TextView) itemView.findViewById(R.id.id_p_dollar);
            mPScan = (TextView) itemView.findViewById(R.id.id_p_scan);
            mPLike = (TextView) itemView.findViewById(R.id.id_p_like);
            mPDesc = (TextView) itemView.findViewById(R.id.id_p_desc);
            mPName = (TextView) itemView.findViewById(R.id.id_p_name);
            mPLogo = (RoundedImageView) itemView.findViewById(R.id.id_p_logo);
            mPImage = (ImageView) itemView.findViewById(R.id.id_p_image);
            mPHeader = (RelativeLayout) itemView.findViewById(R.id.id_p_header);
            mContent = (LinearLayout) itemView.findViewById(R.id.id_content);
        }
    }

    private class LikeViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mPImage;
        TextView mPName;
        TextView mPDesc;
        TextView mPShare;
        TextView mPComment;
        TextView mPLike;
        TextView mPScan;
        RelativeLayout mContent;

        public LikeViewHolder(View itemView) {
            super(itemView);
            mPComment = (TextView) itemView.findViewById(R.id.id_p_comment);
            mPShare = (TextView) itemView.findViewById(R.id.id_p_share);
            mPScan = (TextView) itemView.findViewById(R.id.id_p_scan);
            mPLike = (TextView) itemView.findViewById(R.id.id_p_like);
            mPDesc = (TextView) itemView.findViewById(R.id.id_p_desc);
            mPName = (TextView) itemView.findViewById(R.id.id_p_name);
            mPImage = (RoundedImageView) itemView.findViewById(R.id.id_p_image);
            mContent = (RelativeLayout) itemView.findViewById(R.id.id_content);
        }
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView mPLabelImage;
        TextView mPMessage;
        LinearLayout mContent;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mPLabelImage = (ImageView) itemView.findViewById(R.id.id_p_label_image);
            mPMessage = (TextView) itemView.findViewById(R.id.id_p_message);
            mContent = (LinearLayout) itemView.findViewById(R.id.id_content);
        }
    }


    class OnItemClickListener implements View.OnClickListener {

        private String p_id;

        public OnItemClickListener(String pid) {
            this.p_id = pid;
        }

        @Override
        public void onClick(View v) {
            if (channel == PersonalFragment.CHANNEL_RECOMMEND) {
                ProjectItemClickUtil.itemClickDetails((Activity) mContext, p_id);
            }
            if (channel == PersonalFragment.CHANNEL_LIKE) {
                ProjectItemClickUtil.itemClickDetails((Activity) mContext, p_id, true);
            }
        }
    }

    private class GoldViewHolder extends RecyclerView.ViewHolder {
        TextView mRankingNum;
        TextView mTotalGold;
        TextView mDayGold;
        TextView mRuleGold;

        public GoldViewHolder(View itemView) {
            super(itemView);
            mRankingNum = (TextView) itemView.findViewById(R.id.id_ranking_num);
            mTotalGold = (TextView) itemView.findViewById(R.id.id_total_gold);
            mDayGold = (TextView) itemView.findViewById(R.id.id_day_gold);
            mRuleGold = (TextView) itemView.findViewById(R.id.id_rule_gold);
        }
    }

    private class OnMessageItemClickListener implements View.OnClickListener {
        private UserMessage message;
        private int position;

        public OnMessageItemClickListener(UserMessage message, int position) {
            this.message = message;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
//            UserMessageDealUtil.dealMessage(mContext, message, true);
            dealMessage(mContext, message, position);
        }
    }


    /**
     * 改变消息状态
     *
     * @param context  上下文环境
     * @param message  message对象
     * @param position
     */
    private void changeMessageStatus(final Context context, UserMessage message, final int position) {
        String token = SPUtils.getToken(context);
        if (TextUtils.isEmpty(token)) return;
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("msg_id", message.id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_MSG_INFO,
                RequestTag.REQUEST_TAG_USER_MSG_INFO, param,
                new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                /*Intent intent = new Intent(NotifyAction.ACTION_USER_MESSAGE_CHANGE);
                                context.sendBroadcast(intent);*/
                                ((UserMessage) mData.get(position)).status = "1";
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }


    /**
     * @param context
     * @param message
     * @param position
     */
    public void dealMessage(Context context, UserMessage message, int position) {
        if (TextUtils.isEmpty(message.action)) return;
        if (TextUtils.isEmpty(message.action_value)) return;
        if (UserMessageDealUtil.ACTION_PROJECT.equals(message.action)) {
            ProjectItemClickUtil.itemClickDetails((Activity) context, message.action_value);
        }
        if (UserMessageDealUtil.ACTION_MESSAGE.equals(message.action)) {

        }
        changeMessageStatus(mContext, message, position);


    }

}
