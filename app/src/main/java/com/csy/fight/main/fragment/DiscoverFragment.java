package com.csy.fight.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csy.fight.R;
import com.csy.fight.main.IMainContract;
import com.csy.fight.main.adapter.HomePageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chengshengyang on 2018/1/29.
 *
 * @author chengshengyang
 */

public class DiscoverFragment extends BaseFragment implements IMainContract.IView {

    /**
     * view必须持有presenter对象实例
     */
    protected IMainContract.IPresenter mPresenter;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    Unbinder unbinder;
    @BindView(R.id.tl_top)
    TabLayout mTlTop;

    private List<View> mPageList;
    /**
     * 当前页编号
     */
    private int currIndex = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_2, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEvent();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle();
    }

    @Override
    public void initView() {
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();
        View viewChoiceness = inflater.inflate(R.layout.view_home_page_choiceness, null);
        View viewHot = inflater.inflate(R.layout.view_home_page_hot, null);
        View viewNewest = inflater.inflate(R.layout.view_home_page_newest, null);
        View viewPersonage = inflater.inflate(R.layout.view_home_page_personage, null);

        mPageList = new ArrayList<>();
        mPageList.add(viewChoiceness);
        mPageList.add(viewHot);
        mPageList.add(viewNewest);
        mPageList.add(viewPersonage);
    }

    @Override
    public void setTitle() {
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (isAdded() && mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setTitle(R.string.home_tab_discover);
        }
    }

    @Override
    public void setPresenter(IMainContract.IPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setTitle();
        }
    }

    @Override
    public void initEvent() {
        HomePageAdapter mHomePageAdapter = new HomePageAdapter(mPageList);
        mViewPager.setAdapter(mHomePageAdapter);
        mViewPager.setCurrentItem(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currIndex = position;
                mTlTop.getTabAt(currIndex).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTlTop.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
