package com.ziben365.ocapp.photo.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ziben365.ocapp.R;
import com.ziben365.ocapp.photo.bean.FolderBean;
import com.ziben365.ocapp.photo.util.ImageLoader;
import com.ziben365.ocapp.util.L;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/18.
 * email  1956766863@qq.com
 */
public class ListImageDirPopupWindow extends PopupWindow {

    private int mWidth;
    private int mHeight;

    private View mConvertView;
    private ListView mListView;

    private static final String allPhotos = "所有照片";

    private List<FolderBean> mDatas;

    public interface OnDirSelectedListener{
        void onSelected(FolderBean folderBean);

        void onSelectedAll();
    }

    public OnDirSelectedListener mListener;

    public void setOnDirSelectedListener(OnDirSelectedListener mListener) {
        this.mListener = mListener;
    }

    public ListImageDirPopupWindow(Context context, List<FolderBean> datas) {
        caculateWidthAndHeight(context);

        mConvertView = LayoutInflater.from(context).inflate(R.layout.popup_photo, null);
        mDatas = datas;

        FolderBean folderBean = new FolderBean();
        folderBean.setName(allPhotos);
        int count = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            File file = new File(mDatas.get(i).dir);
            int length = Arrays.asList(file.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg")
                            || filename.endsWith(".jpeg")
                            || filename.endsWith(".png")) {
                        return true;
                    }
                    return false;
                }
            })).size();
            count += length;
        }

        folderBean.setFirstImgPath(mDatas.get(0).firstImgPath);
        folderBean.setCount(count);
        mDatas.add(0,folderBean);


        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initViews(context);

        initEvent();

    }

    private void initViews(Context context) {
        mListView = (ListView) mConvertView.findViewById(R.id.id_listView);
        mListView.setAdapter(new ListDirAdapter(context,0,mDatas));

    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null){
                    if (position == 0){
                        mListener.onSelectedAll();
                    }else{
                        L.e("-----------picture-----------"+mDatas.get(position));
                        mListener.onSelected(mDatas.get(position));
                    }
                }
            }
        });
    }

    /**
     * 计算popupWindow的宽和高
     *
     * @param context
     */
    private void caculateWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.7);


    }


    class ListDirAdapter extends ArrayAdapter<FolderBean> {
        private LayoutInflater mInfalter;
        private List<FolderBean> mDatas;

        public ListDirAdapter(Context context, int resource, List<FolderBean> objects) {
            super(context, resource, objects);
            mInfalter = LayoutInflater.from(context);
            this.mDatas = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInfalter.inflate(R.layout.row_photo_folder, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
                ButterKnife.inject(viewHolder,convertView);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            FolderBean bean = getItem(position);
            //重置
            viewHolder.mDirItemImage.setImageResource(R.mipmap.ic_launcher);

            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(bean.firstImgPath,viewHolder.mDirItemImage);
            viewHolder.mDirItemCount.setText(""+bean.count);
            if (bean.name.contains("/")){
                int index = bean.name.lastIndexOf("/");
                viewHolder.mDirItemName.setText(bean.name.substring(index+1));
            }else{
                viewHolder.mDirItemName.setText(bean.name);
            }
            return convertView;
        }

        /**
         * This class contains all butterknife-injected Views & Layouts from layout file 'row_photo_folder.xml'
         * for easy to all layout elements.
         *
         * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
         */
        class ViewHolder {
            @InjectView(R.id.id_dir_item_image)
            ImageView mDirItemImage;
            @InjectView(R.id.id_dir_item_name)
            TextView mDirItemName;
            @InjectView(R.id.id_dir_item_count)
            TextView mDirItemCount;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }


}
