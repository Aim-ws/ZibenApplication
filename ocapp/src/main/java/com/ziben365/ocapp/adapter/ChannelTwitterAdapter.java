package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsRecyclerAdapter;
import com.ziben365.ocapp.model.ProjectTwitter;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.ui.PersonalCenterActivity;
import com.ziben365.ocapp.widget.CircleImageView;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/31.
 * email  1956766863@qq.com
 */
public class ChannelTwitterAdapter extends AbsRecyclerAdapter<ProjectTwitter> {


    public ChannelTwitterAdapter(Context context, List<ProjectTwitter> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TwitterViewHolder(mInflater.inflate(R.layout.row_channel_twitter, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TwitterViewHolder viewHolder = (TwitterViewHolder) holder;
        ProjectTwitter twitter = mData.get(position);
        viewHolder.mRankingNum.setText("" + (position + 1));
        viewHolder.mUserName.setText(TextUtils.isEmpty(twitter.nick_name) ? "匿名" : twitter.nick_name);
        viewHolder.mUserDescription.setText(twitter.area);
        viewHolder.mGoldNum.setText(twitter.week_coins);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + twitter.logo + "!w100")
                .crossFade().centerCrop().into(viewHolder.mAvatar);
        if ("1".equals(twitter.gender)) {
            viewHolder.mUserGender.setImageResource(R.mipmap.ic_p_twitter_m);
        }else{
            viewHolder.mUserGender.setImageResource(R.mipmap.ic_p_twitter_w);
        }
        viewHolder.mContainer.setOnClickListener(new OnItemClickListener(twitter.id));
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_channel_twitter.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class TwitterViewHolder extends RecyclerView.ViewHolder {
        TextView mRankingNum;
        CircleImageView mAvatar;
        TextView mGoldNum;
        TextView mUserName;
        TextView mUserDescription;
        LinearLayout mContainer;
        ImageView mUserGender;


        public TwitterViewHolder(View itemView) {
            super(itemView);
            mAvatar = (CircleImageView) itemView.findViewById(R.id.id_avatar);
            mGoldNum = (TextView) itemView.findViewById(R.id.id_gold_num);
            mUserName = (TextView) itemView.findViewById(R.id.id_user_name);
            mRankingNum = (TextView) itemView.findViewById(R.id.id_ranking_num);
            mUserDescription = (TextView) itemView.findViewById(R.id.id_user_city);
            mContainer = (LinearLayout) itemView.findViewById(R.id.id_container);
            mUserGender = (ImageView) itemView.findViewById(R.id.id_user_gender);
        }
    }

    class OnItemClickListener implements View.OnClickListener {

        private String id;

        public OnItemClickListener(String id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(id)) {
                return;
            }
            Intent intent = new Intent(mContext, PersonalCenterActivity.class);
            intent.putExtra(PersonalCenterActivity.KEY_PERSONAL_UID, id);
            mContext.startActivity(intent);
        }
    }
}
