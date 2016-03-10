package com.ziben365.ocapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsRecyclerAdapter;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.model.ProjectAlbum;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.widget.RoundedImageView;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/2/26.
 * email  1956766863@qq.com
 */
public class AlbumAdapter extends AbsRecyclerAdapter<ProjectAlbum> {


    public AlbumAdapter(Context context, List<ProjectAlbum> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.row_project_album, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        ProjectAlbum album = mData.get(position);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + album.logo + "!w100")
                .crossFade().centerCrop().crossFade().centerCrop().into(viewHolder.mPImage);
        viewHolder.mPName.setText(album.pname);
        viewHolder.mPDesc.setText(album.intro);
        viewHolder.mPLike.setText(album.praise);
        viewHolder.mPScan.setText(album.views);
        viewHolder.mPShare.setText(album.coins);
        viewHolder.mPComment.setText(album.comments);
        viewHolder.content.setOnClickListener(new OnItemClickListener(album));

    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mPImage;
        TextView mPName;
        TextView mPDesc;
        TextView mPShare;
        TextView mPComment;
        TextView mPLike;
        TextView mPScan;
        RelativeLayout content;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mPComment = (TextView) itemView.findViewById(R.id.id_p_comment);
            mPShare = (TextView) itemView.findViewById(R.id.id_p_share);
            mPScan = (TextView) itemView.findViewById(R.id.id_p_scan);
            mPLike = (TextView) itemView.findViewById(R.id.id_p_like);
            mPDesc = (TextView) itemView.findViewById(R.id.id_p_desc);
            mPName = (TextView) itemView.findViewById(R.id.id_p_name);
            mPImage = (RoundedImageView) itemView.findViewById(R.id.id_p_image);
            content = (RelativeLayout) itemView.findViewById(R.id.id_content);

        }
    }

    class OnItemClickListener implements View.OnClickListener {
        private ProjectAlbum album;

        public OnItemClickListener(ProjectAlbum album) {
            this.album = album;
        }

        @Override
        public void onClick(View v) {
            ProjectItemClickUtil.itemClickDetails((Activity) mContext, album.pid);
        }
    }
}
