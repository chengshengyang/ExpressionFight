package com.csy.fight.main;

import android.content.Context;

import com.csy.fight.data.RootEntity;

/**
 * Created by chengshengyang on 2018/1/29.
 *
 * @author chengshengyang
 */

public class MainPresenter implements IMainContract.IPresenter {

    private Context mContext;
    private IMainContract.IView mMainView;

    public MainPresenter(Context context) {
        this.mContext = context;
    }

    public void setView(IMainContract.IView view) {
        this.mMainView = view;
        mMainView.setPresenter(this);

        mMainView.setTitle();
    }

    @Override
    public RootEntity getHot() {
        return null;
    }

    @Override
    public RootEntity getLatest() {
        return null;
    }

    @Override
    public RootEntity getBest() {
        return null;
    }

    @Override
    public RootEntity getNewest() {
        return null;
    }
}
