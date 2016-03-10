package com.ziben365.ocapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2016/1/7.
 * email  1956766863@qq.com
 */
public class TakePhotoUitl {
    public static int pick = 0;
    public static final int LOGO_PICK = 0x110;
    public static final int P_SINLE_PICK = 0x111;

    public static final int RESULT_REQUEST_PICK_PHOTO = 0x22;
    public static final int RESULT_REQUEST_SELECT_SINGLE = 0x33;
    public static final int RESULT_REQUEST_PHOTO = 0x44;


    public static File sdcardTempFile = new File(DemoApplication.applicationContect
            .getExternalCacheDir()
            + "temp"
            + System.currentTimeMillis()
            + ".png");

    /**
     * @param
     */
    public static void showPictureDialog(final Activity context) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_pick, null);
        final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
        dialog.setCancelable(true);
        dialog.setContentView(convertView);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ScreenUtils.getWindowScreenWidth(context);
        params.gravity = Gravity.BOTTOM;
//        window.setWindowAnimations(R.style.dialogAnim);
        window.setAttributes(params);


        ((TextView) convertView.findViewById(R.id.id_photo_pick)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //打开相机
                TakePhoto(context);

            }
        });
        ((TextView) convertView.findViewById(R.id.id_photo_album)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoAlbum(context);
            }
        });
        ((TextView) convertView.findViewById(R.id.id_photo_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public static void gotoAlbum(Activity context) {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                "image/*");
        intent.putExtra("output", Uri.fromFile(sdcardTempFile));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", pick == P_SINLE_PICK ? 2 : 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        context.startActivityForResult(intent, RESULT_REQUEST_SELECT_SINGLE);
    }

    private static void TakePhoto(Activity context) {
        // 选择拍照
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(sdcardTempFile));
        context.startActivityForResult(cameraintent, RESULT_REQUEST_PICK_PHOTO);
    }

    public static void cutOutPhoto(Activity context,Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", pick == P_SINLE_PICK ? 2 : 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        context.startActivityForResult(intent, RESULT_REQUEST_PHOTO);
    }

    /**
     * 将返回是的数据保存为bitmap
     *
     * @param data
     */
    public static void sendToNext(ImageView imageView,Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap photo = null;
        if (bundle != null) {
            photo = bundle.getParcelable("data");
            if (photo == null) {
            } else {
                // imageView.setImageBitmap(photo);
            }
            ByteArrayOutputStream baos = null;
            BufferedOutputStream bos = null;
            FileOutputStream fos = null;
            try {
                baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] photodata = baos.toByteArray();
                System.out.println(photodata.toString());

                fos = new FileOutputStream(sdcardTempFile);
                bos = new BufferedOutputStream(fos);
                bos.write(photodata);

            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        imageView.setImageBitmap(photo);
//        if (pick == LOGO_PICK) {
//            mPLogo.setImageBitmap(photo);
//        }
//        if (pick == P_SINLE_PICK) {
//            mPAddImage.setImageBitmap(photo);
//        }
    }

}
