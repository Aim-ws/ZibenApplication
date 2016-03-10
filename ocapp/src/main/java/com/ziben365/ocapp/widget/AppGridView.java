package com.ziben365.ocapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/4.
 * email  1956766863@qq.com
 */
public class AppGridView extends GridView {
    public AppGridView(Context context) {
        super(context);
    }

    public AppGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
