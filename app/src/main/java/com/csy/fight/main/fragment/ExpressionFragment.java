package com.csy.fight.main.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csy.fight.R;
import com.csy.fight.main.adapter.HomePageAdapter;
import com.csy.fight.util.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chengshengyang on 2018/1/29.
 *
 * @author chengshengyang
 */

public class ExpressionFragment extends BaseFragment {

    @BindView(R.id.top_tab_1)
    TextView mTopTab1;
    @BindView(R.id.top_tab_2)
    TextView mTopTab2;
    @BindView(R.id.top_tab_3)
    TextView mTopTab3;
    @BindView(R.id.top_tab_4)
    TextView mTopTab4;
    @BindView(R.id.linearLayout)
    LinearLayout mLinearLayout;
    @BindView(R.id.scrollbar)
    ImageView mScrollbar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private List<View> mPageList;
    private HomePageAdapter mHomePageAdapter;
    /** 当前页编号 */
    private int currIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_1, container, false);
        ButterKnife.bind(this, view);

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
        if (isAdded()) {
            mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            setTitle();
        }
    }

    @Override
    public void setTitle() {
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.home_tab_expression);
        }
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.top_tab_1, R.id.top_tab_2, R.id.top_tab_3, R.id.top_tab_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_tab_1:
                currIndex = 0;
                setCurrPage();
                break;
            case R.id.top_tab_2:
                currIndex = 1;
                setCurrPage();
                break;
            case R.id.top_tab_3:
                currIndex = 2;
                setCurrPage();
                break;
            case R.id.top_tab_4:
                currIndex = 3;
                setCurrPage();
                break;
            default:
                break;
        }
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
    public void initEvent() {
        mHomePageAdapter = new HomePageAdapter(mPageList);
        mViewPager.setAdapter(mHomePageAdapter);
        mViewPager.setCurrentItem(0);

        initTabItemSelectedAnimation();
    }

    /**
     * 初始化切换分类的动画
     */
    private void initTabItemSelectedAnimation() {
        int screenW = DeviceUtils.getScreenWidth(getContext());
        // 滚动条宽度
        int bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.ic_scroll_line).getWidth();
        // 滚动条初始偏移量
        int offset = (screenW / 4 - bmpW) / 2;
        // scrollbar的一倍滚动量
        final int one = offset * 2 + bmpW;
        Matrix matrix = new Matrix();
        // 将滚动条的初始位置设置成与左边界间隔一个offset
        matrix.postTranslate(offset, 0);
        mScrollbar.setImageMatrix(matrix);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /**
                 * TranslateAnimation的四个属性分别为
                 * float fromXDelta 动画开始的点离当前View X坐标上的差值
                 * float toXDelta 动画结束的点离当前View X坐标上的差值
                 * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                 * float toYDelta 动画开始的点离当前View Y坐标上的差值
                 **/
                Animation animation = new TranslateAnimation(one * currIndex,
                        one * position, 0, 0);
                currIndex = position;
                // 将此属性设置为true可以使得图片停在动画结束时的位置
                animation.setFillAfter(true);
                animation.setDuration(300);
                mScrollbar.startAnimation(animation);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setCurrPage() {
        mViewPager.setCurrentItem(currIndex);
    }
}
