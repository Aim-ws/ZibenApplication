package com.ziben365.ocapp.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ziben365.ocapp.MainActivity;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2016/1/20.
 * email  1956766863@qq.com
 */
public class GuideFragment extends Fragment {


    @InjectView(R.id.id_imageView)
    ImageView mImageView;

    private AnimationDrawable ad;

    private int[] drawables = new int[]{R.drawable.image_guide_1_1,R.drawable.image_guide_2_2,R.drawable.image_guide_1_3};

    public static Fragment newInstance(int position) {
        GuideFragment instance = new GuideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int position = getArguments() == null ? 0 : getArguments().getInt("position");
        L.i("--------------"+position);
        mImageView.setImageResource(drawables[position]);
//        ad = (AnimationDrawable) mImageView.getDrawable();
//        ad.start();
        if (position == drawables.length){
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    SPUtils.put(getActivity(), SPKeys.KEY_APP_RUN_FIRST,false);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ad.stop();
        ButterKnife.reset(this);
    }
}
