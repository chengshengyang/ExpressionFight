package com.csy.fight.main.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.csy.fight.R;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.main.MainActivity;
import com.csy.fight.main.adapter.PhotoAdapter;
import com.csy.fight.preview.PreviewActivity;
import com.csy.fight.preview.PreviewFragment;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 展示某一个相册中图片表格的Fragment
 *
 * @author chengsy
 */
public class PhotoFragment extends BaseFragment implements PhotoAdapter.OnGridClickListener {

    @BindView(R.id.rv_album_list)
    RecyclerView mRecyclerView;

    private AlbumInfo mAlbumInfo;
    private PhotoAdapter mAdapter;

    private MainActivity mActivity;
    private Bundle       mReenterState;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
        mActionBar = mActivity.getSupportActionBar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.photo_fragment, container, false);
        initView();
        return mFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
            mAdapter = new PhotoAdapter(mActivity, mAlbumInfo);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setListener(this);
        }
        initEvent();
    }

    public void onActivityReenter(Intent data) {
        mReenterState = new Bundle(data.getExtras());
        int startingPosition = mReenterState.getInt(PreviewFragment.ARG_START_POSITION);
        int currentPosition = mReenterState.getInt(PreviewFragment.ARG_CURRENT_POSITION);
        if (startingPosition != currentPosition) {
            mRecyclerView.scrollToPosition(currentPosition);
        }
        postponeEnterTransition();
        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRecyclerView.requestLayout();
                startPostponedEnterTransition();
                return true;
            }
        });
    }

    private final SharedElementCallback mCallback = new SharedElementCallback() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mReenterState != null) {
                //从别的界面返回当前界面
                int startingPosition = mReenterState.getInt(PreviewFragment.ARG_START_POSITION);
                int currentPosition = mReenterState.getInt(PreviewFragment.ARG_CURRENT_POSITION);
                if (startingPosition != currentPosition) {
                    String newTransitionName = mAlbumInfo.getPhotoList().get(currentPosition).getImagePath();
                    View newSharedElement = mRecyclerView.findViewWithTag(newTransitionName);
                    if (newSharedElement != null) {
                        names.clear();
                        names.add(newTransitionName);
                        sharedElements.clear();
                        sharedElements.put(newTransitionName, newSharedElement);
                    }
                }
                mReenterState = null;
            } else {
                //从当前界面进入到别的界面
                View navigationBar = getActivity().findViewById(android.R.id.navigationBarBackground);
                View statusBar = getActivity().findViewById(android.R.id.statusBarBackground);
                if (navigationBar != null) {
                    names.add(navigationBar.getTransitionName());
                    sharedElements.put(navigationBar.getTransitionName(), navigationBar);
                }
                if (statusBar != null) {
                    names.add(statusBar.getTransitionName());
                    sharedElements.put(statusBar.getTransitionName(), statusBar);
                }
            }
        }
    };

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void initShareElement() {
        setExitSharedElementCallback(mCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setExitSharedElementCallback(List<String> names, Map<String, View> sharedElements) {
        mCallback.onMapSharedElements(names, sharedElements);
    }

    @Override
    public void setTitle() {
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(mAlbumInfo.getAlbumName());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setTitle();
        }
    }

    public void setAlbumInfo(AlbumInfo info) {
        this.mAlbumInfo = info;
        refresh();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initView() {
        ButterKnife.bind(this, mFragment);

        initShareElement();
        setTitle();
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PhotoFragment fragment = (PhotoFragment) fm.findFragmentByTag(getTag());
                if (fragment == null) {
                    return false;
                }
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refresh() {
        if (mAdapter != null) {
            mAdapter.setPhotoList(mAlbumInfo);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onGridItemClick(View view, AlbumInfo albumInfo, int position) {
        Intent intent = new Intent(mActivity, PreviewActivity.class);
        intent.putExtra(PreviewFragment.ARG_ALBUM_INFO, albumInfo);
        intent.putExtra(PreviewFragment.ARG_START_POSITION, position);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, view, view.getTransitionName());
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onGridItemLongClick(View v) {
        Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.normal_to_large);
        v.startAnimation(animation);
    }
}
