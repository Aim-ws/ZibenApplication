package com.ziben365.ocapp.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.PAddImageAdapter;
import com.ziben365.ocapp.model.Tag;
import com.ziben365.ocapp.photo.ui.PhotoActivity;
import com.ziben365.ocapp.util.ScreenUtils;
import com.ziben365.ocapp.util.TakePhotoUitl;
import com.ziben365.ocapp.util.WindowUtil;
import com.ziben365.ocapp.view.WheelDialog;
import com.ziben365.ocapp.widget.AppGridView;
import com.ziben365.ocapp.widget.CircleImageView;
import com.ziben365.ocapp.widget.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2016/1/7.
 * email  1956766863@qq.com
 */
public class ReleaseProjectActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.id_p_logo)
    CircleImageView mPLogo;
    @InjectView(R.id.id_p_edit_name)
    EditText mPEditName;
    @InjectView(R.id.id_p_edit_city)
    TextView mPEditCity;
    @InjectView(R.id.id_p_edit_status)
    EditText mPEditStatus;
    @InjectView(R.id.id_p_edit_value)
    EditText mPEditValue;
    @InjectView(R.id.id_p_edit_description)
    EditText mPEditDescription;
    @InjectView(R.id.id_p_add_image)
    ImageView mPAddImage;
    @InjectView(R.id.id_tag_flowLayout)
    FlowLayout mTagFlowLayout;
    @InjectView(R.id.id_p_add_tag_flowLayout)
    FlowLayout mPAddTagFlowLayout;
    @InjectView(R.id.id_app_gridView)
    AppGridView mAppGridView;

    private List<Tag> tagList;

    private List<String> imageList;
    private PAddImageAdapter imageAdapter;

    private static final int ADD_IMAGE = 0x55;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_project);
        ButterKnife.inject(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhotoUitl.pick = TakePhotoUitl.LOGO_PICK;
                TakePhotoUitl.showPictureDialog(ReleaseProjectActivity.this);
            }
        });
        mPAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhotoUitl.pick = TakePhotoUitl.P_SINLE_PICK;
                TakePhotoUitl.showPictureDialog(ReleaseProjectActivity.this);
            }
        });
        mPEditCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityDialog();
            }
        });

        tagList = new ArrayList<>();
        tagList.add(new Tag(0, "市场潜力？", true));
        tagList.add(new Tag(1, "用户满意度？", false));
        tagList.add(new Tag(2, "其他的？", false));
        tagList.add(new Tag(3, "商业模式？", false));
        tagList.add(new Tag(4, "团队力量与优势？", false));
        tagList.add(new Tag(5, "行业规划与发展？", false));
        tagList.add(new Tag(6, "项目亮点、优势", false));
        tagList.add(new Tag(7, "项目专利申请量？", false));
        tagList.add(new Tag(8, "盈利？", false));

        addTag();

        addPTag();

        imageList = new ArrayList<>();
        imageAdapter = new PAddImageAdapter(this,imageList);
        mAppGridView.setAdapter(imageAdapter);
        mAppGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == imageAdapter.getCount()-1){
                    startActivityForResult(new Intent(ReleaseProjectActivity.this, PhotoActivity.class),ADD_IMAGE);
                }
            }
        });
    }

    /**
     * 城市选择
     */
    private void showCityDialog() {
        WheelDialog wheelDialog = new WheelDialog(this);
        wheelDialog.show();
    }

    private void addPTag() {
        for (int i = 0; i < 6; i++) {
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 15;
            lp.leftMargin = 15;
            lp.topMargin = 10;
            lp.bottomMargin = 10;
            TextView text = new TextView(this);
            text.setText("移动互联网");
            text.setPadding(8, 8, 8, 8);
            text.setTextColor(getResources().getColor(R.color.colorAppbar));
            text.setLayoutParams(lp);
            mPAddTagFlowLayout.addView(text, lp);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TakePhotoUitl.RESULT_REQUEST_PICK_PHOTO:
                    TakePhotoUitl.cutOutPhoto(ReleaseProjectActivity.this,Uri.fromFile(TakePhotoUitl.sdcardTempFile));
                    break;
                case TakePhotoUitl.RESULT_REQUEST_PHOTO:
                    if (TakePhotoUitl.pick == TakePhotoUitl.LOGO_PICK){
                        TakePhotoUitl.sendToNext(mPLogo,data);
                    }
                    if (TakePhotoUitl.pick == TakePhotoUitl.P_SINLE_PICK){
                        TakePhotoUitl.sendToNext(mPAddImage,data);
                    }
                    break;
                case TakePhotoUitl.RESULT_REQUEST_SELECT_SINGLE:
                    Bitmap bitmap = BitmapFactory.decodeFile(TakePhotoUitl.sdcardTempFile.getAbsolutePath());
                    if (TakePhotoUitl.pick == TakePhotoUitl.LOGO_PICK) {
                        mPLogo.setImageBitmap(bitmap);
                    }
                    if (TakePhotoUitl.pick == TakePhotoUitl.P_SINLE_PICK) {
                        mPAddImage.setImageBitmap(bitmap);
                    }
                    break;
                case ADD_IMAGE:
                    ArrayList<String> imageData = data.getStringArrayListExtra("photos");
                    imageList.clear();
                    imageList.addAll(imageData);
                    imageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    /**
     * 添加标签
     */
    private void addTag() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mTagFlowLayout.removeAllViews();
        for (int i = 0; i < tagList.size(); i++) {
            View itemView = inflater.inflate(R.layout.row_p_tag, null);
            TextView tagView = (TextView) itemView.findViewById(R.id.id_p_tag);
            Tag t = tagList.get(i);
            if (t.checked) {
                tagView.setBackgroundResource(R.drawable.shape_p_tag_pressed);
                tagView.setTextColor(getResources().getColor(R.color.colorAppbar));
            } else {
                tagView.setBackgroundResource(R.drawable.shape_p_tag_normal);
                tagView.setTextColor(getResources().getColor(R.color.color_text_89));
            }
            tagView.setText(t.tagName);
            tagView.setOnClickListener(new TagViewClickListener(i));
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


    /**
     * 标签点击事件
     */
    class TagViewClickListener implements View.OnClickListener {
        int position;

        public TagViewClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (position < tagList.size()) {
                if (tagList.get(position).checked) {
                    tagList.get(position).setChecked(false);
                } else {
                    tagList.get(position).setChecked(true);
                    showQuestion();
                }
            }
            addTag();
        }
    }

    /**
     * 回答问题
     */
    private void showQuestion() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.layout_q_add_question, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.myQuestionDialogTheme);
        builder.setCancelable(true);
        builder.setView(convertView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ScreenUtils.getWindowScreenWidth(this) - 50;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        dialog.show();


    }
}
