package com.ziben365.ocapp.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ziben365.ocapp.R;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/8.
 * email  1956766863@qq.com
 */
public class SendMessageWindow {
    private LayoutInflater inflater;
    private View convertView;
    private EditText mSendEdit;
    private TextView mSend;
    private OnSendMessageListener sendMessageListener;

    @SuppressLint("NewApi")
    public SendMessageWindow(Activity context) {
        inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.layout_send_msg, null);
        mSendEdit = (EditText) convertView.findViewById(R.id.id_edit_msg);
        mSend = (TextView) convertView.findViewById(R.id.id_send_msg);

        final PopupWindow popupWindow = new PopupWindow(convertView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAsDropDown(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mSendEdit.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {

                } else {
                    if (sendMessageListener != null) {
                        popupWindow.dismiss();
                        sendMessageListener.onSendMessage(msg);
                    }
                }
            }
        });


    }

    public void setOnSendMessageListener(OnSendMessageListener listener){
        this.sendMessageListener = listener;
    }


    public interface OnSendMessageListener {
        void onSendMessage(String msg);
    }


}
