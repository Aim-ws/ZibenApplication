package com.ziben365.ocapp;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.fragment.CardListFragment;
import com.ziben365.ocapp.fragment.CategoryFragment;
import com.ziben365.ocapp.fragment.FlipFragment;
import com.ziben365.ocapp.fragment.PersonalFragment;
import com.ziben365.ocapp.fragment.RhythmPagerFragment;
import com.ziben365.ocapp.fragment.SettingFragment;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.inter.util.UserMessageDealUtil;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.ripple.MaterialRippleLayout;
import com.ziben365.ocapp.ui.AddProjectActivity;
import com.ziben365.ocapp.ui.EditProfileActivity;
import com.ziben365.ocapp.ui.LoginActivity;
import com.ziben365.ocapp.ui.PersonalCenterActivity;
import com.ziben365.ocapp.ui.SearchActivity;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.StringUtil;
import com.ziben365.ocapp.util.VolleyHelper;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.view.UserSignDialog;
import com.ziben365.ocapp.widget.CircleImageView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

    TabLayout tabLayout;
    //    ViewPager mViewPager;
    Toolbar toolbar;
    NavigationView navView;

    Fragment currentFragment;
    CardListFragment cardListFragment;
    RhythmPagerFragment rhythmPagerFragment;
    FlipFragment flipFragment;
    CategoryFragment categoryFragment;
    SettingFragment settingFragment;
    PersonalFragment personalFragment;

    public static int rhythmColor;

//    private int[] mColors = new int[]{R.mipmap.red, R.mipmap.blue, R.mipmap.gray, R.mipmap.dark, R.mipmap.green};

    TextView mFind, mDay, mCategory, mPerson, mSetting, mSearch, mUserName;
    ImageView mAddImage;
    CircleImageView mUserAvatar;
    LinearLayout headerLayout;

    DrawerLayout drawer;

    private List<TextView> textViewList;

    private UserSignReceiver userSignReceiver;

    private UserSignDialog userSignDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/


        DemoApplication.isDebug = true;
        init();
        registerMessageReceiver();

        JPushInterface.init(this);

        if (!JPushInterface.getConnectionState(this)) {

        }

        userSignDialog = new UserSignDialog(this);

        String action = getIntent().getAction();
        if (MESSAGE_RECEIVED_ACTION.equals(action)) {
            /*******处理极光推送过来的消息********/
            acceptBroadcast();
        } else {
            /*******获取用户签到信息*******/
            checkUserSign();
        }


        /*********更新用户信息广播注册************/
        IntentFilter filter = new IntentFilter(NotifyAction.ACTION_UPDATE_USER_INFO);
        registerReceiver(updateUserInfoBroadcastReceiver, filter);

        /************注册用户签到广播************/
        IntentFilter signFilter = new IntentFilter(NotifyAction.ACTION_USER_SIGN);
        userSignReceiver = new UserSignReceiver();
        registerReceiver(userSignReceiver, signFilter);


        rhythmColor = getResources().getColor(R.color.colorAppbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.nav_menu_item_find));
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        initNavHeaderView();

        cardListFragment = new CardListFragment();
        rhythmPagerFragment = new RhythmPagerFragment();
        flipFragment = new FlipFragment();
        categoryFragment = new CategoryFragment();
        settingFragment = new SettingFragment();
        personalFragment = new PersonalFragment();

        tabLayout.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, cardListFragment).commit();
        currentFragment = cardListFragment;
