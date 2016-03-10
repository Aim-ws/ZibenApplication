package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsBaseAdapter;
import com.ziben365.ocapp.model.FlipCard;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2015/12/29.
 * email  1956766863@qq.com
 */
public class FlipAdapter extends AbsBaseAdapter<FlipCard> {
    public FlipAdapter(Context context, List<FlipCard> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_flip_card, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FlipCard entity = (FlipCard) getItem(position);
        viewHolder.idName.setText(entity.id +"、"+ entity.name);
        viewHolder.idContent.setText(entity.content);
        Glide.with(mContext).load(entity.imgUrl).asBitmap().into(viewHolder.idImageView);
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_flip_card.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @InjectView(R.id.id_name)
        TextView idName;
        @InjectView(R.id.id_imageView)
        ImageView idImageView;
        @InjectView(R.id.id_content)
        TextView idContent;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
