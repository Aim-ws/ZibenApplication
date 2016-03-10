package com.ziben365.ocapp.photo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.photo.view.PhotoViewAttacher;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.util.L;

import butterknife.ButterKnife;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/21.
 * email  1956766863@qq.com
 */
public class ImageDetailFragment extends Fragment {

    ImageView image;
    ProgressBar loading;

    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String s) {
        ImageDetailFragment instance = new ImageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", s);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_detail, null);
        image = (ImageView) view.findViewById(R.id.image);
        loading = (ProgressBar) view.findViewById(R.id.loading);
        mAttacher = new PhotoViewAttacher(image);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String path = getArguments().getString("path");
            L.i("-------------path------------" + path);
            if (!path.contains("http://")) {
                image.setImageBitmap(DemoApplication.getInstance().getAcache().getAsBitmap(path));
                loading.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(QiNiuConfig.QINIU_PIC_URL + path + "!w800").asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        loading.setVisibility(View.GONE);
                        image.setImageBitmap(resource);
                        mAttacher.update();
                        image.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                // TODO Auto-generated method stub
//                                try {
//                                    Toast.makeText(getActivity(), "保存图片成功！", Toast.LENGTH_LONG).show();
//                                } catch (Exception e) {
//                                    Toast.makeText(getActivity(), "保存图片失败！", Toast.LENGTH_LONG).show();
//                                }
                                return true;
                            }
                        });
                    }
                });
            }
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
