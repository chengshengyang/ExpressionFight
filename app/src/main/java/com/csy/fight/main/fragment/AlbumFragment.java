package com.csy.fight.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.csy.fight.R;
import com.csy.fight.data.IImageDataSource;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.main.IMainContract;
import com.csy.fight.main.MainActivity;
import com.csy.fight.main.adapter.AlbumAdapter;

import java.util.List;

/**
 * 展示相册列表的Fragment
 * 
 * @author chengsy
 *
 */
public class AlbumFragment extends BaseFragment implements IMainContract.IView {

	private RecyclerView mRecyclerView;
	private ProgressBar mProgressBar;
	private AlbumAdapter mAdapter;
	private MainActivity mActivity;
	private AlbumAdapter.OnAlbumItemClickListener mOnAlbumClickListener;
    /**
     * view必须持有presenter对象实例
     */
    protected IMainContract.IPresenter mPresenter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mActivity = (MainActivity) context;
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

        mPresenter.getLocalDataSource(new IImageDataSource.OnAsyncAlbumFinishListener() {

            @Override
            public void onPreExecute() {
                showProgress(true);
            }

            @Override
            public void onLoadSuccess(List<AlbumInfo> list) {
                if (getActivity() != null) {
                    mAdapter.setAlbumList(list);
                    mRecyclerView.setAdapter(mAdapter);
                }
                showProgress(false);
            }

            @Override
            public void onLoadFailed() {
                showProgress(false);
                Toast.makeText(mActivity, "数据加载失败", Toast.LENGTH_SHORT).show();
            }
        });
	}

	@Override
	public void setTitle() {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(R.string.album_title);
        }
	}

    @Override
    public void refresh() {

    }

    @Override
    public void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPresenter(IMainContract.IPresenter presenter) {
        this.mPresenter = presenter;
    }
}
