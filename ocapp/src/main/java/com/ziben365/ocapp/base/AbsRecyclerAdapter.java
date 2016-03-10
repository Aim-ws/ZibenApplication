package com.ziben365.ocapp.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/11.
 * email  1956766863@qq.com
 */
abstract public class AbsRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mData;

    public AbsRecyclerAdapter(Context context, List<T> data){
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
