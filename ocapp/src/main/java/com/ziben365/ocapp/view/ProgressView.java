package com.ziben365.ocapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.ziben365.ocapp.R;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/14.
 * email  1956766863@qq.com
 */
public class ProgressView extends View {
    private ImageView imageView;
    private RotateAnimation ra;

    public ProgressView(Context context) {
        this(context,null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View convertView = LayoutInflater.from(context).inflate(R.layout.layout_progress,null);
        imageView = (ImageView) convertView.findViewById(R.id.id_progress_image);
        ra = (RotateAnimation) AnimationUtils.loadAnimation(context,R.anim.tip);
        imageView.startAnimation(ra);
    }


}
