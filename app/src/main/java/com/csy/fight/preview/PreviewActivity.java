package com.csy.fight.preview;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.csy.fight.R;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.view.HackyViewPager;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author csy
 */
public class PreviewActivity extends AppCompatActivity {

    @BindView(R.id.preview_viewpager)
    HackyViewPager mViewPager;

    @BindView(R.id.iv_Share)
    ImageView mIvShare;

    private int             mCurrentPosition;
    private int             mStartPosition;
    private boolean         mIsReturning;
    private AlbumInfo       mAlbumInfo;
    private PreviewFragment mPreviewFragment;
    private PreviewPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        getExtras();
        initShareElement();
        initView();
    }

    private void getExtras() {
        if (getIntent().hasExtra(PreviewFragment.ARG_START_POSITION)) {
            mStartPosition = getIntent().getIntExtra(PreviewFragment.ARG_START_POSITION, 0);
        }

        if (getIntent().hasExtra(PreviewFragment.ARG_ALBUM_INFO)) {
            mAlbumInfo = (AlbumInfo) getIntent().getSerializableExtra(PreviewFragment.ARG_ALBUM_INFO);
        }

        if (getIntent().hasExtra(PreviewFragment.ARG_CURRENT_POSITION)) {
            mCurrentPosition = getIntent().getIntExtra(PreviewFragment.ARG_CURRENT_POSITION, 0);
        }
    }

    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                PhotoView sharedElement = mPreviewFragment.getPhotoView();
                if (sharedElement == null) {
                    names.clear();
                    sharedElements.clear();
                } else if (mStartPosition == mCurrentPosition) {
                    names.clear();
                    names.add(sharedElement.getTransitionName());
                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };

    @Override
    public void finishAfterTransition() {
        mIsReturning = true;
        Intent data = new Intent();
        data.putExtra(PreviewFragment.ARG_START_POSITION, mStartPosition);
        data.putExtra(PreviewFragment.ARG_CURRENT_POSITION, mCurrentPosition);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initShareElement() {
        postponeEnterTransition();
        setEnterSharedElementCallback(mCallback);
    }

    private void initView() {
        mCurrentPosition = mStartPosition;

        mViewPager.setAdapter(new PreviewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }
        });

        mPresenter = new PreviewPresenter();
    }

    private class PreviewPagerAdapter extends FragmentStatePagerAdapter {

        PreviewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            mPreviewFragment = PreviewFragment.newInstance(position, mStartPosition, mAlbumInfo);
            mPresenter.setPreviewView(mPreviewFragment);
            return mPreviewFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPreviewFragment = (PreviewFragment) object;
        }

        @Override
        public int getCount() {
            return mAlbumInfo.getPhotoList().size();
        }
    }
}
