package com.ziben365.ocapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ziben365.ocapp.fragment.RhythmCardFragment;
import com.ziben365.ocapp.model.ProjectCard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2015/12/15.
 * email  1956766863@qq.com
 */
public class RhythmPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<ProjectCard> mCardList;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    /**
     * 构造方法
     * @param fm
     * @param cardList
     */
    public RhythmPagerAdapter(FragmentManager fm, ArrayList<ProjectCard> cardList) {
        super(fm);
        this.mCardList = cardList;
        Iterator iterator = cardList.iterator();
        mFragments.clear();
        while (iterator.hasNext()){
            ProjectCard entity = (ProjectCard) iterator.next();
            mFragments.add(RhythmCardFragment.getInstance(entity));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     * 添加数据
     * @param cardList
     */
    public void addCardList(ArrayList<ProjectCard> cardList) {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = cardList.iterator();
        while (localIterator.hasNext())
            localArrayList.add(RhythmCardFragment.getInstance((ProjectCard) localIterator.next()));
        if (this.mFragments == null)
            this.mFragments = new ArrayList();
        this.mFragments.addAll(localArrayList);
        this.mCardList.addAll(cardList);
    }

    /**
     * 获取数据
     * @return
     */
    public ArrayList<ProjectCard> getCardList() {
        return mCardList;
    }

    public List<Fragment> getFragments() {
        return this.mFragments;
    }

    public void setCardList(ArrayList<ProjectCard> cardList) {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = cardList.iterator();
        while (localIterator.hasNext())
            localArrayList.add(RhythmCardFragment.getInstance((ProjectCard) localIterator.next()));
        this.mFragments = localArrayList;
        this.mCardList = cardList;
    }

    public void setFragments(ArrayList<Fragment> paramList) {
        this.mFragments = paramList;
    }


}
