package com.csy.fight.main.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chengshengyang on 2018/1/31.
 *
 * 表情模块--左右滑动ViewPager适配器
 *
 * @author chengshengyang
 */
public class HomePageAdapter extends PagerAdapter {

    private List<View> mPageList;

    public HomePageAdapter(List<View> viewList) {
        this.mPageList = viewList;
    }

    @Override
    public int getCount() {
        return mPageList == null ? 0 : mPageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mPageList.get(position));
        return mPageList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mPageList.get(position));
    }
}
