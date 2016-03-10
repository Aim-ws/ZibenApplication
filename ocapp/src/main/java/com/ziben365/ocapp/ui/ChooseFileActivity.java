package com.ziben365.ocapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.AbsBaseFileActivity;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/28.
 * email  1956766863@qq.com
 */
public class ChooseFileActivity extends AbsBaseFileActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);
    }

    public void onClick(View view){
        chooseFile();
    }


}
