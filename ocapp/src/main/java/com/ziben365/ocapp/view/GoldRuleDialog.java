package com.ziben365.ocapp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.util.ScreenUtils;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2016/3/8.
 * email  1956766863@qq.com
 */
public class GoldRuleDialog extends Dialog {
    private LayoutInflater mInflater;
    private View convertView;
    private ImageView imageDelete;

    public GoldRuleDialog(Context context) {
        this(context, R.style.myDialogTheme);
    }

    public GoldRuleDialog(Context context, int themeResId) {
        super(context, themeResId);

        mInflater = LayoutInflater.from(context);
        convertView = mInflater.inflate(R.layout.dialog_user_gold_rule, null);
        setContentView(convertView);
        setCancelable(true);

        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ScreenUtils.getWindowScreenWidth(context) - 100;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        initView(context);
    }

    private void initView(Context context) {
        imageDelete = (ImageView) convertView.findViewById(R.id.id_delete);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
