package com.csy.fight.data.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.csy.fight.data.IImageDataSource;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.entity.PhotoInfo;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by csy on 2018/6/7 10:21
 * @author csy
 */
public class LocalAlbumDataSource implements IImageDataSource {

    private volatile static LocalAlbumDataSource INSTANCE = null;

    private WeakReference<Context> mContextRef;
    private Map<String, String> mThumbnailList = new HashMap<>();

    private LocalAlbumDataSource(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    public static LocalAlbumDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalAlbumDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalAlbumDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getImageDataSource(OnAsyncAlbumFinishListener listener) {
        AlbumAsync task = new AlbumAsync();
        task.setOnAsyncFinishListener(listener);
        task.execute((Void) null);
    }

    /**
     * 读取媒体资源中缩略图资源，以HashMap的方式保存在mThumbnailList中。
     */
    private void getThumbnail() {
        if (mContextRef.get() == null) {
            return;
        }

        ContentResolver cr = mContextRef.get().getContentResolver();
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

        OnAsyncAlbumFinishListener listener;

        void setOnAsyncFinishListener(OnAsyncAlbumFinishListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreExecute();
        }

        @Override
        protected List<AlbumInfo> doInBackground(Void... params) {
            getThumbnail();

            if (mContextRef.get() == null) {
                return null;
            }

            ContentResolver cr = mContextRef.get().getContentResolver();
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
        }

        @Override
        protected void onPostExecute(List<AlbumInfo> result) {
            super.onPostExecute(result);
            if (listener != null) {
                listener.onLoadSuccess(result);
            }
        }

        @Override
        protected void onCancelled() {
            if (listener != null) {
                listener.onLoadFailed();
            }
        }
    }
}
