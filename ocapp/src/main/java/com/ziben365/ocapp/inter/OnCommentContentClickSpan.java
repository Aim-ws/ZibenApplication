package com.ziben365.ocapp.inter;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.PCommentAdapter;
import com.ziben365.ocapp.model.ProjectComment;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/2/25.
 * email  1956766863@qq.com
 */
public class OnCommentContentClickSpan extends ClickableSpan {
    private Context mContext;
    private ProjectComment p_comment;
    private PCommentAdapter.OnReplyCommentClickListener listener;

    public OnCommentContentClickSpan(Context mContext, ProjectComment comment,
                                     PCommentAdapter.OnReplyCommentClickListener replyCommentClickListener) {
        this.listener = replyCommentClickListener;
        this.mContext = mContext;
        this.p_comment = comment;
    }

    @Override
    public void onClick(View widget) {
        if (listener != null) {
            listener.onReplyCommentClick(widget, p_comment);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mContext.getResources().getColor(R.color.color_click_span_text1));
        ds.setUnderlineText(false);
    }
}
