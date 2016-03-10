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
import com.ziben365.ocapp.model.ProjectChart;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.widget.RoundedImageView;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/31.
 * email  1956766863@qq.com
 */
public class ChannelChartsAdapter extends AbsRecyclerAdapter<ProjectChart> {
    private String[] headers = new String[]{"最受欢迎项目", "最具影响力项目", "最具潜力项目", "最具活力项目"};


    public ChannelChartsAdapter(Context context, List<ProjectChart> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChartsViewHolder(mInflater.inflate(R.layout.row_channel_charts, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChartsViewHolder viewHolder = (ChartsViewHolder) holder;
        ProjectChart currChart = mData.get(position);
        viewHolder.mRankingNum.setText("" + (position + 1));
        viewHolder.mPName.setText(currChart.pname);
        viewHolder.mPDescription.setText(currChart.intro);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + currChart.logo + "!w100").crossFade().centerCrop().into(viewHolder.mLogo);

        viewHolder.mHeaderHint.setVisibility(View.GONE);
        if (position == 0) {
            viewHolder.mHeaderHint.setVisibility(View.VISIBLE);
            viewHolder.mHeaderHint.setText(headers[0]);
        } else {
            ProjectChart preChart = mData.get(position - 1);
            if (preChart.type != currChart.type) {
                viewHolder.mHeaderHint.setVisibility(View.VISIBLE);
                viewHolder.mHeaderHint.setText(headers[currChart.type]);
            }
        }
        viewHolder.mItemLayout.setOnClickListener(new OnChartsClickListener(currChart));
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_channel_charts.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ChartsViewHolder extends RecyclerView.ViewHolder {
        TextView mHeaderHint;
        TextView mRankingNum;
        RoundedImageView mLogo;
        TextView mPName;
        TextView mPDescription;
        RelativeLayout mItemLayout;

        public ChartsViewHolder(View itemView) {
            super(itemView);
            mHeaderHint = (TextView) itemView.findViewById(R.id.id_header_hint);
            mRankingNum = (TextView) itemView.findViewById(R.id.id_ranking_num);
            mPName = (TextView) itemView.findViewById(R.id.id_p_name);
            mPDescription = (TextView) itemView.findViewById(R.id.id_p_description);
            mLogo = (RoundedImageView) itemView.findViewById(R.id.id_logo);
            mItemLayout = (RelativeLayout) itemView.findViewById(R.id.id_item_layout);
        }
    }

    private class OnChartsClickListener implements View.OnClickListener {
        private ProjectChart chart;

        public OnChartsClickListener(ProjectChart currChart) {
            this.chart = currChart;
        }

        @Override
        public void onClick(View v) {
            ProjectItemClickUtil.itemClickDetails((Activity) mContext, chart.pid);
        }
    }
}