//        switchColor(cardListFragment, 0);


    }

    /**
     * 获取用户签到信息
     */
    private void checkUserSign() {
        String token = SPUtils.getToken(this);
        if (TextUtils.isEmpty(token)) return;
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_SIGN_INFO,
                RequestTag.REQUEST_TAG_USER_SIGN_INFO,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONObject object = GsonUtil.pareJSONObject(result).optJSONObject(RequestCode.KEY_ARRAY);
                                String sign_num = object.optString("sign_num");
                                String recent_time = object.optString("recent_time");
                                userSign(sign_num, recent_time);
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }


                });
    }

    /**
     * 初始化导航栏
     */
    private void initNavHeaderView() {
        navView = (NavigationView) findViewById(R.id.nav_view);
//        navView.setNavigationItemSelectedListener(this);
//        navView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));

        View headerView = navView.getHeaderView(0);
        mFind = (TextView) headerView.findViewById(R.id.id_nav_find);
        mDay = (TextView) headerView.findViewById(R.id.id_nav_day);
        mCategory = (TextView) headerView.findViewById(R.id.id_nav_category);
        mPerson = (TextView) headerView.findViewById(R.id.id_nav_person);
        mSetting = (TextView) headerView.findViewById(R.id.id_nav_setting);
        mSearch = (TextView) headerView.findViewById(R.id.id_search);
        mUserName = (TextView) headerView.findViewById(R.id.id_user_name);

        mUserAvatar = (CircleImageView) headerView.findViewById(R.id.id_user_avatar);

        headerLayout = (LinearLayout) headerView.findViewById(R.id.id_header_ll);

        mAddImage = (ImageView) headerView.findViewById(R.id.id_add_imageView);

        //设置点击水波效果
        MaterialRippleLayout.on(mFind).rippleAlpha(0.2f).rippleColor(getResources().getColor(R.color.color_text_89)).rippleOverlay(true).create();
        MaterialRippleLayout.on(mDay).rippleAlpha(0.2f).rippleColor(getResources().getColor(R.color.color_text_89)).rippleOverlay(true).create();
        MaterialRippleLayout.on(mCategory).rippleAlpha(0.2f).rippleColor(getResources().getColor(R.color.color_text_89)).rippleOverlay(true).create();
        MaterialRippleLayout.on(mPerson).rippleAlpha(0.2f).rippleColor(getResources().getColor(R.color.color_text_89)).rippleOverlay(true).create();
        MaterialRippleLayout.on(mSetting).rippleAlpha(0.2f).rippleColor(getResources().getColor(R.color.color_text_89)).rippleOverlay(true).create();

        textViewList = new ArrayList<>();
        textViewList.add(mFind);
        textViewList.add(mDay);
        textViewList.add(mCategory);
        textViewList.add(mPerson);
        textViewList.add(mSetting);


        initEvent();

        VolleyHelper.requestReason();
        VolleyHelper.requestTag();

        initNavHeaderViewData();


    }

    private void initEvent() {
        mFind.setOnClickListener(this);
        mDay.setOnClickListener(this);
        mCategory.setOnClickListener(this);
        mPerson.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        headerLayout.setOnClickListener(this);
        mAddImage.setOnClickListener(this);
    }

    private void initNavHeaderViewData() {
        String logo_url = (String) SPUtils.get(this, SPKeys.KEY_USER_IMAGE_TAG, "");
        String nick = (String) SPUtils.get(this, SPKeys.KEY_USER_NICK_TAG, "");
        L.i("logo:" + logo_url + "\nnick:" + nick);
        if (StringUtils.isNotEmpty(logo_url)) {
            if (logo_url.contains("http://")) {
                Glide.with(DemoApplication.applicationContect).load(logo_url).crossFade().centerCrop().into(mUserAvatar);
            } else {
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + logo_url).crossFade().centerCrop().into(mUserAvatar);
            }
        } else {
            mUserAvatar.setImageResource(R.mipmap.ic_default_avatar);
        }
        mUserName.setText(StringUtils.isNotEmpty(nick) ? nick : "请登录");
    }


    public Toolbar getToolbar() {
        return toolbar;
    }

    public NavigationView getNavView() {
        return navView;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (currentFragment instanceof CardListFragment) {
        } else {
            menu.removeItem(R.id.action_search);
        }
        MenuItem menuItem = menu.findItem(R.id.action_search);
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }


   /* public void switchRhythmColor(int position) {
        switchColor(rhythmPagerFragment, position % mColors.length);
    }*/

    /**
     * 切换主题颜色
     *
     * @param fragment
     */
    /*private void switchColor(final Fragment fragment, int position) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mColors[position]);
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (fragment instanceof RhythmPagerFragment) {
                    tabLayout.setBackgroundColor(vibrant.getRgb());
                    toolbar.setBackgroundColor(vibrant.getRgb());
                }
                navView.setBackgroundColor(Color.WHITE);
                navView.getHeaderView(0).setBackgroundColor(Color.WHITE);
                navView.setBackgroundColor(vibrant.getRgb());
                navView.getHeaderView(0).setBackgroundColor(vibrant.getRgb());

                if (Build.VERSION.SDK_INT > 21) {
                    Window window = getWindow();
                    window.setStatusBarColor(vibrant.getRgb());
                    window.setNavigationBarColor(vibrant.getRgb());
                }
            }
        });
    }*/


    /**
     * fragment  切换
     *
     * @param toFragment
     */
    private void switchFragment(Fragment toFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (null != toFragment) {
            if (toFragment != currentFragment) {
                if (toFragment.isAdded()) {
                    ft.hide(currentFragment).show(toFragment).commit();
                } else {
                    ft.hide(currentFragment).add(R.id.fragment_container, toFragment).commit();
                }
                currentFragment = toFragment;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_header_ll:
                String user_id = (String) SPUtils.get(this, SPKeys.KEY_USER_ID_TAG, "");
                if (TextUtils.isEmpty(user_id)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
                }
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.id_search:
                startActivity(new Intent(this, SearchActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.id_nav_find:
                switchFind();
                break;
            case R.id.id_nav_day:
                toolbar.setBackgroundColor(rhythmColor);
                tabLayout.setVisibility(View.GONE);
                switchFragment(rhythmPagerFragment);
//                switchColor(rhythmPagerFragment, 0);
                switchTAB(1);
                switchTitle(v);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.id_nav_category:
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorAppbar));
                tabLayout.setVisibility(View.GONE);
                switchFragment(categoryFragment);
//                switchColor(categoryFragment, 2);
                switchTAB(2);
                switchTitle(v);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.id_nav_person:
                String uid = (String) SPUtils.get(this, SPKeys.KEY_USER_ID_TAG, "");
                if (StringUtils.isEmpty(uid)) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorAppbar));
                    tabLayout.setVisibility(View.GONE);
                    switchFragment(personalFragment);
//                    switchColor(personalFragment, 3);
                    switchTAB(3);
                    switchTitle(v);
                }
                drawer.closeDrawer(GravityCompat.START);

                break;
            case R.id.id_nav_setting:
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorAppbar));
                tabLayout.setVisibility(View.GONE);
                switchFragment(settingFragment);
