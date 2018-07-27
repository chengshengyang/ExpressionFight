package com.csy.fight.data;

import com.csy.fight.entity.AlbumInfo;

import java.util.List;

/**
 * Created by csy on 2018/6/7 10:11
 * @author csy
 */
public interface IImageDataSource {

    interface OnAsyncAlbumFinishListener {

        /**
         * 执行前的准备操作
         */
        void onPreExecute();

        /**
         * 加载成功
         * @param list
         */
        void onLoadSuccess(List<AlbumInfo> list);

        /**
         * 加载失败
         */
        void onLoadFailed();
    }

    /**
     * 获取图片数据源接口
     *
     * @param listener
     * @return
     */
    void getImageDataSource(OnAsyncAlbumFinishListener listener);
}
