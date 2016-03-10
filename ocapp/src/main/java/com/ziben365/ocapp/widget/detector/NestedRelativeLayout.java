package com.ziben365.ocapp.widget.detector;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.ziben365.ocapp.R;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/17.
 * email  1956766863@qq.com
 */
public class NestedRelativeLayout extends RelativeLayout {
    private static final int TRANSLATE_DURATION_MILLIS = 200;

    private boolean mVisible;
    private int mScrollThreshold;
    private Interpolator mInterPolator = new AccelerateDecelerateInterpolator();


    public NestedRelativeLayout(Context context) {
        this(context, null);
    }

    public NestedRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mVisible = true;
        mScrollThreshold = getResources().getDimensionPixelOffset(R.dimen.fab_margin);
    }

    public void attachToScrollView(@NonNull ObservableNestedScrollView scrollView) {
        attachToScrollView(scrollView, null, null);
    }

    public void attachToScrollView(@NonNull ObservableNestedScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener) {
        attachToScrollView(scrollView, scrollDirectionListener, null);
    }

    public void attachToScrollView(@NonNull ObservableNestedScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener,
                                   ObservableNestedScrollView.OnScrollChangedListener onScrollChangedListener) {
        NestedScrollViewDetectorImpl scrollDetector = new NestedScrollViewDetectorImpl();
        scrollDetector.setScrollDirectionListener(scrollDirectionListener);
        scrollDetector.setOnScrollChangedListener(onScrollChangedListener);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        scrollView.setOnScrollChangedListener(scrollDetector);
    }


    class NestedScrollViewDetectorImpl extends NestedScrollViewDetector {
        private ScrollDirectionListener mScrollDirectionListener;
        private ObservableNestedScrollView.OnScrollChangedListener mOnScrollChangedListener;

        public void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener) {
            this.mScrollDirectionListener = scrollDirectionListener;
        }

        public void setOnScrollChangedListener(ObservableNestedScrollView.OnScrollChangedListener onScrollListener) {
            this.mOnScrollChangedListener = onScrollListener;
        }

        @Override
        void onScrollUp() {
            hide();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }

        @Override
        void onScrollDown() {
            show();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
        }

        @Override
        public void onScrollChanged(NestedScrollView who, int l, int t, int oldl, int oldt) {
            if (mOnScrollChangedListener != null) {
                mOnScrollChangedListener.onScrollChanged(who, l, t, oldl, oldt);
            }
            super.onScrollChanged(who, l, t, oldl, oldt);
        }
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean animate) {
        toggle(true, animate, false);
    }

    private void toggle(boolean visible, boolean animate, boolean force) {
        if (mVisible != visible) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            currentVto.removeOnPreDrawListener(this);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height - getMarginBottom();
            if (animate) {
                ViewPropertyAnimator.animate(this).setInterpolator(mInterPolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS).translationY(translationY);
            } else {
                ViewHelper.setTranslationY(this, translationY);
            }

            if (!hasHoneycombApi()) {
                setClickable(visible);
            }
        }
    }

    private boolean hasHoneycombApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((MarginLayoutParams) lp).bottomMargin;
        }
        return marginBottom;
    }


    public void show() {
        show(true);
    }

    private void show(boolean animate) {
        toggle(false, animate, false);
    }
}
