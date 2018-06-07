package com.csy.fight.data;

import android.support.annotation.NonNull;

/**
 * Created by csy on 2018/6/7 10:25
 * @author csy
 */
public class ImageRepository implements IImageDataSource {

    private volatile static ImageRepository INSTANCE = null;

    private final IImageDataSource mLocalDataSource;

    //private final IImageDataSource mRemoteDataSource;

    public ImageRepository(IImageDataSource localSource) {
        this.mLocalDataSource = localSource;
    }

    public static ImageRepository getInstance(@NonNull IImageDataSource localData) {
        if (INSTANCE == null) {
            synchronized (ImageRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ImageRepository(localData);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getImageDataSource(OnAsyncAlbumFinishListener listener) {
        if (mLocalDataSource != null) {
            mLocalDataSource.getImageDataSource(listener);
        }
    }
}
