package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.fragment.CardListFragment;
import com.ziben365.ocapp.inter.OnItemProjectClickListener;
import com.ziben365.ocapp.inter.util.UserPersonalDealUtil;
import com.ziben365.ocapp.model.Project;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.util.StringUtil;
import com.ziben365.ocapp.widget.CircleImageView;
import com.ziben365.ocapp.widget.RoundedImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/14.
 * email  1956766863@qq.com
 */
public class ChannelChoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Project> entities;
    private LayoutInflater inflater;
    private Context mContext;
    private int mChannel;
    private int[] mColors = new int[]{R.mipmap.white, R.mipmap.blue, R.mipmap.gray, R.mipmap.dark, R.mipmap.green};
    private OnItemProjectClickListener mListener;

    public ChannelChoiceAdapter(Context context, List<Project> entities, int channel) {
        this.entities = entities;
        this.mContext = context;
        this.mChannel = channel;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemProjectClickListener(OnItemProjectClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        initViewData(holder, position);

    }


    @Override
    public int getItemCount() {
        return entities.size();
    }


    /**
     * 填充数据
     *
     * @param holder
     * @param position
     */
    private void initViewData(RecyclerView.ViewHolder holder, int position) {
        Project project = entities.get(position);
        switch (mChannel) {
            case CardListFragment.CHANNEL_CHOICE:
                ChoiceViewHolder choiceViewHolder = (ChoiceViewHolder) holder;
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + project.banner + "!w480").crossFade().centerCrop().into(choiceViewHolder.mImageView);
                if (!TextUtils.isEmpty(project.avatar)) {
                    Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + project.avatar + "!w100").crossFade().centerCrop().into(choiceViewHolder.mLogo);
                } else {
                    choiceViewHolder.mLogo.setImageResource(R.mipmap.ic_default_avatar);
                }
                choiceViewHolder.mName.setText(project.pname);
                choiceViewHolder.mRanking.setText("No、" + (position + 1));
                if (!TextUtils.isEmpty(project.add_time)) {
                    choiceViewHolder.mDate.setText(StringUtil.formatTime(Long.parseLong(project.add_time)));
                }
                choiceViewHolder.mRecommendPerson.setText((TextUtils.isEmpty(project.nick_name) ? "推荐人" : project.nick_name) + "说:");
                choiceViewHolder.mDescription.setText(project.intro);
                choiceViewHolder.mRecommendReason.setText(project.recreason);
                choiceViewHolder.mRootView.setOnClickListener(new ItemClickListener(position));
                choiceViewHolder.mRecommendReason.setMaxLines(5);
                choiceViewHolder.mRecommendReason.setEllipsize(TextUtils.TruncateAt.END);
                choiceViewHolder.mLogo.setOnClickListener(new OnUserClickListener(project));
                break;

            case CardListFragment.CHANNEL_UP_TO_DATE:
                NewestViewHolder newestViewHolder = (NewestViewHolder) holder;
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + project.logo + "!w100")
                        .crossFade().centerCrop().into(newestViewHolder.mLogo);
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + project.banner + "!w480")
                        .crossFade().centerCrop().into(newestViewHolder.mImageView);
                if (StringUtils.isNotEmpty(project.avatar)) {
                    Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + project.avatar + "!w50")
                            .crossFade().centerCrop().into(newestViewHolder.mAvatar);
                } else {
                    newestViewHolder.mAvatar.setImageResource(R.mipmap.ic_default_avatar);
                }
                newestViewHolder.mName.setText(project.pname);
                newestViewHolder.mDescription.setText(project.intro);
                newestViewHolder.mViewNum.setText(project.views);
                newestViewHolder.mLikeNum.setText(project.praise);
                newestViewHolder.mCommentNum.setText(project.comments);
                newestViewHolder.mUserName.setText(TextUtils.isEmpty(project.nick_name) ? "匿名" : project.nick_name);
                newestViewHolder.mRootView.setOnClickListener(new ItemClickListener(position));
                break;
            default:

                break;
        }
    }


    /**
     * 根据不同的channel获取不同viewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View convertView;
        switch (mChannel) {
            case CardListFragment.CHANNEL_CHOICE:
                convertView = inflater.inflate(R.layout.row_channel_chiose, null);
                viewHolder = new ChoiceViewHolder(convertView);

                break;
            case CardListFragment.CHANNEL_UP_TO_DATE:
                convertView = inflater.inflate(R.layout.row_channel_newest, null);
                viewHolder = new NewestViewHolder(convertView);
                break;
            default:

                break;

        }
        return viewHolder;
    }


    class ItemClickListener implements View.OnClickListener {
        private int index;

        public ItemClickListener(int position) {
            index = position;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemProjectClick(entities.get(index).pid);
            }
//            Intent intent = new Intent(mContext, ProjectDetailsActivity.class);
//            intent.putExtra("bitmap", mColors[index % mColors.length]);
//            intent.putExtra("obj",entities.get(index));
//            mContext.startActivity(intent);
        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_channel_chiose.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ChoiceViewHolder extends RecyclerView.ViewHolder {
        TextView mRanking;
        ImageView mImageView;
        TextView mName;
        CircleImageView mLogo;
        TextView mRecommendPerson;
        TextView mRecommendReason;
        TextView mDate;
        TextView mDescription;
        RelativeLayout mRootView;

        public ChoiceViewHolder(View itemView) {
            super(itemView);
            mRanking = (TextView) itemView.findViewById(R.id.id_ranking);
            mDate = (TextView) itemView.findViewById(R.id.id_date);
            mName = (TextView) itemView.findViewById(R.id.id_name);
            mRecommendReason = (TextView) itemView.findViewById(R.id.id_recommend_reason);
            mRecommendPerson = (TextView) itemView.findViewById(R.id.id_recommend_person);
            mDescription = (TextView) itemView.findViewById(R.id.id_description);
            mImageView = (ImageView) itemView.findViewById(R.id.id_imageView);
            mLogo = (CircleImageView) itemView.findViewById(R.id.id_logo);
            mRootView = (RelativeLayout) itemView.findViewById(R.id.id_root_view);

        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_channel_newest.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class NewestViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mLogo;
        TextView mName;
        ImageView mImageView;
        CircleImageView mAvatar;
        TextView mCommentNum;
        TextView mLikeNum;
        TextView mViewNum;
        TextView mUserName;
        TextView mDescription;
        FrameLayout mRootView;


        public NewestViewHolder(View itemView) {
            super(itemView);
            mLogo = (RoundedImageView) itemView.findViewById(R.id.id_logo);
            mAvatar = (CircleImageView) itemView.findViewById(R.id.id_avatar);
            mImageView = (ImageView) itemView.findViewById(R.id.id_imageView);
            mName = (TextView) itemView.findViewById(R.id.id_name);
            mCommentNum = (TextView) itemView.findViewById(R.id.id_comment_num);
            mLikeNum = (TextView) itemView.findViewById(R.id.id_like_num);
            mViewNum = (TextView) itemView.findViewById(R.id.id_view_num);
            mUserName = (TextView) itemView.findViewById(R.id.id_user_name);
            mDescription = (TextView) itemView.findViewById(R.id.id_description);
            mRootView = (FrameLayout) itemView.findViewById(R.id.id_root_view);
        }
    }


    private class OnUserClickListener implements View.OnClickListener {
        private Project project;

        public OnUserClickListener(Project project) {
            this.project = project;
        }

        @Override
        public void onClick(View v) {
            UserPersonalDealUtil.dealUser(mContext, project.user_id);
        }
    }
}
