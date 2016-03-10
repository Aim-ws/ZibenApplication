package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsBaseAdapter;
import com.ziben365.ocapp.model.ProjectComment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2016/1/5.
 * email  1956766863@qq.com
 */
public class PReplyCommentAdapter extends AbsBaseAdapter<ProjectComment> {
    public PReplyCommentAdapter(Context context, List<ProjectComment> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_p_detail_reply_comment, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load("http://f.hiphotos.baidu.com/zhidao/pic/item/95eef01f3a292df5d55cf" +
                "82fbd315c6034a87368.jpg").asBitmap().into(viewHolder.mAvatar);
        viewHolder.mUserName.setText("功夫宝宝");
        viewHolder.mReplyComment.setText("邮箱呢？？？？？");
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_p_detail_reply_comment.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @InjectView(R.id.id_avatar)
        ImageView mAvatar;
        @InjectView(R.id.id_user_name)
        TextView mUserName;
        @InjectView(R.id.id_reply_comment)
        TextView mReplyComment;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
