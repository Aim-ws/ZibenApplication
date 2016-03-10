package com.ziben365.ocapp.photo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsBaseAdapter;
import com.ziben365.ocapp.photo.ui.PhotoActivity;
import com.ziben365.ocapp.photo.util.ImageLoader;
import com.ziben365.ocapp.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/18.
 * email  1956766863@qq.com
 */
public class ImageAdapter extends AbsBaseAdapter {

    private String mDirPath;
    private static ArrayList<String> mSelectedImg = new ArrayList<>();

    public ImageAdapter(Context context, List data, String dirPath) {
        super(context, data);
        this.mDirPath = dirPath;
        mSelectedImg.clear();
    }


    public static ArrayList<String> getmSelectedImg() {
        return mSelectedImg;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_photo_image, parent,false);
            viewHolder = new ViewHolder(convertView);
            ButterKnife.inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mImageView.setImageResource(R.mipmap.defaults_pic);
        viewHolder.mImageButton.setImageResource(R.mipmap.checkbox_attachment_press_0);
        viewHolder.mImageView.setColorFilter(null);

        String path = (String) getItem(position);

        L.i("----------path------------"+path);

        final String filePath;

        if (TextUtils.isEmpty(mDirPath)){
            //显示所有的照片
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(path,viewHolder.mImageView);
            filePath = (String) getItem(position);
        }else{
            //显示固定文件夹小的照片
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(mDirPath+"/"+path,viewHolder.mImageView);
            filePath = mDirPath+"/" +(String) getItem(position);
        }

        viewHolder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PhotoActivity.mMaxSelectCount == mSelectedImg.size()){
                    Toast.makeText(mContext,"最多可选择"+PhotoActivity.mMaxSelectCount+"张图片",Toast.LENGTH_SHORT).show();
                    return;
                }

                //选中
                if (mSelectedImg.contains(filePath)){
                    mSelectedImg.remove(filePath);
                    viewHolder.mImageView.setColorFilter(null);
                    viewHolder.mImageButton.setImageResource(R.mipmap.checkbox_attachment_press_0);
                }else{
                    //未选中
                    L.i("-------------------- 选中的图片路径--------------"+filePath);
                    mSelectedImg.add(filePath);
                    viewHolder.mImageView.setColorFilter(Color.parseColor("#77000000"));
                    viewHolder.mImageButton.setImageResource(R.mipmap.checkbox_attachment_press_1);
                }
//                notifyDataSetChanged();
                ((PhotoActivity)mContext).updateText(mSelectedImg);
            }
        });

        if (mSelectedImg.contains(filePath)){
            viewHolder.mImageView.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.mImageButton.setImageResource(R.mipmap.checkbox_attachment_press_1);
        }


        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_photo_image.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */

    static class ViewHolder {
        @InjectView(R.id.id_imageView)
        ImageView mImageView;
        @InjectView(R.id.id_imageButton)
        ImageButton mImageButton;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
