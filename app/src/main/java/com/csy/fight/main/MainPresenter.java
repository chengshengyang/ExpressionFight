package com.csy.fight.main;

import android.content.Context;

import com.csy.fight.data.IImageDataSource;
import com.csy.fight.data.ImageRepository;
import com.csy.fight.data.RootEntity;

/**
 * Created by chengshengyang on 2018/1/29.
 *
 * @author chengshengyang
 */

public class MainPresenter implements IMainContract.IPresenter {

    private Context mContext;
    private IMainContract.IView mMainView;
    private ImageRepository mImageRepository;

    MainPresenter(Context context, ImageRepository repository) {
        this.mContext = context;
        this.mImageRepository = repository;
    }

    public void setView(IMainContract.IView view) {
        this.mMainView = view;
        mMainView.setPresenter(this);
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

    @Override
    public void getLocalDataSource(IImageDataSource.OnAsyncAlbumFinishListener listener) {
        mImageRepository.getImageDataSource(listener);
    }
}
