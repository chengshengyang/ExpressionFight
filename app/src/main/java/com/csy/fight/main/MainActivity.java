package com.csy.fight.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.csy.fight.R;
import com.csy.fight.constant.TagStatic;
import com.csy.fight.data.ImageRepository;
import com.csy.fight.data.local.LocalAlbumDataSource;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.main.adapter.AlbumAdapter;
import com.csy.fight.main.fragment.AlbumFragment;
import com.csy.fight.main.fragment.DiscoverFragment;
import com.csy.fight.main.fragment.ExpressionFragment;
import com.csy.fight.main.fragment.MeFragment;
import com.csy.fight.main.fragment.MessageFragment;
import com.csy.fight.main.fragment.PhotoFragment;
import com.csy.fight.widget.TabItem;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author chengshengyang
 */
public class MainActivity extends AppCompatActivity
        implements AlbumAdapter.OnAlbumItemClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tab_item_main_0)
    TabItem tabItemMain0;

    @BindView(R.id.tab_item_main_1)
    TabItem tabItemMain1;

    @BindView(R.id.tab_item_main_2)
    TabItem tabItemMain2;

    @BindView(R.id.tab_item_main_3)
    TabItem tabItemMain3;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentManager mFragmentManager;
    private ExpressionFragment mExpressionFragment;
    private DiscoverFragment mDiscoverFragment;
    private MessageFragment mMessageFragment;
    private MeFragment mMeFragment;
    private AlbumFragment mAlbumFragment;
    private PhotoFragment mPhotoFragment;

    /**
     * Activity持有presenter对象实例，操作model，同时持有view实例，
     * 并调用presenter的setView方法，将view传递到presenter中。
     */
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSharedElementCallback();
        initViews();
        //new JavaTest();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onActivityReenter(int requestCode, Intent data) {
        super.onActivityReenter(requestCode, data);
        if (mPhotoFragment != null && mPhotoFragment.isVisible()) {
            mPhotoFragment.onActivityReenter(data);
        }
    }

    private void initViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFragmentManager = getSupportFragmentManager();

        // 设置返回按钮监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoFragment != null && mPhotoFragment.isVisible()) {
                    showFragment(TagStatic.TAG_FRAGMENT_ALBUM);
                } else if (mAlbumFragment != null && mAlbumFragment.isVisible()) {
                    finish();
                }
            }
        });

        mPresenter = new MainPresenter(getApplicationContext(),
                ImageRepository.getInstance(LocalAlbumDataSource.getInstance(getApplicationContext())));

        tabItemMain0.performClick();
    }

    @Override
    public void onBackPressed() {
        if (mPhotoFragment != null && mPhotoFragment.isVisible()) {
            showFragment(TagStatic.TAG_FRAGMENT_ALBUM);
        } else if (mAlbumFragment != null && mAlbumFragment.isVisible()) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tab_item_main_0, R.id.tab_item_main_1, R.id.tab_item_main_2,
            R.id.tab_item_main_3})
    public void onClick(View view) {
        clearChecked();
        switch (view.getId()) {
            case R.id.tab_item_main_0:
                tabItemMain0.setChecked(true);
                showFragment(TagStatic.TAG_FRAGMENT_HOME_PAGE);
                break;

            case R.id.tab_item_main_1:
                tabItemMain1.setChecked(true);
                showFragment(TagStatic.TAG_FRAGMENT_DISCOVER);
                break;

            case R.id.tab_item_main_2:
                tabItemMain2.setChecked(true);
                showFragment(TagStatic.TAG_FRAGMENT_MESSAGE);
                break;

            case R.id.tab_item_main_3:
                tabItemMain3.setChecked(true);
                showFragment(TagStatic.TAG_FRAGMENT_ME);
                break;

            default:
                break;
        }
    }

    public void showFragment(int tag) {
        if (mFragmentManager != null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            hideFragments();
            switch (tag) {
                case TagStatic.TAG_FRAGMENT_ALBUM:
                    mAlbumFragment = (AlbumFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_ALBUM + "");
                    if (mAlbumFragment == null) {
                        mAlbumFragment = new AlbumFragment();
                        transaction.add(R.id.fragment_content, mAlbumFragment, tag + "");
                    } else {
                        transaction.show(mAlbumFragment);
                    }
                    mPresenter.setView(mAlbumFragment);
                    break;

                case  TagStatic.TAG_FRAGMENT_PHOTO:
                    mPhotoFragment = (PhotoFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_PHOTO + "");
                    if (mPhotoFragment == null) {
                        mPhotoFragment = new PhotoFragment();
                        transaction.add(R.id.fragment_content, mPhotoFragment, tag + "");
                    } else {
                        transaction.show(mPhotoFragment);
                    }
                    mPresenter.setView(mPhotoFragment);
                    break;

                case TagStatic.TAG_FRAGMENT_HOME_PAGE:
                    mExpressionFragment = (ExpressionFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_HOME_PAGE + "");
                    if (mExpressionFragment == null) {
                        mExpressionFragment = new ExpressionFragment();
                        transaction.add(R.id.fragment_content, mExpressionFragment, tag + "");
                    } else {
                        transaction.show(mExpressionFragment);
                    }
                    mPresenter.setView(mExpressionFragment);
                    break;

                case TagStatic.TAG_FRAGMENT_DISCOVER:
                    mDiscoverFragment = (DiscoverFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_DISCOVER + "");
                    if (mDiscoverFragment == null) {
                        mDiscoverFragment = new DiscoverFragment();
                        transaction.add(R.id.fragment_content, mDiscoverFragment, tag + "");
                    } else {
                        transaction.show(mDiscoverFragment);
                    }
                    mPresenter.setView(mDiscoverFragment);
                    break;

                case TagStatic.TAG_FRAGMENT_MESSAGE:
                    mMessageFragment = (MessageFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_MESSAGE + "");
                    if (mMessageFragment == null) {
                        mMessageFragment = new MessageFragment();
                        transaction.add(R.id.fragment_content, mMessageFragment, tag + "");
                    } else {
                        transaction.show(mMessageFragment);
                    }
                    mPresenter.setView(mMessageFragment);
                    break;

                case TagStatic.TAG_FRAGMENT_ME:
                    mMeFragment = (MeFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_ME + "");
                    if (mMeFragment == null) {
                        mMeFragment = new MeFragment();
                        transaction.add(R.id.fragment_content, mMeFragment, tag + "");
                    } else {
                        transaction.show(mMeFragment);
                    }
                    mPresenter.setView(mMeFragment);
                    break;

                default:
                    break;
            }

            transaction.commitAllowingStateLoss();
        }
    }

    private void hideFragments() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mExpressionFragment = (ExpressionFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_HOME_PAGE + "");
        if (mExpressionFragment != null) {
            transaction.hide(mExpressionFragment);
        }

        mDiscoverFragment = (DiscoverFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_DISCOVER + "");
        if (mDiscoverFragment != null) {
            transaction.hide(mDiscoverFragment);
        }

        mMessageFragment = (MessageFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_MESSAGE + "");
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }

        mMeFragment = (MeFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_ME + "");
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }

        mAlbumFragment = (AlbumFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_ALBUM + "");
        if (mAlbumFragment != null) {
            transaction.hide(mAlbumFragment);
        }

        mPhotoFragment = (PhotoFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_PHOTO + "");
        if (mPhotoFragment != null) {
            transaction.hide(mPhotoFragment);
        }

        transaction.commitAllowingStateLoss();
    }

    private void clearChecked() {
        tabItemMain0.setChecked(false);
        tabItemMain1.setChecked(false);
        tabItemMain2.setChecked(false);
        tabItemMain3.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AlbumAdapter.ViewHolder holder, AlbumInfo info, int position) {
        if (mFragmentManager != null) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            hideFragments();

            mPhotoFragment = (PhotoFragment) mFragmentManager.findFragmentByTag(TagStatic.TAG_FRAGMENT_PHOTO + "");
            if (mPhotoFragment == null) {
                mPhotoFragment = new PhotoFragment();
                mPhotoFragment.setAlbumInfo(info);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mPhotoFragment.setSharedElementEnterTransition(new PhotoTransition());
//                    mPhotoFragment.setExitTransition(new Fade());
//                    mPhotoFragment.setEnterTransition(new Fade());
//                    mPhotoFragment.setSharedElementReturnTransition(new PhotoTransition());
//                }

                transaction.addSharedElement(holder.getIv_album(), info.getPhotoList().get(0).getImagePath());
                transaction.add(R.id.fragment_content, mPhotoFragment, TagStatic.TAG_FRAGMENT_PHOTO + "");
                transaction.addToBackStack(null);
            } else {
                mPhotoFragment.setAlbumInfo(info);
                transaction.show(mPhotoFragment);
            }

            transaction.commitAllowingStateLoss();
        }
    }

    private void initSharedElementCallback() {
        setExitSharedElementCallback(sharedElementCallback);
    }

    private final SharedElementCallback sharedElementCallback = new SharedElementCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mPhotoFragment != null) {
                mPhotoFragment.setExitSharedElementCallback(names, sharedElements);
            }
        }
    };
}
