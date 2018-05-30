package com.csy.fight.main.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.csy.fight.main.IMainContract;

/**
 * Created by chengshengyang on 2018/1/29.
 *
 * @author chengshengyang
 */

public abstract class BaseFragment extends Fragment implements IMainContract.IView {

    /**
     * view必须持有presenter对象实例
     */
    protected IMainContract.IPresenter mPresenter;

    protected ActionBar mActionBar;
    protected View mFragment;

    @Override
    public void setPresenter(IMainContract.IPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void setTitle() {

    }

    @Override
    public void refresh() {

    }
}
