package com.csy.fight.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csy.fight.R;
import com.csy.fight.main.IMainContract;

import butterknife.ButterKnife;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.fragment_main_2, container, false);
        initView();
        return mFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this, mFragment);
    }

    @Override
    public void setTitle() {
        if (isAdded() && mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
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

    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void refresh() {

    }
}
