package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.photo.util.ImageLoader;
import com.ziben365.ocapp.util.ScreenUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/7.
 * email  1956766863@qq.com
 */
public class PAddImageAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> images;
    private int imageWidth;

    public PAddImageAdapter(Context context, List<String> images) {
        inflater = LayoutInflater.from(context);
        this.images = images;
        int width = DemoApplication.sWidthDp / 4;
        imageWidth = ScreenUtils.dpToPx(width);
    }

    @Override
    public int getCount() {
        return images.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return getCount() == 1 ? null : images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_p_add_image, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(imageWidth, imageWidth);
        layoutParams.height = imageWidth;
        layoutParams.width = imageWidth;
        viewHolder.container.setLayoutParams(layoutParams);
        viewHolder.mPImageLeft.setVisibility(View.GONE);
        viewHolder.mPDelete.setVisibility(View.VISIBLE);
        viewHolder.mPImage.setImageResource(R.mipmap.icon_p_add_photo);
        if (position == 0 && images.size() > 0) {
            viewHolder.mPImageLeft.setVisibility(View.VISIBLE);
        }
        if (position == images.size()) {
            viewHolder.mPDelete.setVisibility(View.GONE);
        }
        if (getCount() == 2){
            switch (position){
                case 0:
                    String image_url = images.get(0);
                    if (!image_url.contains("http://")) {
                        ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(image_url, viewHolder.mPImage);
                    }
                    break;
                case 1:
                    viewHolder.mPImage.setImageResource(R.mipmap.icon_p_add_photo);
                    break;
            }
        }else{
            if (position < images.size()) {
                String image_url = images.get(position);
                if (!image_url.contains("http://")) {
                    ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(image_url, viewHolder.mPImage);
                }
            }
            if (position == images.size()) {
                viewHolder.mPImage.setImageResource(R.mipmap.icon_p_add_photo);
            }
        }

        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_p_add_image.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @InjectView(R.id.id_p_image)
        ImageView mPImage;
        @InjectView(R.id.id_p_image_left)
        ImageView mPImageLeft;
        @InjectView(R.id.id_p_delete)
        ImageView mPDelete;
        @InjectView(R.id.id_container)
        FrameLayout container;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