//                switchColor(settingFragment, 4);
                switchTAB(4);
                switchTitle(v);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.id_add_imageView:
                String u_id = (String) SPUtils.get(this, SPKeys.KEY_USER_ID_TAG, "");
                if (TextUtils.isEmpty(u_id)) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat("alpha", 1f, 0.7f, 1f);
                    PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.6f, 1f);
                    PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.6f, 1f);
                    ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(v, pvhA, pvhX, pvhY);
                    anim.setDuration(1000L);
                    anim.setRepeatCount(Integer.MAX_VALUE);
                    anim.setRepeatCount(ValueAnimator.REVERSE);
                    anim.setInterpolator(new LinearInterpolator());
                    anim.start();
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawer.closeDrawer(GravityCompat.START);
                            startActivity(new Intent(MainActivity.this, AddProjectActivity.class));
                        }
                    }, 1000L);

                }
                break;
        }


    }

    /**
     * 切换到每日发现
     *
     * @param
     */
    public void switchFind() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAppbar));
        tabLayout.setVisibility(View.VISIBLE);
        switchFragment(cardListFragment);
//        switchColor(cardListFragment, 1);
        switchTAB(0);
        switchTitle(mFind);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void switchTitle(View v) {
        String title = ((TextView) v).getText().toString();
        toolbar.setTitle(title);
    }

    @SuppressLint("NewApi")
    private void switchTAB(int position) {
        for (int i = 0; i < textViewList.size(); i++) {
            if (position == i) {
                textViewList.get(position).setBackgroundColor(getResources().getColor(R.color.color_nav_background));
            } else {
                textViewList.get(i).setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
        }
        return false;
    }

    @Override
    protected void onStop() {
        if (userSignDialog != null) {
            userSignDialog.dismiss();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        getSharedPreferences(PersonalFragment.FILE_DATA_NAME, MODE_PRIVATE).edit().clear().commit();
        getSharedPreferences(PersonalCenterActivity.FILE_USER_DATA_NAME, MODE_PRIVATE).edit().clear().commit();
        unregisterReceiver(updateUserInfoBroadcastReceiver);
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    BroadcastReceiver updateUserInfoBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().endsWith(NotifyAction.ACTION_UPDATE_USER_INFO)) {
                initNavHeaderViewData();
            }
        }
    };


    /***
     * @SuppressWarnings("StatementWithEmptyBody")
     * @Override public boolean onNavigationItemSelected(MenuItem item) {
     * // Handle navigation view item clicks here.
     * int id = item.getItemId();
     * <p/>
     * if (id == R.id.nav_find) {
     * // Handle the camera action
     * tabLayout.setVisibility(View.GONE);
     * switchFragment(rhythmPagerFragment);
     * switchColor(rhythmPagerFragment, 1);
     * <p/>
     * <p/>
     * } else if (id == R.id.nav_day) {
     * tabLayout.setVisibility(View.VISIBLE);
     * switchFragment(cardListFragment);
     * switchColor(cardListFragment, 0);
     * <p/>
     * } else if (id == R.id.nav_category) {
     * tabLayout.setVisibility(View.GONE);
     * switchFragment(rhythmPagerFragment);
     * switchColor(rhythmPagerFragment, 2);
     * <p/>
     * } else if (id == R.id.nav_person) {
     * tabLayout.setVisibility(View.GONE);
     * switchFragment(flipFragment);
     * switchColor(flipFragment, 3);
     * <p/>
     * } else if (id == R.id.nav_setting) {
     * tabLayout.setVisibility(View.GONE);
     * switchFragment(rhythmPagerFragment);
     * switchColor(rhythmPagerFragment, 4);
     * <p/>
     * }
     * <p/>
     * toolbar.setTitle(item.getTitle());
     * DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
     * drawer.closeDrawer(GravityCompat.START);
     * return true;
     * }
     ****/

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        JPushInterface.init(getApplicationContext());
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    private void acceptBroadcast() {
        String intent_action = getIntent().getAction();
        if (MESSAGE_RECEIVED_ACTION.equals(intent_action)) {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                L.i("--------------Bundle----------------" + b.toString());
                String extras = b.getString(JPushInterface.EXTRA_EXTRA);
                L.i("-----------------extras----------------------------" + extras);
                String action = GsonUtil.pareJSONObject(extras).optString("p_type");
                String action_value = GsonUtil.pareJSONObject(extras).optString("txt");
                if (UserMessageDealUtil.ACTION_PROJECT.equals(action)) {
                    ProjectItemClickUtil.itemClickDetails(this, action_value);
                }
            }
        }
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }


    /**
     * 签到广播
     */
    public class UserSignReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NotifyAction.ACTION_USER_SIGN)) {
                //用户签到
                checkUserSign();
            }
        }
    }

    /**
     * 用户签到
     *
     * @param recent_time
     * @param sign_num
     */
    private void userSign(String sign_num, String recent_time) {
        if (userSignDialog == null) {
            userSignDialog = new UserSignDialog(this);
        }
        int num = 0;
        if (!TextUtils.isEmpty(sign_num)) {
            num = Integer.parseInt(sign_num);
        }
        userSignDialog.setSign_num(num);
        String u_id = (String) SPUtils.get(this, SPKeys.KEY_USER_ID_TAG, "");
        if (TextUtils.isEmpty(u_id)) {
            return;
        }
        if (TextUtils.isEmpty(recent_time)) {
            userSignDialog.show();
        } else {
            long pre_time = Long.parseLong(recent_time);
            long curr_time = System.currentTimeMillis();
            String currTime = StringUtil.formatSystemTime(curr_time, "yyyy-MM-dd");
            String preTime = StringUtil.formatTime(pre_time, "yyyy-MM-dd");
            L.i("------------pre_time-------------" + pre_time * 1000 + "------------" + preTime);
            L.i("---------------curr_time----------" + curr_time + "------------------" + currTime);
            if (pre_time * 1000 < curr_time && !currTime.equals(preTime)) {
                userSignDialog.show();
            }

            /*if (SPUtils.contains(this, u_id)) {
                String preTime = (String) SPUtils.get(this, u_id, "");
                String currTime = StringUtil.formatSystemTime(System.currentTimeMillis(), "yyyy-MM-dd");

                L.e("preTime----------" + preTime + "\ncurrTime-------" + currTime);
                if (!currTime.equals(preTime)) {
                    userSignDialog.show();
                }
            } else {
                L.e("签到");
                userSignDialog.show();
            }*/
        }


    }

}
