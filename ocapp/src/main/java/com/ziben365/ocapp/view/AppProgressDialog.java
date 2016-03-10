package com.ziben365.ocapp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.widget.refresh.ProgressStyle;
import com.ziben365.ocapp.widget.refresh.progressindicator.AVLoadingIndicatorView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/22.
 * email  1956766863@qq.com
 */
public class AppProgressDialog extends Dialog {

    public AppProgressDialog(Context context) {
        this(context, R.style.selectorDialog);
    }

    public AppProgressDialog(Context context, int theme) {
        super(context, theme);
        AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        avLoadingIndicatorView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        this.setContentView(avLoadingIndicatorView);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        this.getWindow().getAttributes().alpha = 1.0f;
        this.setCancelable(false);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus){
            dismiss();
        }
    }

}
