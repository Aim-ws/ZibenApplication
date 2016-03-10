package com.ziben365.ocapp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.PCommentAdapter;
import com.ziben365.ocapp.adapter.PImgAdapter;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.inter.util.UserPersonalDealUtil;
import com.ziben365.ocapp.layoutmanager.FullyGridLayoutManager;
import com.ziben365.ocapp.model.ProjectComment;
import com.ziben365.ocapp.model.ProjectDetail;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.util.ErrorDealUtil;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.ScreenUtils;
import com.ziben365.ocapp.util.ShareUtils;
import com.ziben365.ocapp.util.StringUtil;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.widget.ArcMenuView;
import com.ziben365.ocapp.widget.CircleImageView;
import com.ziben365.ocapp.widget.RoundedImageView;
import com.ziben365.ocapp.widget.detector.FabArcMenuView;
import com.ziben365.ocapp.widget.flowlayout.FlowLayout;
import com.ziben365.ocapp.widget.refresh.ProgressStyle;
import com.ziben365.ocapp.widget.refresh.recycler.RefreshRecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/12.
 * email  1956766863@qq.com
 */
public class ProjectDetailsActivity extends BaseActivity implements RefreshRecyclerView.LoadingListener,
        PCommentAdapter.OnReplyCommentClickListener, PCommentAdapter.OnReplyUserClickListener, PCommentAdapter.OnCommentPraiseClickListener {

    @InjectView(R.id.back_icon)
    ImageView backIcon;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @InjectView(R.id.id_refresh_recyclerView)
    RefreshRecyclerView mRefreshRecyclerView;


    TextView mPName;
    TextView mPDescription;
    ImageView mPLabel;
    FlowLayout mFlowLayoutTag;
    TextView mDollar;
    TextView mLike;
    TextView mComment;
    TextView mShare;
    TextView mPContent;
    LinearLayout mRoundContainer;
    TextView mPQuestionWord;
    TextView mPImageWord;
    TextView mTvDown;
    RelativeLayout mPDownLayout;
    TextView mTvPPraise;
    LinearLayout mPPraiseContainer;
    LinearLayout mPPraiseLayout;
    TextView mTvLabel;
    CircleImageView mPAvatar;
    TextView mPUsername;
    LinearLayout mPQuestion;
    LinearLayout mPImg;
    RelativeLayout mPLayoutTop;
    CardView mCardView;
    RoundedImageView mPLogo;
    TextView mPQuestionContent;

    private int state = 0x0;
    private static final int STATE_CLOSE = 0x0;
    private static final int STATE_OPEN = 0x1;

    private static final int COMMENT_RELEASE = 0x2;
    private static final int COMMENT_REPLY = 0x3;
    private int comment_type = COMMENT_RELEASE;

    private boolean isBroadcast = false;


    private Dialog inputDialog;
    private EditText editText;

    /*****
     * 原版问题列表显示控件
     *****/
//    RecyclerView mPQRecyclerView;

    RecyclerView mPImgRecyclerView;
    @InjectView(R.id.id_arcMenuView)
    FabArcMenuView mArcMenuView;


    private int imgCount = 8;

    private View headerView;


    private List<ProjectComment> commentList;
    private PCommentAdapter commentAdapter;


    /*******
     * 原版问题列表数据及adapter
     ******/
//    private PRecyclerQuestionAdapter qquestionRecyclerAdapter;
//    private List<ProjectCard> questions;

    private ProjectDetail project;
    private ProjectDetail.ProjectInfo projectInfo;
    private PImgAdapter imgRecyclerAdapter;
    private List<String> imgs;

    private String pro_id;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()    //
            .showImageOnLoading(R.mipmap.defaults_pic)    //
            .showImageOnFail(R.mipmap.defaults_pic)       //
            .cacheInMemory(true)     //
            .cacheOnDisk(true)     //
            .build();


    private String token;

    private int page = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_project_details);
        ButterKnife.inject(this);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/

        project = (ProjectDetail) getIntent().getSerializableExtra("obj");
        isBroadcast = getIntent().getBooleanExtra(NotifyAction.KEY_LIKE_BROADCAST, false);


        initToolbar();
        initView();
        initHeaderView();
        resetLabelText();
    }

    /**
     * 设置标题
     */
    private void initToolbar() {
        toolbar.setTitle("");
        collapsingToolbarLayout.setTitle("");
        collapsingToolbarLayout.setTitleEnabled(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    /**
     * 修改推荐说，展示图样式
     * <p/>
     * 首字符放大
     */
    private void resetLabelText() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.p_recommend_word));
        ss.setSpan(new AbsoluteSizeSpan(70), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPQuestionWord.setText(ss);

        SpannableString ss1 = new SpannableString(getResources().getString(R.string.p_img_word));
        ss1.setSpan(new AbsoluteSizeSpan(70), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPImageWord.setText(ss1);
    }


    /**
     * 初始化view
     */
    private void initView() {
        headerView = LayoutInflater.from(this).inflate(R.layout.layout_detail_header, null);
        commentList = new ArrayList<>();
        commentAdapter = new PCommentAdapter(this, commentList);
        commentAdapter.setOnReplyCommentClickListener(this);
        commentAdapter.setOnReplyUserClickListener(this);
        commentAdapter.setOnCommentPraiseClickListener(this);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mRefreshRecyclerView.addHeaderView(headerView);
        mRefreshRecyclerView.setAdapter(commentAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //设置垂直排列
        mRefreshRecyclerView.setLayoutManager(layoutManager);
        //加载每条的动画
        mRefreshRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置下拉刷新
        mRefreshRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRefreshRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRefreshRecyclerView.setPullRefreshEnabled(false);
        mRefreshRecyclerView.setLoadingMoreEnabled(true);
        mRefreshRecyclerView.setLoadingListener(this);
        mRefreshRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    Glide.with(DemoApplication.applicationContect).resumeRequests();
                } else {
                    Glide.with(DemoApplication.applicationContect).pauseRequests();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        if (project != null && project.comments != null) {
            commentList.clear();
            commentList.addAll(project.comments);
        }
        commentAdapter.notifyDataSetChanged();
        mArcMenuView.attachToRecyclerView(mRefreshRecyclerView);

    }

    /**
     * 初始化recyclerView的
     * headerView
     */
    private void initHeaderView() {
        mPLogo = (RoundedImageView) headerView.findViewById(R.id.id_p_logo);
        mPName = (TextView) headerView.findViewById(R.id.id_p_name);
        mPDescription = (TextView) headerView.findViewById(R.id.id_p_description);
        mDollar = (TextView) headerView.findViewById(R.id.id_tv_dollar);
        mLike = (TextView) headerView.findViewById(R.id.id_tv_like);
        mComment = (TextView) headerView.findViewById(R.id.id_tv_comment);
        mShare = (TextView) headerView.findViewById(R.id.id_tv_share);
        mPContent = (TextView) headerView.findViewById(R.id.id_p_content);
        mFlowLayoutTag = (FlowLayout) headerView.findViewById(R.id.id_flow_layout_tag);

        mPUsername = (TextView) headerView.findViewById(R.id.id_p_username);
        mPAvatar = (CircleImageView) headerView.findViewById(R.id.id_p_avatar);

        mRoundContainer = (LinearLayout) headerView.findViewById(R.id.id_round_container);
        mPQuestion = (LinearLayout) findViewById(R.id.id_p_question);
        mPQuestionWord = (TextView) headerView.findViewById(R.id.id_p_question_word);
        mPQuestionContent = (TextView) headerView.findViewById(R.id.id_p_question_content);
//        mPQRecyclerView = (RecyclerView) headerView.findViewById(R.id.id_p_q_recyclerView);

        mPImageWord = (TextView) headerView.findViewById(R.id.id_p_image_word);
        mPImgRecyclerView = (RecyclerView) headerView.findViewById(R.id.id_p_img_recyclerView);
        mPImg = (LinearLayout) headerView.findViewById(R.id.id_p_img);

        mCardView = (CardView) headerView.findViewById(R.id.id_cardView);
        mPLayoutTop = (RelativeLayout) headerView.findViewById(R.id.id_p_layout_top);

        mPLabel = (ImageView) headerView.findViewById(R.id.id_p_label);
        mTvDown = (TextView) headerView.findViewById(R.id.id_tv_down);
        mPDownLayout = (RelativeLayout) headerView.findViewById(R.id.id_p_down_layout);

        mTvPPraise = (TextView) headerView.findViewById(R.id.id_tv_p_praise);
        mPPraiseContainer = (LinearLayout) headerView.findViewById(R.id.id_p_praise_container);
        mPPraiseLayout = (LinearLayout) headerView.findViewById(R.id.id_p_praise_layout);

        mTvLabel = (TextView) headerView.findViewById(R.id.id_tv_label);

        initEvent();

        updateProjectView();

    }

    private void updateProjectView() {
        projectInfo = project.info;
        pro_id = projectInfo.pid;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + projectInfo.banner + "!w480")
                        .crossFade().centerCrop().into(backIcon);
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + projectInfo.logo + "!w100")
                        .crossFade().centerCrop().into(mPLogo);
            }
        });
        /*ImageLoader.getInstance().displayImage(QiNiuConfig.QINIU_PIC_URL + project.info.banner + "!w480",backIcon,options);
        ImageLoader.getInstance().displayImage(QiNiuConfig.QINIU_PIC_URL + project.info.logo + "!w100",mPLogo,options);*/

        mPName.setText(projectInfo.pname);
        mPDescription.setText(projectInfo.intro);

        //更新数据
        updateNumber();


        //添加行业标签
        addIndustryTag();

        //设置内容
        Drawable d = getResources().getDrawable(R.mipmap.icon_p_content_tag);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan is = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
        SpannableString ss = new SpannableString(" ");
        ss.setSpan(is, 0, " ".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(ss).append(projectInfo.pdesc);
        mPContent.setText(ssb);

        if (project.valuation != null && project.valuation.size() > 0) {
            L.e(QiNiuConfig.QINIU_PIC_URL + project.valuation.get(0).logo + "!w100");
            Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + project.valuation.get(0).logo + "!w100").crossFade().centerCrop().into(mPAvatar);
            mPUsername.setText(project.valuation.get(0).real_name);
            mPAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserPersonalDealUtil.dealUser(ProjectDetailsActivity.this, project.valuation.get(0).user_id);
                }
            });
        } else {
            mPAvatar.setImageResource(R.mipmap.ic_default_avatar);
        }

        //添加推荐估值
        addRound();

        mPQuestionContent.setText(projectInfo.recreason);

        //显示展示图

        imgs = new ArrayList<>();
        String[] images = projectInfo.pics.split(";");
        for (int i = 0; i < images.length; i++) {
            imgs.add(images[i]);
        }
        imgRecyclerAdapter = new PImgAdapter(this, imgs);
        FullyGridLayoutManager fgm = new FullyGridLayoutManager(this, 3);
        fgm.setSmoothScrollbarEnabled(true);
        fgm.setOrientation(LinearLayoutManager.VERTICAL);
        mPImgRecyclerView.setLayoutManager(fgm);
        mPImgRecyclerView.setAdapter(imgRecyclerAdapter);
        mPImg.setVisibility(View.VISIBLE);


        //添加收藏头像
        addLikeView();
    }

    private void updateNumber() {
        mDollar.setText("点赞(" + (TextUtils.isEmpty(projectInfo.praise) ? "0" : projectInfo.praise) + ")");
        mLike.setText("收藏(" + (TextUtils.isEmpty(projectInfo.collects) ? "0" : projectInfo.collects) + ")");
        mComment.setText("评论(" + (TextUtils.isEmpty(projectInfo.comments) ? "0" : projectInfo.comments) + ")");
        mShare.setText("分享(" + (TextUtils.isEmpty(projectInfo.share) ? "0" : projectInfo.share) + ")");
    }

    private void initEvent() {
        mPDownLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == STATE_CLOSE) {
                    mPQuestionContent.setEllipsize(null);
                    mPQuestionContent.setMaxLines(Integer.MAX_VALUE);
                    mPQuestionContent.setText(projectInfo.recreason);
                    state = STATE_OPEN;
                    Drawable d = getResources().getDrawable(R.mipmap.ic_p_up);
                    d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
                    mTvDown.setCompoundDrawables(null, d, null, null);
                    mTvDown.setText("收回");
                } else {
                    mPQuestionContent.setEllipsize(TextUtils.TruncateAt.END);
                    mPQuestionContent.setMaxLines(3);
                    mPQuestionContent.setText(projectInfo.recreason);
                    state = STATE_CLOSE;
                    Drawable d = getResources().getDrawable(R.mipmap.icon_p_down);
                    d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
                    mTvDown.setCompoundDrawables(null, null, null, d);
                    mTvDown.setText("展开");
                }

            }
        });
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCount();
                ShareUtils.showShare(ProjectDetailsActivity.this, project);
            }
        });
        mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_TOKEN_TAG, "");
                if (TextUtils.isEmpty(token)) {
                    startActivity(new Intent(ProjectDetailsActivity.this, LoginActivity.class));
                    return;
                }
                comment_type = COMMENT_RELEASE;
                showInputDialog("我也说一句...");
            }
        });
        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like();
            }
        });
        mDollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praise();
            }
        });
        mArcMenuView.setOnMenuItemClickListener(new ArcMenuView.OnMenuItemClickListener() {
            @Override
            public void onMenuClick(View view, int pos) {
                switch (pos) {
                    case 1:
                        shareCount();
                        ShareUtils.showShare(ProjectDetailsActivity.this, project);
                        break;
                    case 2:
                        token = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_TOKEN_TAG, "");
                        if (TextUtils.isEmpty(token)) {
                            startActivity(new Intent(ProjectDetailsActivity.this, LoginActivity.class));
                            return;
                        }
                        comment_type = COMMENT_RELEASE;
                        showInputDialog("我也说一句...");
                        break;
                    case 3:
                        like();

                        break;
                    case 4:
                        token = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_TOKEN_TAG, "");
                        if (TextUtils.isEmpty(token)) {
                            startActivity(new Intent(ProjectDetailsActivity.this, LoginActivity.class));
                            return;
                        }
                        praise();
                        break;
                    case 5:
                        startActivity(new Intent(ProjectDetailsActivity.this, AddProjectActivity.class));
                        break;
                }
            }
        });

    }

    /**
     * 分享计数
     */
    private void shareCount() {
        HashMap<String, String> param = new HashMap<>();
        param.put("id", pro_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_SHARE, RequestTag.REQUEST_TAG_SHARE,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                int count = 0;
                                if (!TextUtils.isEmpty(projectInfo.share)) {
                                    count = Integer.parseInt(projectInfo.share);
                                }
                                ++count;
                                projectInfo.share = String.valueOf(count);
                                updateNumber();
                            } else {
                                Toast.makeText(ProjectDetailsActivity.this, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    /**
     * 点赞
     */
    private void praise() {
        token = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_TOKEN_TAG, "");
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(ProjectDetailsActivity.this, LoginActivity.class));
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("id", pro_id);
        param.put("token", token);
        VolleyRequest.volleyRequestPost(Link.PROJECT_USER_PROJECT_ADD_PRAISE,
                RequestTag.REQUEST_TAG_USER_PROJECT_ADD_PRAISE,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                int count = 0;
                                if (!TextUtils.isEmpty(projectInfo.praise)) {
                                    count = Integer.parseInt(projectInfo.praise);
                                }
                                ++count;
                                projectInfo.praise = String.valueOf(count);
                                updateNumber();
                            } else {
                                ErrorDealUtil.dealError(ProjectDetailsActivity.this, GsonUtil.pareCode(result));
                                Toast.makeText(ProjectDetailsActivity.this, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    /**
     * 收藏/删除收藏项目
     */
    private void like() {
        token = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_TOKEN_TAG, "");
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(ProjectDetailsActivity.this, LoginActivity.class));
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("id", pro_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_FAVORITES,
                RequestTag.REQUEST_TAG_FAVORITES, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i("-------------" + result);
                        Intent intent = new Intent(NotifyAction.ACTION_UPDATE_USER_LIKE);
                        sendBroadcast(intent);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                int count = 0;
                                count = GsonUtil.pareJSONObject(result).optInt(RequestCode.KEY_ARRAY);
                                projectInfo.collects = String.valueOf(count);
                                updateNumber();
                            } else {
                                Toast.makeText(ProjectDetailsActivity.this, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                                ErrorDealUtil.dealError(ProjectDetailsActivity.this, GsonUtil.pareCode(result));
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }


    /**
     * 添加收藏人的头像
     */
    private void addLikeView() {
        int pWidth = ScreenUtils.dpToPx(DemoApplication.sWidthDp) / imgCount;
        int count = Math.min(project.praise_person.size(), 7);
        mPPraiseContainer.removeAllViews();
        for (int i = 0; i < count; i++) {
            CircleImageView image = new CircleImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = pWidth; // setting imageView height
            params.width = pWidth; // setting imageView width
            params.leftMargin = 5; // setting imageView leftMargin
            params.rightMargin = 5; // setting ImageView rightMargin
            if (i < 6) {
                L.e(QiNiuConfig.QINIU_PIC_URL + project.praise_person.get(i).logo + "!w100");
                Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + project.praise_person.get(i).logo + "!w100").crossFade().centerCrop().into(image);
                image.setOnClickListener(new OnPraiseAvatarClickListener(project.praise_person.get(i)));
            } else {
                image.setImageResource(R.mipmap.icon_next);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProjectDetailsActivity.this, ProjectPraiseActivity.class);
                        intent.putExtra("p_id", pro_id);
                        startActivity(intent);
                    }
                });
            }
            mPPraiseContainer.addView(image, params);
        }
    }

    /**
     * 推荐的估值
     */
    private void addRound() {
        if (project != null) {
            for (int i = 0; i < project.valuation.size(); i++) {
                TextView text = new TextView(this);
                text.setTextColor(getResources().getColor(R.color.colorAppbar));
                text.setTextSize(12);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = 10;
                text.setPadding(5, 5, 5, 5);
                String time = StringUtil.formatTime(Long.parseLong(project.valuation.get(i).add_time));
                text.setText(time + " 第" + (i + 1) + "次推荐  估值" + project.valuation.get(i).valuation + "万");
                mRoundContainer.addView(text, lp);
            }
        } else {
            TextView text = new TextView(this);
            text.setTextColor(getResources().getColor(R.color.colorAppbar));
            text.setTextSize(12);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            text.setPadding(5, 5, 5, 5);
//            text.setText(StringUtil.formatTime(Long.valueOf(project.info.add_time)) + " 第一次推荐  估值" + project.valuation + "万");
            mRoundContainer.addView(text, lp);
        }

    }


    /**
     * 添加标签
     */
    private void addIndustryTag() {
        if (project == null) {
            for (int i = 0; i < 5; i++) {
                TextView textView = new TextView(this);
                textView.setPadding(10, 3, 10, 3);
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.rightMargin = 5;
                lp.leftMargin = 5;
                lp.topMargin = 1;
                lp.bottomMargin = 1;
                textView.setLayoutParams(lp);
                textView.setText("互联网");
                textView.setTextColor(getResources().getColor(R.color.colorAppbar));
                mFlowLayoutTag.addView(textView, lp);
            }
        } else {
            if (!TextUtils.isEmpty(projectInfo.tags)) {
                String tags[] = projectInfo.tags.split(" ");
                for (int i = 0; i < tags.length; i++) {
                    TextView textView = new TextView(this);
                    textView.setPadding(10, 3, 10, 3);
                    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.rightMargin = 5;
                    lp.leftMargin = 5;
                    lp.topMargin = 1;
                    lp.bottomMargin = 1;
                    textView.setLayoutParams(lp);
                    textView.setText(tags[i]);
                    textView.setTextColor(getResources().getColor(R.color.colorAppbar));
                    mFlowLayoutTag.addView(textView, lp);
                }
            }
        }

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        loadMoreComments();
    }

    private void loadMoreComments() {
        ++page;
        HashMap<String, String> param = new HashMap<>();
        param.put("id", pro_id);
        param.put("p", String.valueOf(page));
        VolleyRequest.volleyRequestPost(Link.PROJECT_COMMENT_LIST,
                RequestTag.REQUEST_TAG_PROJECT_COMMENTLIST,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                                ArrayList<ProjectComment> data = GsonUtil.getInstance().fromJson(array.toString(),
                                        new TypeToken<ArrayList<ProjectComment>>() {
                                        }.getType());
                                commentList.addAll(data);
                                commentAdapter.notifyDataSetChanged();
                                mRefreshRecyclerView.loadMoreComplete();
                                /*if (commentList.size() < 10) {
                                    page = 1;
                                } else {
                                    page = commentList.size() / 10;
                                }*/
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });


    }

    //    private ProjectComment p_comment;
    private String p_id, rec_id, u_name;

    @Override
    public void onReplyCommentClick(View v, ProjectComment pComment) {
//        this.p_comment = pComment;
        this.p_id = pComment.pid;
        this.rec_id = pComment.user_id;
        this.u_name = pComment.real_name;
        token = (String) SPUtils.get(this, SPKeys.KEY_USER_TOKEN_TAG, "");
        String uid = (String) SPUtils.get(this, SPKeys.KEY_USER_ID_TAG, "");
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(ProjectDetailsActivity.this, LoginActivity.class));
            return;
        }
        if (rec_id.endsWith(uid)) {
            Toast.makeText(this, "自己不能回复自己", Toast.LENGTH_SHORT).show();
            return;
        }
        comment_type = COMMENT_REPLY;
        showInputDialog("@" + u_name);
    }

    /**
     * 显示输入框
     *
     * @param s
     */
    private void showInputDialog(String s) {
        inputDialog = new Dialog(this, R.style.myDialogTheme);
        View convertView = LayoutInflater.from(this).inflate(R.layout.layout_send_msg, null);
        inputDialog.setContentView(convertView);
        Window dialogWindow = inputDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        inputDialog.setCancelable(true);
        inputDialog.show();
        TextView sendView = (TextView) convertView.findViewById(R.id.id_send_msg);
        editText = (EditText) convertView.findViewById(R.id.id_edit_msg);
        editText.setHint(s);
        sendView.setOnClickListener(new OnSendMsgListener(editText));
    }

    @Override
    public void onReplyUserClick(View v, ProjectComment comment) {
//        this.p_comment = comment;
        this.p_id = comment.pid;
        this.rec_id = comment.rec_id;
        this.u_name = comment.rev_relname;
        token = (String) SPUtils.get(this, SPKeys.KEY_USER_TOKEN_TAG, "");
        String uid = (String) SPUtils.get(this, SPKeys.KEY_USER_ID_TAG, "");
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(ProjectDetailsActivity.this, LoginActivity.class));
            return;
        }
        if (rec_id.endsWith(uid)) {
            Toast.makeText(this, "自己不能回复自己", Toast.LENGTH_SHORT).show();
            return;
        }
        comment_type = COMMENT_REPLY;
        showInputDialog("@" + u_name);
    }

    @Override
    public void onCommentPraiseClick(View v, ProjectComment comment, int position) {
        token = (String) SPUtils.get(this, SPKeys.KEY_USER_TOKEN_TAG, "");
        String uid = (String) SPUtils.get(this, SPKeys.KEY_USER_ID_TAG, "");
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(ProjectDetailsActivity.this, LoginActivity.class));
            return;
        }
        /*if (comment.user_id.endsWith(uid)) {
            Toast.makeText(this, "自己不能回复自己", Toast.LENGTH_SHORT).show();
            return;
        }*/
        praiseComment(comment, position);
    }

    /**
     * 给评论点赞
     *
     * @param comment
     * @param position
     */
    private void praiseComment(final ProjectComment comment, final int position) {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("top_id", comment.top_id);
        VolleyRequest.volleyRequestPost(Link.PROJECT_COMMENT_PRAISE,
                RequestTag.REQUEST_TAG_PROJECT_COMMENT_PRAISE,
                param, new VolleyInterface() {

                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        L.i(result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                int p_count = 0;
                                if (!TextUtils.isEmpty(comment.num)) {
                                    p_count = Integer.parseInt(comment.num);
                                }
                                p_count++;
                                commentList.get(position).num = String.valueOf(p_count);
                                commentAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ProjectDetailsActivity.this, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                                ErrorDealUtil.dealError(ProjectDetailsActivity.this, GsonUtil.pareCode(result));
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }


    /**
     * 发送按钮点击监听
     */
    class OnSendMsgListener implements View.OnClickListener {
        private EditText editText;

        public OnSendMsgListener(EditText msg) {
            this.editText = msg;
        }

        @Override
        public void onClick(View v) {
            String targetMsg = editText.getText().toString().trim();
            L.i("-----------11----------" + targetMsg);
            if (TextUtils.isEmpty(targetMsg)) {
                Toast.makeText(ProjectDetailsActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            inputDialog.dismiss();
            sendMsg(targetMsg);
        }
    }

    /**
     * 发送消息
     *
     * @param targetMsg
     */
    private void sendMsg(String targetMsg) {
        if (comment_type == COMMENT_RELEASE) {
            releaseComment(targetMsg);
        }
        if (comment_type == COMMENT_REPLY) {
            replyComment(targetMsg);
        }
    }


    /**
     * 发表评论
     *
     * @param targetMsg
     */
    private void releaseComment(final String targetMsg) {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("id", pro_id);
        param.put("content", targetMsg);
        VolleyRequest.volleyRequestPost(Link.PROJECT_RELEASE_COMMENT,
                RequestTag.REQUEST_TAG_PROJECT_RELEASE_COMMENT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        editText.setText("");
                        L.i("-------------" + result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                String uid = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_ID_TAG, "");
                                String re_name = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_NAME_TAG, "");
                                String u_logo = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_IMAGE_TAG, "");
                                String id = GsonUtil.pareJSONObject(result).optString("data");
                                ProjectComment comment = new ProjectComment();
                                comment.user_id = uid;
                                comment.top_id = id;
                                comment.rev_relname = "";
                                comment.rec_id = "";
                                comment.pid = project.info.pid;
                                comment.real_name = re_name;
                                comment.num = "0";
                                comment.logo = u_logo;
                                comment.add_time = String.valueOf(System.currentTimeMillis() / 1000);
                                comment.content = targetMsg;
                                commentList.add(0, comment);
                                commentAdapter.notifyDataSetChanged();
                                comment = null;

                                int count = 0;
                                if (!TextUtils.isEmpty(projectInfo.comments)) {
                                    count = Integer.parseInt(projectInfo.comments);
                                }
                                ++count;
                                projectInfo.comments = String.valueOf(count);
                                updateNumber();

                            } else {
                                Toast.makeText(ProjectDetailsActivity.this, GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                                ErrorDealUtil.dealError(ProjectDetailsActivity.this, GsonUtil.pareCode(result));
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }


    /**
     * 回复评论
     *
     * @param targetMsg
     */
    private void replyComment(final String targetMsg) {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("id", p_id);
        param.put("rec_id", rec_id);
        param.put("content", targetMsg);
        VolleyRequest.volleyRequestPost(Link.PROJECT_REPLY_COMMENT,
                RequestTag.REQUEST_TAG_PROJECT_REPLY_COMMENT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        editText.setText("");
                        inputDialog.dismiss();
                        L.i("-------------" + result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                String uid = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_ID_TAG, "");
                                String re_name = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_NAME_TAG, "");
                                String u_logo = (String) SPUtils.get(ProjectDetailsActivity.this, SPKeys.KEY_USER_IMAGE_TAG, "");
                                String id = GsonUtil.pareJSONObject(result).optString("data");
                                ProjectComment comment = new ProjectComment();
                                comment.user_id = uid;
                                comment.top_id = id;
                                comment.rev_relname = u_name;
                                comment.rec_id = rec_id;
                                comment.pid = p_id;
                                comment.real_name = re_name;
                                comment.num = "0";
                                comment.logo = u_logo;
                                comment.add_time = String.valueOf(System.currentTimeMillis() / 1000);
                                comment.content = targetMsg;
                                commentList.add(0, comment);
                                commentAdapter.notifyDataSetChanged();
                                comment = null;

                                int count = 0;
                                if (!TextUtils.isEmpty(projectInfo.comments)) {
                                    count = Integer.parseInt(projectInfo.comments);
                                }
                                ++count;
                                projectInfo.comments = String.valueOf(count);
                                updateNumber();

                            } else {
                                ErrorDealUtil.dealError(ProjectDetailsActivity.this, GsonUtil.pareCode(result));
                            }
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    private class OnPraiseAvatarClickListener implements View.OnClickListener {

        private ProjectDetail.ProjectPraise praise;

        public OnPraiseAvatarClickListener(ProjectDetail.ProjectPraise projectPraise) {
            this.praise = projectPraise;
        }

        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(praise.id)) {
                return;
            }
            Intent intent = new Intent(ProjectDetailsActivity.this, PersonalCenterActivity.class);
            intent.putExtra(PersonalCenterActivity.KEY_PERSONAL_UID, praise.id);
            startActivity(intent);
        }
    }


    /**
     * 更新contentView
     */
   /* private void updateView() {
        Glide.with(this).load("http://img3.imgtn.bdimg.com/it/u=4265264976,3304730425&fm=21&gp=0.jpg").asBitmap().into(backIcon);
        Glide.with(this).load("http://img1.imgtn.bdimg.com/it/u=4034782898,2246360535&fm=21&gp=0.jpg").asBitmap().into(mPLogo);
        mPName.setText("蛋糕事业联盟体");
        mPDescription.setText("烘焙行业的四位老古董，联合发酵传奇");

        //添加行业标签
        addIndustryTag();

        //设置内容
        Drawable d = getResources().getDrawable(R.mipmap.icon_p_content_tag);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan is = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
        SpannableString ss = new SpannableString(" ");
        ss.setSpan(is, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(ss).append(" 烘焙行业的四位老古董（20年知名品牌高级技师+10年知名品牌Marketing+10年知名品牌技术辅导+10年中央厨房管理）" +
                "，合计成功运营知名烘焙品牌超过300家门店，借互联网+东风再次合力打造移动互联网蛋糕传奇！");
        mPContent.setText(ssb);

        Glide.with(this).load("http://f.hiphotos.baidu.com/image/h%3D200/sign=67ab903616dfa9ece22e5" +
                "11752d1f754/c75c10385343fbf227695409b77eca8065388f57.jpg").asBitmap().into(mPAvatar);
        mPUsername.setText("野小草");

        //添加推荐估值
        addRound();


        //问题列表的显示
//        questions = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            questions.add(new ProjectCard());
//        }
//        questionRecyclerAdapter = new PRecyclerQuestionAdapter(this, questions);
//        mPQRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        mPQRecyclerView.setAdapter(questionRecyclerAdapter);
//        mPQuestion.setVisibility(View.VISIBLE);

        mPQuestionContent.setText(questionContent);

        //显示展示图
        imgs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            imgs.add("");
        }
        imgRecyclerAdapter = new PImgAdapter(this, imgs);
        FullyGridLayoutManager fgm = new FullyGridLayoutManager(this, 3);
        fgm.setSmoothScrollbarEnabled(true);
        fgm.setOrientation(LinearLayoutManager.VERTICAL);
        mPImgRecyclerView.setLayoutManager(fgm);
        mPImgRecyclerView.setAdapter(imgRecyclerAdapter);
        mPImg.setVisibility(View.VISIBLE);


        //添加收藏头像
        addLikeView();


    }*/
}
