package com.ziben365.ocapp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.PAddImageAdapter;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.model.ProjectRecommendReason;
import com.ziben365.ocapp.model.Tag;
import com.ziben365.ocapp.photo.ui.PhotoActivity;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.util.ErrorDealUtil;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPKeys;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.TakePhotoUitl;
import com.ziben365.ocapp.util.UploadUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.view.WheelDialog;
import com.ziben365.ocapp.widget.AppGridView;
import com.ziben365.ocapp.widget.RoundedImageView;
import com.ziben365.ocapp.widget.flowlayout.FlowLayout;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/13.
 * email  1956766863@qq.com
 */
public class AddProjectActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.id_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.id_p_logo)
    RoundedImageView mPLogo;
    @InjectView(R.id.id_p_edit_name)
    EditText mPEditName;
    @InjectView(R.id.id_p_edit_one_description)
    EditText mPEditOneDescription;
    @InjectView(R.id.id_img_gridView)
    AppGridView mImgGridView;
    @InjectView(R.id.id_p_edit_city)
    TextView mPEditCity;
    @InjectView(R.id.id_p_add_tag_flowLayout)
    FlowLayout mPAddTagFlowLayout;
    @InjectView(R.id.id_p_edit_value)
    EditText mPEditValue;
    @InjectView(R.id.id_p_edit_description)
    EditText mPEditDescription;
    @InjectView(R.id.id_tag_flowLayout)
    FlowLayout mTagFlowLayout;
    @InjectView(R.id.id_p_add_tag)
    TextView mPAddTag;
    @InjectView(R.id.id_complete)
    TextView mComplete;
    @InjectView(R.id.id_rg_status)
    RadioGroup mRgStatus;
    @InjectView(R.id.id_p_edit_recommend)
    EditText mPEditRecommend;

    private static final int ADD_IMAGE = 0x55;
    private static final int ADD_TAG = 0x101;


    private PAddImageAdapter imageAdapter;
    private List<String> imgs;

    ArrayList<Tag> tags = new ArrayList<>();

    private ProjectRecommendReason pr_reason;

    private ProgressDialog progressDialog;
    private String logo;
    private String p_name;
    private String p_description;
    private String p_city;
    private String p_status;
    private String p_tag;
    private String p_value;
    private String p_one_description;
    private String p_reason;
    private int pic_count = 0;
    boolean isStart = false;
    private boolean isBroadcast = false;

    private String logoPath;
    private String u_id;

    /***********
     * 显示已回答问题
     ************/
//    private PRecyclerAddQuestionAdapter questionAdapter;
//    private List<ProjectCard> questions = new ArrayList<>();

