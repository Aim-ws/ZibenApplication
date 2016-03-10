package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsRecyclerAdapter;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/12.
 * email  1956766863@qq.com
 */
public class SearchRecordAdapter extends AbsRecyclerAdapter {
    private OnItemRecordClickListener mListener;

    public SearchRecordAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordViewHolder(mInflater.inflate(R.layout.row_search_recod,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecordViewHolder viewHolder = (RecordViewHolder) holder;
        String name = (String) mData.get(position);
        viewHolder.name.setText(name);
        viewHolder.name.setOnClickListener(new OnItemClickListener(name));
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder{
        TextView name;

        public RecordViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.id_record_name);
        }
    }

    class OnItemClickListener implements View.OnClickListener{

        private String keyword;
        public OnItemClickListener(String name) {
            this.keyword = name;
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onItemRecordClick(keyword);
            }
        }
    }

    public interface OnItemRecordClickListener{
        void onItemRecordClick(String keywords);
    }

    public void setOnItemRecordClickListener(OnItemRecordClickListener listener){
        this.mListener = listener;
    }
}
