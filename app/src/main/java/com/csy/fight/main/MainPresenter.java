package com.csy.fight.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;

import com.csy.fight.data.IImageDataSource;
import com.csy.fight.data.ImageRepository;
import com.csy.fight.data.RootEntity;
import com.csy.fight.data.local.LocalAlbumDataSource;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.entity.PhotoInfo;
import com.csy.fight.main.fragment.AlbumFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

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
