package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsRecyclerAdapter;
import com.ziben365.ocapp.model.ProjectCollection;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.ui.AlbumActivity;
import com.ziben365.ocapp.widget.RoundedImageView;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/30.
 * email  1956766863@qq.com
 * <p/>
 * 合集适配器
 */
public class ChannelAlbumAdapter extends AbsRecyclerAdapter<ProjectCollection> {
    public ChannelAlbumAdapter(Context context, List<ProjectCollection> data) {
        super(context, data);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectionViewHolder(mInflater.inflate(R.layout.row_channel_collection, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CollectionViewHolder viewHolder = (CollectionViewHolder) holder;
        ProjectCollection collection = mData.get(position);
        viewHolder.mDescription.setText(collection.desc);
        viewHolder.mSpecialName.setText(collection.name);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + collection.img).crossFade().centerCrop()
                .into(viewHolder.mImageView);
        viewHolder.mRootView.setOnClickListener(new OnItemClickListener(collection));
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_channel_collection.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class CollectionViewHolder extends RecyclerView.ViewHolder {
        TextView mSpecialName;
        RoundedImageView mImageView;
        View mViewMasking;
        TextView mDescription;
        LinearLayout mRootView;


        public CollectionViewHolder(View itemView) {
            super(itemView);
            mSpecialName = (TextView) itemView.findViewById(R.id.id_special_name);
            mImageView = (RoundedImageView) itemView.findViewById(R.id.id_imageView);
            mViewMasking = itemView.findViewById(R.id.id_view_masking);
            mDescription = (TextView) itemView.findViewById(R.id.id_description);
            mRootView = (LinearLayout) itemView.findViewById(R.id.id_root_view);
        }
    }


    class OnItemClickListener implements View.OnClickListener {
        private ProjectCollection projectCollection;

        public OnItemClickListener(ProjectCollection collection) {
            this.projectCollection = collection;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, AlbumActivity.class);
            intent.putExtra("url_logo", projectCollection.img);
            intent.putExtra("title", projectCollection.name);
            intent.putExtra("id", projectCollection.id);
            mContext.startActivity(intent);
        }
    }
}
