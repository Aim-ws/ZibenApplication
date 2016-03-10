package com.ziben365.ocapp.widget.detector;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/17.
 * email  1956766863@qq.com
 */
public class ObservableNestedScrollView extends NestedScrollView {
    private OnScrollChangedListener mOnScrollChangedListener;


    public ObservableNestedScrollView(Context context) {
        super(context);
    }

    public ObservableNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mOnScrollChangedListener){
                mOnScrollChangedListener.onScrollChanged(this,l,t,oldl,oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener){
        this.mOnScrollChangedListener = onScrollChangedListener;
    }


    public interface OnScrollChangedListener{
        void onScrollChanged(NestedScrollView who, int l, int t, int oldl, int oldt);
    }
}
