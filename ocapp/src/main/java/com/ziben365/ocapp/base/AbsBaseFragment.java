package com.ziben365.ocapp.base;

import android.os.Handler;
import android.support.v4.app.Fragment;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/15.
 * email  1956766863@qq.com
 */
public abstract class AbsBaseFragment extends Fragment {

    protected Handler mHandler = new Handler();


    /**
    protected abstract void initData();

    protected abstract View initView(LayoutInflater paramLayoutInflater);

    protected abstract void initActionListener(View paramView);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater);
        initActionListener(view);
        return view;
    }

     ***/
}
