package com.ziben365.ocapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsRecyclerAdapter;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.model.ProjectSelect;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.ui.CategoryActivity;
import com.ziben365.ocapp.widget.RoundedImageView;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/31.
 * email  1956766863@qq.com
 */
public class CategoryAdapter extends AbsRecyclerAdapter<ProjectSelect> {
    private boolean isHeader;

    public CategoryAdapter(Context context, List<ProjectSelect> data) {
        super(context, data);
    }


    public void setHeaderEnbled(boolean isHeader) {
        this.isHeader = isHeader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassifyViewHolder(mInflater.inflate(R.layout.row_fragment_category, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ClassifyViewHolder viewHolder = (ClassifyViewHolder) holder;
        ProjectSelect album = mData.get(position);
        viewHolder.mPName.setText(album.pname);
        viewHolder.mPDescription.setText(album.intro);
        viewHolder.mRowCategoryHeader.setVisibility(View.GONE);
        if (isHeader) {
            if (position == 0) {
                viewHolder.mRowCategoryHeader.setVisibility(View.VISIBLE);
                viewHolder.mRowCategoryName.setText(album.ins_name);
                viewHolder.mRowCategoryHeader.setOnClickListener(new OnMoreClickListener(album));
            } else {
                ProjectSelect pre_Album = mData.get(position - 1);
                if (!pre_Album.ins_name.equals(album.ins_name)) {
                    viewHolder.mRowCategoryHeader.setVisibility(View.VISIBLE);
                    viewHolder.mRowCategoryName.setText(album.ins_name);
                    viewHolder.mRowCategoryHeader.setOnClickListener(new OnMoreClickListener(album));
                }
            }

        }
        if (isHeader) {
            viewHolder.mLine.setVisibility(View.GONE);
        } else {
            viewHolder.mLine.setVisibility(View.VISIBLE);
        }
        viewHolder.mContentLayout.setOnClickListener(new OnItemClickListener(album));
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + album.logo + "!w100").crossFade().centerCrop().into(viewHolder.mLogo);
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_fragment_category.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ClassifyViewHolder extends RecyclerView.ViewHolder {
        TextView mRowCategoryName;
        LinearLayout mRowCategoryHeader;
        RoundedImageView mLogo;
        ImageView mCategoryNext;
        TextView mPName;
        TextView mPDescription;
        RelativeLayout mContentLayout;
        View mLine;


        public ClassifyViewHolder(View itemView) {
            super(itemView);
            mRowCategoryName = (TextView) itemView.findViewById(R.id.id_row_category_name);
            mPDescription = (TextView) itemView.findViewById(R.id.id_p_description);
            mPName = (TextView) itemView.findViewById(R.id.id_p_name);
            mRowCategoryHeader = (LinearLayout) itemView.findViewById(R.id.id_row_category_header);
            mCategoryNext = (ImageView) itemView.findViewById(R.id.id_category_next);
            mLogo = (RoundedImageView) itemView.findViewById(R.id.id_logo);
            mContentLayout = (RelativeLayout) itemView.findViewById(R.id.id_content_layout);
            mLine = itemView.findViewById(R.id.id_line);
        }
    }

    class OnMoreClickListener implements View.OnClickListener {
        private ProjectSelect album;

        public OnMoreClickListener(ProjectSelect album) {
            this.album = album;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, CategoryActivity.class);
            intent.putExtra("title", album.ins_name);
            mContext.startActivity(intent);
        }
    }

    class OnItemClickListener implements View.OnClickListener {
        private ProjectSelect album;

        public OnItemClickListener(ProjectSelect album) {
            this.album = album;
        }

        @Override
        public void onClick(View v) {
            ProjectItemClickUtil.itemClickDetails((Activity) mContext, album.pid);
        }
    }
}
