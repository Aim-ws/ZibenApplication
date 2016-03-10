package com.ziben365.ocapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsRecyclerAdapter;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.model.ProjectSearch;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.widget.RoundedImageView;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/3/1.
 * email  1956766863@qq.com
 */
public class SearchAdapter extends AbsRecyclerAdapter<ProjectSearch> {


    public SearchAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchViewHolder(mInflater.inflate(R.layout.row_project_search, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchViewHolder viewHolder = (SearchViewHolder) holder;
        ProjectSearch entity = mData.get(position);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + entity.banner + "!w100")
                .crossFade().centerCrop().into(viewHolder.mPImage);
        viewHolder.mPDesc.setText(entity.intro);
        viewHolder.mPContent.setTag(entity.pdesc);
        viewHolder.mPName.setText(entity.pname);
        viewHolder.mPContent.setOnClickListener(new OnItemClickListener(entity));
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mPImage;
        ImageView mPNext;
        TextView mPName;
        TextView mPDesc;
        RelativeLayout mPContent;

        public SearchViewHolder(View itemView) {
            super(itemView);
            mPImage = (RoundedImageView) itemView.findViewById(R.id.id_p_image);
            mPNext = (ImageView) itemView.findViewById(R.id.id_p_next);
            mPName = (TextView) itemView.findViewById(R.id.id_p_name);
            mPDesc = (TextView) itemView.findViewById(R.id.id_p_desc);
            mPContent = (RelativeLayout) itemView.findViewById(R.id.id_p_content);
        }
    }

    class OnItemClickListener implements View.OnClickListener {

        private ProjectSearch search;

        public OnItemClickListener(ProjectSearch entity) {
            this.search = entity;
        }

        @Override
        public void onClick(View v) {
            ProjectItemClickUtil.itemClickDetails((Activity) mContext, search.pid);
        }
    }
}
