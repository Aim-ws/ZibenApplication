package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.model.ProjectCard;

import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/11.
 * email  1956766863@qq.com
 */
public class PRecyclerQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int[] imgs = new int[]{R.mipmap.icon_p_q_1,R.mipmap.icon_p_q_2,R.mipmap.icon_p_q_3};
    private LayoutInflater inflater;
    private Context mContext;
    private List<ProjectCard> questions;

    public PRecyclerQuestionAdapter(Context context, List<ProjectCard> questions) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.questions = questions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.row_p_detail_question, null);
        ItemViewHolder holder = new ItemViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.mPQName.setText("市场潜力");
        Drawable d = mContext.getResources().getDrawable(imgs[position % imgs.length]);
        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
        viewHolder.mPQName.setCompoundDrawables(d,null,null,null);

        viewHolder.mPQAnswer.setText("潜力巨大，核心亮点");
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_p_detail_question.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView mPQName;
        TextView mPQAnswer;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mPQName = (TextView) itemView.findViewById(R.id.id_p_q_name);
            mPQAnswer = (TextView) itemView.findViewById(R.id.id_p_q_answer);
        }
    }
}
