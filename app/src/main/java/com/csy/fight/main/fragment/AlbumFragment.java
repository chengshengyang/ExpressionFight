package com.csy.fight.main.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.csy.fight.R;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.entity.PhotoInfo;
import com.csy.fight.main.MainActivity;
import com.csy.fight.main.adapter.AlbumAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 展示相册列表的Fragment
 * 
 * @author chengsy
 *
 */
public class AlbumFragment extends BaseFragment {

	private RecyclerView mRecyclerView;
	private List<AlbumInfo> mAlbumList;
	private ProgressBar mProgressBar;
	private AlbumAdapter mAdapter;
	private Map<String, String> mThumbnailList = new HashMap<>();
	private MainActivity mActivity;
	private AlbumAdapter.OnAlbumItemClickListener mOnAlbumClickListener;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
        mActionBar = mActivity.getSupportActionBar();
		mOnAlbumClickListener = mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mFragment = inflater.inflate(R.layout.ablum_fragment, container, false);
		initView();
		setTitle();
		return mFragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvent();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			setTitle();
		}
	}

	/**
	 * 读取媒体资源中缩略图资源，以HashMap的方式保存在mThumbnailList中。
	 */
	public void getThumbnail() {
		if (isAdded()) {
			ContentResolver cr = getActivity().getContentResolver();
			String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
			Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, Thumbnails.DATA + " desc ");

			if (cursor != null && cursor.getCount() > 0) {
				mAlbumList = new ArrayList<>();
				while (cursor.moveToNext()) {
					@SuppressWarnings("unused")
					int _id;
					int image_id;
					String image_path;

					int _idColumn = cursor.getColumnIndex(Thumbnails._ID);
					int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
					int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);

					// Get the field values
					_id = cursor.getInt(_idColumn);
					image_id = cursor.getInt(image_idColumn);
					image_path = cursor.getString(dataColumn);

					mThumbnailList.put("" + image_id, image_path);
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			mActivity.finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void initView() {
		mRecyclerView = mFragment.findViewById(R.id.album_lv);
		mProgressBar = mFragment.findViewById(R.id.loading_photos_progressBar);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
	}

	@Override
	public void initEvent() {
        mAdapter = new AlbumAdapter(getActivity());
		mAdapter.setAlbumItemClickListener(mOnAlbumClickListener);


        AlbumAsync task = new AlbumAsync();
        /**
         * 创建一个没有限制的线程池(Executors.newCachedThreadPool())，并提供给AsyncTask。
         * 这样这个AsyncTask实例就有了自己的线程池而不必使用AsyncTask默认的。
         */
        task.executeOnExecutor(Executors.newCachedThreadPool(), 0);
	}

	@Override
	public void setTitle() {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(R.string.album_title);
        }
	}

    /**
     * 异步构建相册数据
     *
     * @author chengsy
     *
     */
    class AlbumAsync extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            getThumbnail();

            ContentResolver cr = getActivity().getContentResolver();
            String[] projection = { Media._ID, Media.BUCKET_ID, Media.BUCKET_DISPLAY_NAME, Media.DATA };
            Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection, null, null, Media.DATE_MODIFIED + " desc ");

            if (cursor != null && cursor.getCount() > 0) {
                mAlbumList = new ArrayList<AlbumInfo>();
                Map<String, AlbumInfo> idMap = new HashMap<String, AlbumInfo>();
                while (cursor.moveToNext()) {
                    PhotoInfo pInfo = new PhotoInfo();
                    String s_ID = cursor.getString(0);
                    String s_Buck_ID = cursor.getString(1);
                    String sName = cursor.getString(2);
                    String sPath = cursor.getString(3);

                    pInfo.setImageID(s_ID);
                    pInfo.setThumbnailPath(mThumbnailList.get(s_ID));
                    pInfo.setImagePath(sPath);
                    pInfo.setImageURI("file://" + sPath);

                    File file = new File(sPath);
                    if (file.length() == 0) {
                        continue;
                    }
                    if (idMap.containsKey(s_Buck_ID)) {
                        idMap.get(s_Buck_ID).getPhotoList().add(pInfo);
                    } else {
                        List<PhotoInfo> mPhotoList = new ArrayList<PhotoInfo>();
                        mPhotoList.add(pInfo);

                        AlbumInfo aInfo = new AlbumInfo();
                        aInfo.setAlbumName(sName);
                        aInfo.setPhotoList(mPhotoList);
                        mAlbumList.add(aInfo);

                        idMap.put(s_Buck_ID, aInfo);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            if (getActivity() != null && mAlbumList != null) {
                mAdapter.setAlbumList(mAlbumList);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }
}
