package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsRecyclerAdapter;
import com.ziben365.ocapp.photo.ui.PhotoPagerActivity;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.util.ScreenUtils;
import com.ziben365.ocapp.widget.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/11.
 * email  1956766863@qq.com
 */
public class PImgAdapter extends AbsRecyclerAdapter<String> {
    private int imageWidth;

    public PImgAdapter(Context context, List<String> data) {
        super(context, data);
        int width = DemoApplication.sWidthDp / 4;
        imageWidth = ScreenUtils.dpToPx(width);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.row_p_detail_img, null);
        return new ItemViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder viewHolder = (ItemViewHolder) holder;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageWidth, imageWidth);
        layoutParams.height = imageWidth;
        layoutParams.width = imageWidth;
        layoutParams.bottomMargin = 10;
        layoutParams.topMargin = 10;
        layoutParams.leftMargin = 10;
        layoutParams.rightMargin = 10;
        viewHolder.mPImage.setLayoutParams(layoutParams);
        String img_url = mData.get(position);

        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + img_url + "!w300").crossFade().centerCrop().into(viewHolder.mPImage);
        viewHolder.mPImage.setOnClickListener(new ImageClickListener(position));


    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'row_p_detail_img.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mPImage;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mPImage = (RoundedImageView) itemView.findViewById(R.id.id_p_image);
        }
    }

    static class ImageSimpleTarget extends SimpleTarget<Bitmap> {
        private RoundedImageView mImage;
        private String image_key;

        public ImageSimpleTarget(RoundedImageView mPImage, String key) {
            this.mImage = mPImage;
            this.image_key = key;
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            mImage.setImageBitmap(resource);
            DemoApplication.getInstance().getAcache().put(image_key, resource);
        }
    }

    class ImageClickListener implements View.OnClickListener {
        private int position;

        public ImageClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, PhotoPagerActivity.class);
            intent.putStringArrayListExtra(PhotoPagerActivity.EXTRA_IMAGE_LIST, (ArrayList<String>) mData);
            intent.putExtra(PhotoPagerActivity.EXTRA_IMAGE_INDEX, position);
            mContext.startActivity(intent);
        }
    }
}
