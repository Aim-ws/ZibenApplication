package com.ziben365.ocapp.photo.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.photo.adapter.ImageAdapter;
import com.ziben365.ocapp.photo.bean.FolderBean;
import com.ziben365.ocapp.photo.popup.ListImageDirPopupWindow;
import com.ziben365.ocapp.util.L;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2015/12/18.
 * email  1956766863@qq.com
 */
public class PhotoActivity extends AppCompatActivity {

    @InjectView(R.id.id_gridView)
    GridView mGridView;
    @InjectView(R.id.id_dir_name)
    TextView mDirName;
    @InjectView(R.id.id_dir_count)
    TextView mDirCount;
    @InjectView(R.id.id_bottom_ly)
    RelativeLayout mBottomLy;
    @InjectView(R.id.id_complete)
    TextView mComplete;
    @InjectView(R.id.id_back)
    ImageView mBack;
    @InjectView(R.id.id_title)
    TextView mTitle;
    @InjectView(R.id.top_rl)
    RelativeLayout topRl;


    private ImageAdapter mImageAdapter;
    private List<String> mImgs;
    private File mCurrentDir;
    private int mMaxCount;

    public static int mMaxSelectCount = 9;

    private List<String> allImgs;

    private List<FolderBean> mFolderBeans = new ArrayList<>();

    private ProgressDialog mProgressDialog;

    private static final int DATA_LOADED = 0X110;

    private ListImageDirPopupWindow mDirPopupWindow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DATA_LOADED) {
                mProgressDialog.dismiss();
                //绑定数据
                data2View();
                initDirPopupWindow();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
        ButterKnife.inject(this);


        initData();
        initEvent();
    }

    /**
     * 利用ContentProvider扫描手机中的所有图片
     */
    private void initData() {
        mMaxSelectCount = getIntent().getIntExtra("count", 9);
        allImgs = new ArrayList<>();

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用！", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = PhotoActivity.this.getContentResolver();
                Cursor cursor = cr.query(mImgUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> mDirPaths = new HashSet<String>();

                if (cursor == null) return;

                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    allImgs.add(path);
                    File parentFile = new File(path).getParentFile();

                    if (parentFile == null)
                        continue;

                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;

                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }

                    if (parentFile.list() == null)
                        continue;

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") ||
                                    filename.endsWith(".jpeg") ||
                                    filename.endsWith(".png")) {
                                return true;
                            }
                            return false;
                        }
                    }).length;

                    folderBean.setCount(picSize);
                    mFolderBeans.add(folderBean);

                    if (picSize > mMaxCount) {
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                    cursor.moveToNext();
                }


                cursor.close();
                //扫描完成释放临时变量的内存
                //通知handler图片扫描完成
                mHandler.sendEmptyMessage(DATA_LOADED);

            }
        }).start();

    }

    private void initEvent() {
        mDirName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDirPopupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
                mDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
                lightOff();
            }
        });
        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("photos", ImageAdapter.getmSelectedImg());
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    /**
     * 内容区域变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    private void initDirPopupWindow() {
        mDirPopupWindow = new ListImageDirPopupWindow(this, mFolderBeans);
        mDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mDirPopupWindow.setOnDirSelectedListener(new ListImageDirPopupWindow.OnDirSelectedListener() {
            @Override
            public void onSelected(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getDir());
                L.i("----------mCurrentDir-----------" + mCurrentDir);
                mImgs = Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg")
                                || filename.endsWith(".jpeg")
                                || filename.endsWith(".png")) {
                            return true;
                        }
                        return false;
                    }
                }));

                mImageAdapter = new ImageAdapter(PhotoActivity.this, mImgs, mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(mImageAdapter);

                mDirCount.setText("" + folderBean.count);
                if (folderBean.name.contains("/")) {
                    int index = folderBean.name.lastIndexOf("/");
                    mDirName.setText(folderBean.name.substring(index + 1));
                    mTitle.setText(folderBean.name.substring(index + 1));
                } else {
                    mDirName.setText(folderBean.name);
                    mTitle.setText(folderBean.name);
                }
                mDirPopupWindow.dismiss();
            }

            @Override
            public void onSelectedAll() {
                showAllPhoto();
                mDirPopupWindow.dismiss();
            }
        });
    }

    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }


    /**
     * 绑定数据
     */
    private void data2View() {
        if (mCurrentDir == null) {
            Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }

        //显示所有的图片
        showAllPhoto();


//        mImgs = Arrays.asList(mCurrentDir.list());
//        mImageAdapter = new ImageAdapter(this, mImgs, mCurrentDir.getAbsolutePath());
//        mGridView.setAdapter(mImageAdapter);
//
//        mDirCount.setText(mMaxCount + "");
//        if (mCurrentDir.getAbsolutePath().contains("/")){
//            int index = mCurrentDir.getAbsolutePath().lastIndexOf("/");
//            mDirName.setText(mCurrentDir.getAbsolutePath().substring(index+1));
//        }else{
//            mDirName.setText(mCurrentDir.getAbsolutePath());
//        }


    }

    /**
     * 显示所有的图片
     */
    private void showAllPhoto() {
        mImageAdapter = new ImageAdapter(this, allImgs, "");
        mGridView.setAdapter(mImageAdapter);
        mDirCount.setText("" + allImgs.size());
        mDirName.setText("所有图片");
    }

    /**
     * 更新选中的数量
     *
     * @param mSelectedImg
     */
    public void updateText(ArrayList<String> mSelectedImg) {
        mComplete.setText("完成" + mSelectedImg.size() + "/" + mMaxSelectCount);
    }
}
