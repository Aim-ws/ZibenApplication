package com.ziben365.ocapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ziben365.ocapp.MainActivity;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.fragment.GuideFragment;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.NetworkUtil;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/19.
 * email  1956766863@qq.com
 * guide activity
 */
public class GuideActivity extends AppCompatActivity {


    boolean isFirst;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.ic_container)
    LinearLayout mContainer;
    @InjectView(R.id.id_imageView)
    ImageView mImageView;
    private List<ImageView> mImageViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


        isFirst = (boolean) SPUtils.get(this, SPKeys.KEY_APP_RUN_FIRST, true);
        if (NetworkUtil.isNetworkAvailable(this)) {
//        if (isFirst) {
//            runFirst();
//        } else {
            run();
//        }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("设置网络");
            builder.setMessage("网络错误，请设置网络");

            builder.setPositiveButton("设置网络", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //获取系统版本号
                            /* Build.VERSION_CODES
                                1 (0x00000001)           Android 1.0             BASE
                                2 (0x00000002)           Android 1.1             BASE_1_1
                                3 (0x00000003)           Android 1.5             CUPCAKE
                                4 (0x00000004)           Android 1.6             DONUT
                                5 (0x00000005)           Android 2.0             ECLAIR
                                6 (0x00000006)           Android 2.0.1          ECLAIR_0_1
                                7 (0x00000007)           Android 2.1             ECLAIR_MR1
                                8 (0x00000008)           Android 2.2             FROYO
                                9 (0x00000009)           Android 2.3             GINGERBREAD
                                10 (0x0000000a)         Android 2.3.3          GINGERBREAD_MR1
                                11 (0x0000000b)         Android 3.0             HONEYCOMB
                                12 (0x0000000c)         Android 3.1             HONEYCOMB_MR1
                                13 (0x0000000d)         Android 3.2             HONEYCOMB_MR2 */
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    System.out.println("currentapiVersion = " + currentapiVersion);
                    Intent intent;
                    if (currentapiVersion < 11) {
                        intent = new Intent();
                        intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                    } else {
                        //3.0以后
                        //intent = new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    }
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }

    }

    private void run() {
        final Bundle bundle = getIntent().getExtras();
        L.i("-------guideActivity----------" + (bundle == null ? "" : bundle.toString()));
        viewpager.setVisibility(View.GONE);
        mContainer.setVisibility(View.GONE);
        mImageView.setImageResource(R.mipmap.welcome);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000L);
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    if (null != bundle) {
                        intent.setAction(MainActivity.MESSAGE_RECEIVED_ACTION);
                        intent.putExtras(bundle);
                    }
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 首次运行
     */
    private void runFirst() {
        mImageView.setVisibility(View.GONE);
        mContainer.removeAllViews();
        mImageViews = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(30, 30);
            lp.leftMargin = 10;
            lp.rightMargin = 10;
            imageView.setLayoutParams(lp);
            imageView.setImageResource(R.drawable.shape_guide_normal);
            mContainer.addView(imageView);
            mImageViews.add(imageView);
        }
        mImageViews.get(0).setImageResource(R.drawable.shape_guide_select);
        viewpager.setAdapter(new GuideAdapter(getSupportFragmentManager()));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mImageViews.size(); i++) {
                    if (position == i) {
                        mImageViews.get(i).setImageResource(R.drawable.shape_guide_select);
                    } else {
                        mImageViews.get(i).setImageResource(R.drawable.shape_guide_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(0);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000L);
                    finish();
                    startActivity(new Intent(GuideActivity.this,MainActivity.class));
                    SPUtils.put(GuideActivity.this,SPKeys.KEY_APP_RUN_FIRST,false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    class GuideAdapter extends FragmentPagerAdapter {

        public GuideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return GuideFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
