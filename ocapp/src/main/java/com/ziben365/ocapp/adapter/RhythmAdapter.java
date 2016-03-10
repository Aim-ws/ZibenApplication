package com.ziben365.ocapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.model.ProjectCard;
import com.ziben365.ocapp.qiniu.QiNiuConfig;
import com.ziben365.ocapp.widget.rhythm.RhythmLayout;

import java.util.ArrayList;
import java.util.List;

public class RhythmAdapter extends BaseAdapter {

    /**
     * item的宽度
     */
    private float itemWidth;
    /**
     * 数据源
     */
    private List<ProjectCard> mCardList;

    private LayoutInflater mInflater;
    private Context mContext;
    private RhythmLayout mRhythmLayout;

    public RhythmAdapter(Context context, RhythmLayout rhythmLayout, List<ProjectCard> cardList) {
        this.mContext = context;
        this.mRhythmLayout = rhythmLayout;
        this.mCardList = new ArrayList();
        this.mCardList.addAll(cardList);
        if (context != null)
            this.mInflater = LayoutInflater.from(context);
    }

    public List<ProjectCard> getCardList() {
        return this.mCardList;
    }

    public void addCardList(List<ProjectCard> cardList) {
        mCardList.addAll(cardList);
    }

    public int getCount() {
        return this.mCardList.size();
    }

    public Object getItem(int position) {
        return this.mCardList.get(position);
    }

    public long getItemId(int paramInt) {
//        return (this.mCardList.get(paramInt)).getUid();
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout relativeLayout = (RelativeLayout) this.mInflater.inflate(R.layout.adapter_rhythm_icon, null);
        //设置item布局的大小以及Y轴的位置
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams((int) itemWidth, mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height)));
        relativeLayout.setTranslationY(itemWidth);

        //设置第二层RelativeLayout布局的宽和高
        RelativeLayout childRelativeLayout = (RelativeLayout) relativeLayout.getChildAt(0);
        int relativeLayoutWidth = (int) itemWidth - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin);
        childRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayoutWidth, mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height) - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin)));

        ImageView imageIcon = (ImageView) relativeLayout.findViewById(R.id.image_icon);
        //计算ImageView的大小
        int iconSize = (relativeLayoutWidth - 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin));
        ViewGroup.LayoutParams iconParams = imageIcon.getLayoutParams();
        iconParams.width = iconSize;
        iconParams.height = iconSize;
        imageIcon.setLayoutParams(iconParams);
        imageIcon.setBackgroundResource(R.mipmap.ic_launcher);
        //设置背景图片
//        imageIcon.setBackgroundResource(ScreenUtils.getDrawableIdByName(mContext, mCardList.get(position).getIconUrl()));
        ProjectCard entity = mCardList.get(position);
        Glide.with(DemoApplication.applicationContect).load(QiNiuConfig.QINIU_PIC_URL + entity.logo + "!w100").crossFade().centerCrop().into(imageIcon);

        return relativeLayout;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.mRhythmLayout.invalidateData();
    }

    public void setCardList(List<ProjectCard> paramList) {
        this.mCardList = paramList;
    }

    /**
     * 设置每个item的宽度
     */
    public void setItemWidth(float width) {
        this.itemWidth = width;
    }
}