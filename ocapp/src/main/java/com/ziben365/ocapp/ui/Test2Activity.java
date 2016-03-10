package com.ziben365.ocapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.base.BaseActivity;
import com.ziben365.ocapp.widget.flowlayout.FlowLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/22.
 * email  1956766863@qq.com
 */
public class Test2Activity extends BaseActivity {

    @InjectView(R.id.id_flow_layout)
    FlowLayout mFlowLayout;

    private String[] source = new String[]{"z最优资本", "app", "button", "textView", "created", "contains", "template", "administrator", "hello world", "great wall", "末日帝国"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ButterKnife.inject(this);

        initData();
    }

    /**
     *
     */
    private void initData() {
        for (int i = 0; i < source.length; i++) {
            TextView textView = new TextView(this);
            textView.setPadding(20, 10, 20, 10);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = 10;
            lp.leftMargin = 10;
            lp.topMargin = 7;
            lp.bottomMargin = 7;
            textView.setLayoutParams(lp);
            textView.setText(source[i]);
            textView.setBackgroundColor(Color.GRAY);
            textView.setOnClickListener(new TextClickListener(source[i]));
            mFlowLayout.addView(textView, lp);
        }


    }


    class TextClickListener implements View.OnClickListener {
        private String s;
        public TextClickListener(String s) {
            this.s = s;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(Test2Activity.this,""+s,Toast.LENGTH_SHORT).show();
        }
    }
}


