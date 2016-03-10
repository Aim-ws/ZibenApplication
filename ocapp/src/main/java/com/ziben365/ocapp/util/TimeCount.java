package com.ziben365.ocapp.util;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.ziben365.ocapp.R;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/18.
 * email  1956766863@qq.com
 *
 * 获取验证码计时器
 */
public class TimeCount extends CountDownTimer{


        private TextView button;

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        public void setButton(TextView btn) {
            this.button = btn;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            button.setClickable(false);
            button.setBackgroundResource(R.drawable.shape_register_yz_bg1);
            button.setText(millisUntilFinished / 1000 + "秒后重发");

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            button.setText("获取验证码");
            button.setBackgroundResource(R.drawable.shape_register_yz_bg);
            button.setClickable(true);
        }

}
