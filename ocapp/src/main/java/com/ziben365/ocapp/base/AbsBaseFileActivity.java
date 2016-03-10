package com.ziben365.ocapp.base;

import android.content.Intent;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/28.
 * email  1956766863@qq.com
 */
public class AbsBaseFileActivity extends BaseActivity {
    protected static final int FILE_CHOOSE_CODE = 0X110;

    protected void chooseFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,""),FILE_CHOOSE_CODE);
    }
}
