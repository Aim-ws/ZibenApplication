package com.ziben365.ocapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.widget.wheel.OnWheelChangedListener;
import com.ziben365.ocapp.widget.wheel.adapter.ArrayWheelAdapter;
import com.ziben365.ocapp.widget.wheel.view.WheelView;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/11.
 * email  1956766863@qq.com
 */
public class ChoiceCityUtils {

    private View convertView;
    private LayoutInflater inflater;
    private WheelView mProvinceWheel,mCityWheel;
    private OnUpdateSelectCityListener updateListener;


    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    private String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;

    public ChoiceCityUtils(Activity context){
        inflater = LayoutInflater.from(context);

        initView(context);
    }

    private void initView(Activity context) {
        convertView = inflater.inflate(R.layout.popupwindow_city_select,null);
        mProvinceWheel = (WheelView) convertView.findViewById(R.id.id_province_wheelView);
        mCityWheel = (WheelView) convertView.findViewById(R.id.id_city_wheelView);

        PopupWindow popupWindow = new PopupWindow(convertView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);

        initData(context);
    }

    private void initData(final Activity context) {
        AreaUtils areaUtils = new AreaUtils(context);
        mProvinceDatas = areaUtils.getProvinceData();
        mCitisDatasMap = areaUtils.getCityData();

        ArrayWheelAdapter<String> mProvinceAdapter = new ArrayWheelAdapter<String>(context,mProvinceDatas);
        mProvinceWheel.setViewAdapter(mProvinceAdapter);
        mProvinceWheel.setVisibleItems(5);
        mCityWheel.setVisibleItems(5);
        mProvinceWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCity(context);
                if (updateListener!=null){
                    updateListener.onUpdateCity(mCurrentProviceName,mCurrentCityName);
                }
            }
        });
        mCityWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int position = mCityWheel.getCurrentItem();
                mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[position];
                if (updateListener!=null){
                    updateListener.onUpdateCity(mCurrentProviceName,mCurrentCityName);
                }
            }
        });


        updateCity(context);
    }

    private void updateCity(Context context) {
        int current = mProvinceWheel.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[current];
        String[] currentCities = mCitisDatasMap.get(mCurrentProviceName);
        if (currentCities == null){
            currentCities = new String[]{""};
        }
        mCurrentCityName = currentCities[0];
        mCityWheel.setViewAdapter(new ArrayWheelAdapter<String>(context,currentCities));
        mCityWheel.setCurrentItem(0);
    }


    public void setOnUpdateSelectCityListener(OnUpdateSelectCityListener listener){
        this.updateListener = listener;
    }


    public interface OnUpdateSelectCityListener{
        void onUpdateCity(String province,String city);
    }


}
