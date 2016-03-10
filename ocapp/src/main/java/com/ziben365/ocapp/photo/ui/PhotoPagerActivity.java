package com.ziben365.ocapp.photo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.photo.view.HackyViewPager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/21.
 * email  1956766863@qq.com
 * <p/>
 * 浏览大图
 */
public class PhotoPagerActivity extends AppCompatActivity {

    @InjectView(R.id.id_photo_viewpager)
    HackyViewPager mPhotoViewpager;
    @InjectView(R.id.id_indicator)
    TextView mIndicator;

    private ArrayList<String> images;

    private int pagerPosition;

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_LIST = "image_list";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);
        ButterKnife.inject(this);

        images = getIntent().getStringArrayListExtra(EXTRA_IMAGE_LIST);
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX,0);
        mPhotoViewpager.setAdapter(new PhotoPagerAdapter(getSupportFragmentManager()));

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPhotoViewpager.getAdapter().getCount());
        mIndicator.setText(text);
        mPhotoViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CharSequence text = getString(R.string.viewpager_indicator, position + 1, mPhotoViewpager.getAdapter().getCount());
                mIndicator.setText(text);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
        mPhotoViewpager.setCurrentItem(pagerPosition);


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPhotoViewpager.getCurrentItem());
    }


    class PhotoPagerAdapter extends FragmentStatePagerAdapter{
        public PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(images.get(position));
        }


        @Override
        public int getCount() {
            return images == null ? 0 :images.size();
        }
    }


}
