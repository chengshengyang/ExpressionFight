package com.csy.fight.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;

import com.csy.fight.data.RootEntity;
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
    private Map<String, String> mThumbnailList = new HashMap<>();

    MainPresenter(Context context) {
        this.mContext = context;
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
    public List<AlbumInfo> getLocalDataSource(AsyncResponse asyncResponse) {
        AlbumAsync task = new AlbumAsync();
        task.execute((Void) null);
        task.setOnAsyncResponse(asyncResponse);
        return null;
    }

    public interface AsyncResponse {
        void onLoadDataSuccess(List<AlbumInfo> list);

        void onLoadDataFailed();
    }

    /**
     * 读取媒体资源中缩略图资源，以HashMap的方式保存在mThumbnailList中。
     */
    public void getThumbnail() {
        ContentResolver cr = mContext.getContentResolver();
        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Thumbnails.DATA + " desc ");

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int imageId;
                String imagePath;

                int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

                imageId = cursor.getInt(imageIdColumn);
                imagePath = cursor.getString(dataColumn);

                mThumbnailList.put("" + imageId, imagePath);
            }
        }
    }

    /**
     * 异步构建相册数据
     *
     * @author chengsy
     */
    public class AlbumAsync extends AsyncTask<Void, Void, List<AlbumInfo>> {

        AsyncResponse asyncResponse;

        void setOnAsyncResponse(AsyncResponse asyncResponse) {
            this.asyncResponse = asyncResponse;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMainView.showProgress(true);
        }

        @Override
        protected List<AlbumInfo> doInBackground(Void... params) {
            getThumbnail();

            ContentResolver cr = mContext.getContentResolver();
            String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA};
            Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc ");

            List<AlbumInfo> albumInfos = new ArrayList<>();
            if (cursor != null && cursor.getCount() > 0) {
                Map<String, AlbumInfo> idMap = new HashMap<>();
                while (cursor.moveToNext()) {
                    PhotoInfo pInfo = new PhotoInfo();
                    String sId = cursor.getString(0);
                    String sBuckId = cursor.getString(1);
                    String sName = cursor.getString(2);
                    String sPath = cursor.getString(3);

                    pInfo.setImageID(sId);
                    pInfo.setThumbnailPath(mThumbnailList.get(sId));
                    pInfo.setImagePath(sPath);
                    pInfo.setImageURI("file://" + sPath);

                    File file = new File(sPath);
                    if (file.length() == 0) {
                        continue;
                    }
                    if (idMap.containsKey(sBuckId)) {
                        idMap.get(sBuckId).getPhotoList().add(pInfo);
                    } else {
                        List<PhotoInfo> mPhotoList = new ArrayList<>();
                        mPhotoList.add(pInfo);

                        AlbumInfo aInfo = new AlbumInfo();
                        aInfo.setAlbumName(sName);
                        aInfo.setPhotoList(mPhotoList);
                        albumInfos.add(aInfo);

                        idMap.put(sBuckId, aInfo);
                    }
                }
            }
            return albumInfos;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            mMainView.showProgress(true);
        }

        @Override
        protected void onPostExecute(List<AlbumInfo> result) {
            super.onPostExecute(result);
            if (asyncResponse != null) {
                asyncResponse.onLoadDataSuccess(result);
            }
            mMainView.showProgress(false);
        }

        @Override
        protected void onCancelled() {
            if (asyncResponse != null) {
                asyncResponse.onLoadDataFailed();
            }
            mMainView.showProgress(false);
        }
    }
}