//    private PRecyclerStatusAdapter statusAdapter;
//    private List<ProjectStatus> statusList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        ButterKnife.inject(this);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
*/
        initToolbar();
        initEvent();
        initData();
    }


    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initEvent() {
        mPLogo.setOnClickListener(this);
        mPAddTag.setOnClickListener(this);
        mPEditCity.setOnClickListener(this);
        mImgGridView.setOnItemClickListener(this);
        mRgStatus.setOnCheckedChangeListener(this);
        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }

    private void initData() {
        p_status = "0";    //默认项目开发中
        imgs = new ArrayList<>();
        imageAdapter = new PAddImageAdapter(this, imgs);
        mImgGridView.setAdapter(imageAdapter);

        u_id = (String) SPUtils.get(this,SPKeys.KEY_USER_ID_TAG,"");
        isBroadcast = getIntent().getBooleanExtra(NotifyAction.KEY_RECOMMEND_BROADCAST,false);

        L.i("-------------uid-----------"+SPUtils.get(this,SPKeys.KEY_USER_ID_TAG,"").toString());
        tags.clear();

        /***********显示已回答问题************/
//        mAddQRecyclerView.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        questionAdapter = new PRecyclerAddQuestionAdapter(this, questions);
//        mAddQRecyclerView.setAdapter(questionAdapter);

        progressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);


        initQuestionTag();
    }

    private void initQuestionTag() {
        String reasons = (String) SPUtils.get(DemoApplication.applicationContect,
                SPKeys.KEY_PROJECT_RECOMMEND_REASON, "");
        if (!TextUtils.isEmpty(reasons)) {
            pr_reason = GsonUtil.getInstance().fromJson(reasons, ProjectRecommendReason.class);
            addQuestionTag(pr_reason.recreason);
        } else {
            requestReason();
        }

    }

    /*******
     * 请求项目推荐理由
     ******/
    private void requestReason() {
        VolleyRequest.volleyRequestPost(Link.PROJECT_RECOMMEND_REASON, RequestTag.REQUEST_TAG_REASON, new VolleyInterface() {
            @Override
            public void onSuccess(Object o) {
                L.i("----------------------------" + o.toString());
                int code = GsonUtil.pareCode(o.toString());
                if (code == RequestCode.SUCCESS) {
                    JSONObject object = GsonUtil.pareJSONObject(o.toString());
                    JSONObject array = object.optJSONObject("data");
                    SPUtils.put(DemoApplication.applicationContect,
                            SPKeys.KEY_PROJECT_RECOMMEND_REASON,
                            array.toString());
                    initQuestionTag();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

        /***
         VolleyRequest.volleyJSONObjectMsgpack(Link.PROJECT_RECOMMEND_REASON, RequestTag.REQUEST_TAG_REASON,
         new VolleyJSONObjectInterface() {
        @Override public void onSuccess(Object t) {

        }

        @Override public void onError(VolleyError error) {

        }
        });

         */
    }

    private void addQuestionTag(String[] reason) {
        LayoutInflater inflater = LayoutInflater.from(this);
        mTagFlowLayout.removeAllViews();
        for (int i = 0; i < reason.length; i++) {
            View itemView = inflater.inflate(R.layout.row_p_tag, null);
            TextView tagView = (TextView) itemView.findViewById(R.id.id_p_tag);
//            Tag t = tagList.get(i);
//            if (t.checked) {
//                tagView.setBackgroundResource(R.drawable.shape_p_tag_pressed);
//                tagView.setTextColor(getResources().getColor(R.color.colorAppbar));
//            } else {
            tagView.setBackgroundResource(R.drawable.shape_p_tag_normal);
            tagView.setTextColor(getResources().getColor(R.color.color_text_89));
//            }
            tagView.setText(reason[i]);
//            tagView.setOnClickListener(new QuestionTagViewClickListener(i));
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 15;
            lp.leftMargin = 15;
            lp.topMargin = 10;
            lp.bottomMargin = 10;
            mTagFlowLayout.addView(itemView, lp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_p_logo:
                TakePhotoUitl.showPictureDialog(this);
                break;
            case R.id.id_p_add_tag:
                Intent intent = new Intent(this, TagActivity.class);
                L.i("===============" + (4 - tags.size()));
                intent.putExtra("count", (4 - tags.size()));
                startActivityForResult(intent, ADD_TAG);
                break;
            case R.id.id_p_edit_city:
                showCityDialog();
                break;
        }
    }

    /**
     * 校验数据
     */
    private void checkData() {
        p_name = mPEditName.getText().toString().trim();
        p_one_description = mPEditOneDescription.getText().toString().trim();
        p_city = mPEditCity.getText().toString().trim();
        p_description = mPEditDescription.getText().toString().trim();
        p_reason = mPEditRecommend.getText().toString().trim();
        p_value = mPEditValue.getText().toString().trim();

        L.i("" + p_name + "\n" + p_one_description + "\n" + p_city + "\n" + p_description + "\n" + p_reason);

        if (StringUtils.isEmpty(logoPath)) {
            Toast.makeText(this, "请选择项目logo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(p_name)) {
            Toast.makeText(this, "项目名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(p_one_description)) {
            Toast.makeText(this, "一句话简介不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (p_one_description.length() > 30) {
            Toast.makeText(this, "一句话简介最多30字", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(p_city)) {
            Toast.makeText(this, "请选择项目所在城市", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tags.size() == 0) {
            Toast.makeText(this, "请为项目添加标签", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(p_value)) {
            Toast.makeText(this, "请填写项目估值", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(p_description)) {
            Toast.makeText(this, "请填写项目概述", Toast.LENGTH_SHORT).show();
            return;
        }
        if (p_description.length() < 30) {
            Toast.makeText(this, "项目概述不少于30字", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(p_reason)) {
            Toast.makeText(this, "请填写项目推荐理由", Toast.LENGTH_SHORT).show();
            return;
        }
        if (p_reason.length() < 30) {
            Toast.makeText(this, "项目推荐理由不少于30字", Toast.LENGTH_SHORT).show();
            return;
        }


        if (imgs.size() == 0) {
            Toast.makeText(this, "请选择项目封面图", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imgs.size() < 2) {
            Toast.makeText(this, "最少选择一张项目图片", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tags.size(); i++) {
            if (i == 0) {
                sb.append(tags.get(i).tagName);
            } else {
                sb.append(" ").append(tags.get(i).tagName);
            }
        }
        p_tag = sb.toString();
        submitImages();
    }

    /**
     * 提交图片
     */
    private void submitImages() {
        progressDialog.setMax(100);
        progressDialog.setMessage("正在上传用户头像");
        progressDialog.show();
        //提交logo
        UploadUtils.getInstance().clearArray();
        UploadUtils.getInstance().uploadImage(TakePhotoUitl.sdcardTempFile.getAbsolutePath(),
                QiNiuConfig.getToken(),
                new UploadUtils.OnUploadImageListener() {
                    @Override
                    public void onSuccess(String fileUrl) {
                        logo = UploadUtils.getInstance().getmImages().get(0);
                        //提交第一张产品图片
                        UploadUtils.getInstance().clearArray();
                        uploadProjectImage(imgs.get(pic_count));
                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                    }

                    @Override
                    public void onProgress(String s, double progress) {
                        progressDialog.setProgress((int) progress);
                    }
                });
    }

    /**
     * 上传产品图片
     *
     * @param s
     */
    private void uploadProjectImage(String s) {
        progressDialog.setProgress(0);
        int index = pic_count + 1;
        progressDialog.setMessage("正在上传第" + index + "张图片");
        UploadUtils.getInstance().uploadImage(s, QiNiuConfig.getToken(),
                new UploadUtils.OnUploadImageListener() {
                    @Override
                    public void onSuccess(String fileUrl) {
                        //循环提交图片
                        ++pic_count;
                        int index = pic_count + 1;
                        progressDialog.setMessage("正在上传第" + index + "张图片");
                        progressDialog.setProgress(0);
                        if (pic_count < imgs.size()) {
                            uploadProjectImage(imgs.get(pic_count));
                        } else {
                            progressDialog.dismiss();
                            submitData();
                        }
                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                    }

                    @Override
                    public void onProgress(String s, double progress) {
                        progressDialog.setProgress((int) progress);
                    }
                });
    }

    /**
     * 提交数据
     */
    private void submitData() {
        final ProgressDialog mProgressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setMessage("正在提交项目请稍等...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        List<String> images = UploadUtils.getInstance().getmImages();
        String p_banner = images.get(0);
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < images.size(); i++) {
            if (i == 1) {
                sb.append(images.get(i));
            } else {
                sb.append(";").append(images.get(i));
            }
        }
        Map<String, String> param = new HashMap<>();
        param.put("pname", p_name);
        param.put("logo", logo);
        param.put("intro", p_one_description);
        param.put("banner", p_banner);
        param.put("pics", sb.toString());
        param.put("city", p_city);
        param.put("tags", p_tag);
        param.put("product_status", p_status);
        param.put("valuation", p_value);
        param.put("pdesc", p_description);
        param.put("recreason", p_reason);
        param.put("uid", u_id);
        param.put("token",SPUtils.getToken(this));
        VolleyRequest.volleyRequestPost(Link.PROJECT_ADD_PROJECT, RequestTag.REQUEST_TAG_REC_PROJECT,
                param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        L.i("----------------------------" + o.toString());
                        mProgressDialog.dismiss();
                        if (StringUtils.isNotEmpty(o.toString())) {
                            if (GsonUtil.pareCode(o.toString()) == RequestCode.SUCCESS){
                                Toast.makeText(AddProjectActivity.this, GsonUtil.pareMsg(o.toString()), Toast.LENGTH_SHORT).show();
                                if (isBroadcast){
                                    Intent intent = new Intent(NotifyAction.ACTION_UPDATE_USER_RECOMMEND);
                                    sendBroadcast(intent);
                                }
                                onBackPressed();
                            }else{
                                ErrorDealUtil.dealError(AddProjectActivity.this,GsonUtil.pareCode(o.toString()));
                            }

                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }


    /**
     * 城市选择
     */
    private void showCityDialog() {
        WheelDialog wheelDialog = new WheelDialog(this);
        wheelDialog.setOnWheelSelectedListener(new WheelDialog.OnWheelSelectedListener() {
            @Override
            public void OnWheelSelected(String province, String city) {
                mPEditCity.setText(province + " " + city);
            }
        });
        wheelDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TakePhotoUitl.RESULT_REQUEST_PICK_PHOTO:
                    TakePhotoUitl.cutOutPhoto(AddProjectActivity.this, Uri.fromFile(TakePhotoUitl.sdcardTempFile));
                    break;
                case TakePhotoUitl.RESULT_REQUEST_PHOTO:
                    logoPath = TakePhotoUitl.sdcardTempFile.getAbsolutePath();
                    TakePhotoUitl.sendToNext(mPLogo, data);
                    break;
                case TakePhotoUitl.RESULT_REQUEST_SELECT_SINGLE:
                    logoPath = TakePhotoUitl.sdcardTempFile.getAbsolutePath();
                    Bitmap bitmap = BitmapFactory.decodeFile(TakePhotoUitl.sdcardTempFile.getAbsolutePath());
                    mPLogo.setImageBitmap(bitmap);
                    break;
                case ADD_IMAGE:
                    ArrayList<String> imageData = data.getStringArrayListExtra("photos");
                    imgs.addAll(imageData);
                    imageAdapter.notifyDataSetChanged();
                    break;
                case ADD_TAG:
                    ArrayList<String> dataNames = data.getStringArrayListExtra("tagName");
                    ArrayList<Integer> dataIds = data.getIntegerArrayListExtra("tagId");
                    if (dataNames.size() > 0) {
                        mPAddTag.setText("");
                        for (int i = 0; i < dataNames.size(); i++) {
                            Tag tag = new Tag(dataIds.get(i), dataNames.get(i), true);
                            tags.add(tag);
                        }
                        addPTag();
                    }


                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == imageAdapter.getCount() - 1) {
            Intent intent = new Intent(AddProjectActivity.this, PhotoActivity.class);
            intent.putExtra("count", 9 - imgs.size());
            startActivityForResult(intent, ADD_IMAGE);
        } else {
            imgs.remove(position);
            imageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加标签
     */
    private void addPTag() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View itemView;
        mPAddTagFlowLayout.removeAllViews();
        int count = tags.size() + 1;
        for (int i = 0; i < count; i++) {
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 15;
            lp.leftMargin = 15;
            lp.topMargin = 10;
            lp.bottomMargin = 10;
            if (i != tags.size()) {
                itemView = inflater.inflate(R.layout.row_tag, null);
                TextView text = (TextView) itemView.findViewById(R.id.id_tag_name);
                ImageView delete = (ImageView) itemView.findViewById(R.id.id_tag_delete);

                delete.setVisibility(View.VISIBLE);
                text.setText(tags.get(i).tagName);
                itemView.setBackgroundResource(R.drawable.shape_tag_pressed);
                itemView.setLayoutParams(lp);
                itemView.setOnClickListener(new OnDeleteTagListener(i));
                mPAddTagFlowLayout.addView(itemView, lp);
            } else {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.mipmap.ic_p_add_image);
                imageView.setOnClickListener(new OnAddTagListener());
                mPAddTagFlowLayout.addView(imageView, lp);
            }


        }
    }

    /**
     * “0”----->开发中
     * “1”----->开发完成
     * “2”----->众筹中
     * “3”----->盈利中
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.id_rb_1:
                p_status = "0";
                break;
            case R.id.id_rb_2:
                p_status = "1";
                break;
            case R.id.id_rb_3:
                p_status = "2";
                break;
            case R.id.id_rb_4:
                p_status = "3";
                break;
        }
    }


    private class OnDeleteTagListener implements View.OnClickListener {
        private int position;

        public OnDeleteTagListener(int i) {
            this.position = i;
        }

        @Override
        public void onClick(View v) {
            tags.remove(position);
            if (tags.size() != 0) {
                addPTag();
            } else {
                mPAddTagFlowLayout.removeAllViews();
                mPAddTag.setText("添加标签");
            }
        }
    }

    private class OnAddTagListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddProjectActivity.this, TagActivity.class);
            intent.putExtra("count", 4 - tags.size());
            startActivityForResult(intent, ADD_TAG);
        }
    }


    /**
     * 标签点击事件
     */
//    class QuestionTagViewClickListener implements View.OnClickListener {
//        int position;
//
//        public QuestionTagViewClickListener(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (position < tagList.size()) {
//                if (tagList.get(position).checked) {
//                    tagList.get(position).setChecked(false);
//                } else {
//                    tagList.get(position).setChecked(true);
//                    showQuestion();
//                }
//            }
//            addQuestionTag();
//        }
//    }

    /**
     * 回答问题
     */
//    private void showQuestion() {
//        View convertView = LayoutInflater.from(this).inflate(R.layout.layout_q_add_question, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.myQuestionDialogTheme);
//        builder.setCancelable(true);
//        builder.setView(convertView);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                questions.add(new ProjectCard());
//                questionAdapter.notifyDataSetChanged();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        Dialog dialog = builder.create();
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = ScreenUtils.getWindowScreenWidth(this) - 50;
//        params.gravity = Gravity.CENTER;
//        window.setAttributes(params);
//        dialog.show();
//
//
//    }
}
