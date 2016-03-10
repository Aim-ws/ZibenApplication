package com.ziben365.ocapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.util.AreaUtils;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.ScreenUtils;
import com.ziben365.ocapp.widget.wheel.OnWheelChangedListener;
import com.ziben365.ocapp.widget.wheel.adapter.ArrayWheelAdapter;
import com.ziben365.ocapp.widget.wheel.view.WheelView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/8.
 * email  1956766863@qq.com
 */
public class WheelDialog extends Dialog {
    private View convertView;
    private LayoutInflater inflater;
    private WheelView mProvinceWheel,mCityWheel;
    private TextView cancel,ok;

    private OnWheelSelectedListener wheelSelectedListener;

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


    public WheelDialog(Context context) {
        this(context,R.style.myQuestionDialogTheme);
    }

    public WheelDialog(Context context, int themeResId) {
        super(context, themeResId);
        inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.layout_city_choice,null);
        this.setContentView(convertView);
        this.setCancelable(true);

        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ScreenUtils.getWindowScreenWidth(context) - 50;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        initView(context);
    }

    private void initView(Context context) {
        mProvinceWheel = (WheelView) convertView.findViewById(R.id.id_province_wheelView);
        mCityWheel = (WheelView) convertView.findViewById(R.id.id_city_wheelView);
        cancel = (TextView) convertView.findViewById(R.id.id_cancel);
        ok = (TextView) convertView.findViewById(R.id.id_ok);

        initData(context);
    }

    private void initData(final Context context) {
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
            }
        });
        mCityWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int position = mCityWheel.getCurrentItem();
                mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[position];
            }
        });
        updateCity(context);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (wheelSelectedListener!=null){
                    L.i("province------"+mCurrentProviceName + "==city----------"+mCurrentCityName);
                    wheelSelectedListener.OnWheelSelected(mCurrentProviceName,mCurrentCityName);
                }
            }
        });
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mProvinceDatas.length; i++) {
            String[] cities = mCitisDatasMap.get(mProvinceDatas[i]);
            for (int j = 0; j < cities.length; j++) {
                sb.append("\"").append(cities[j]).append("\"").append(",");
            }
        }
        L.i("-----------city--------------"+sb.toString());

        save(context,sb.toString());
    }

    /**
     *@author chenzheng_Java
     *保存用户输入的内容到文件
     */
    private void save(Context context,String msg) {
            String fileName = "android_city.txt";
        try {
            /* 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的，
             * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的
             *   public abstract FileOutputStream openFileOutput(String name, int mode)
             *   throws FileNotFoundException;
             * openFileOutput(String name, int mode);
             * 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
             *          该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt
             * 第二个参数，代表文件的操作模式
             *          MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖
             *          MODE_APPEND  私有   重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件
             *          MODE_WORLD_READABLE 公用  可读
             *          MODE_WORLD_WRITEABLE 公用 可读写
             *  */
//            FileOutputStream outputStream = context.openFileOutput(fileName,
//                    Activity.MODE_PRIVATE);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString(),fileName);
            if (!file.exists()){
                file.createNewFile();
            }
//            file.mkdirs();
            FileOutputStream outputStream = new FileOutputStream(file,true);
            outputStream.write(msg.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    public void setOnWheelSelectedListener(OnWheelSelectedListener listener){
        this.wheelSelectedListener = listener;
    }

    public interface OnWheelSelectedListener{
        void OnWheelSelected(String province,String city);

    }
}
