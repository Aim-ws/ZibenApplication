package com.ziben365.ocapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.photo.ui.PhotoActivity;
import com.ziben365.ocapp.view.AppProgressDialog;

import butterknife.ButterKnife;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/10.
 * email  1956766863@qq.com
 */
public class TestActivity extends AppCompatActivity {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);

        AppProgressDialog progressDialog = new AppProgressDialog(this);
        progressDialog.show();

    }

    public void onClick(View view){
        startActivity(new Intent(this, PhotoActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public <T extends View> T findIdView(int resid){
        return (T)findViewById(resid);
    }



}
