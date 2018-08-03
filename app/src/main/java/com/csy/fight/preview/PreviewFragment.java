package com.csy.fight.preview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.csy.fight.R;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.main.fragment.BaseFragment;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @author csy
 */
public class PreviewFragment extends BaseFragment implements IPreviewContract.IView {

    private static final String TAG = PreviewFragment.class.getSimpleName();

    public static final String ARG_CURRENT_POSITION = "current_position";
    public static final String ARG_START_POSITION   = "start_position";
    public static final String ARG_ALBUM_INFO   = "album_info";

    @BindView(R.id.iv_preview) PhotoView mPhotoView;

    private Context     mContext;
    private int         mCurrentPosition;
    private int         mStartPosition;
    private AlbumInfo   mAlbumInfo;
    private IPreviewContract.IPresenter mPresenter;

    public PreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreviewFragment.
     */
    public static PreviewFragment newInstance(int currentPosition, int startPosition, AlbumInfo info) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_CURRENT_POSITION, currentPosition);
        bundle.putInt(ARG_START_POSITION, startPosition);
        bundle.putSerializable(ARG_ALBUM_INFO, info);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragment = inflater.inflate(R.layout.fragment_preview, container, false);
        initView();
        initEvent();
        return mFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // mvp数据接口调用测试
        mPresenter.getData();
    }

    /**
     * Returns true if {@param view} is contained within {@param container}'s bounds.
     */
    private static boolean isViewInBounds(@NonNull View container, @NonNull View view) {
        Rect containerBounds = new Rect();
        container.getHitRect(containerBounds);
        return view.getLocalVisibleRect(containerBounds);
    }

    /**
     * Returns the shared element that should be transitioned back to the previous Activity,
     * or null if the view is not visible on the screen.
     */
    public PhotoView getPhotoView() {
        if (isViewInBounds(getActivity().getWindow().getDecorView(), mPhotoView)) {
            return mPhotoView;
        }
        return null;
    }

    @Override
    public void startPostponedEnterTransition() {
        if (mCurrentPosition == mStartPosition) {
            mPhotoView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mPhotoView.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().startPostponedEnterTransition();
                    }
                    return true;
                }
            });
        }
    }

    RequestListener mRequestListener = new RequestListener() {

        @Override
        public boolean onLoadFailed(GlideException e, Object model, Target target, boolean isFirstResource) {
            Log.d(TAG, "onException: " + e.toString() + "  \nmodel:" + model
                    + " isFirstResource: " + isFirstResource);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target,
                                       DataSource dataSource, boolean isFirstResource) {
            startPostponedEnterTransition();

            Log.e(TAG, "Ready: isFirstResource: " + isFirstResource
                    + ", isFromMemoryCache: " + dataSource.name() + ", ||| \nmodel:" + model);
            return false;
        }
    };

    @Override
    public void getExtras() {
        Bundle bundle = getArguments();
        mStartPosition = bundle.getInt(ARG_START_POSITION);
        mCurrentPosition = bundle.getInt(ARG_CURRENT_POSITION);
        mAlbumInfo = (AlbumInfo) bundle.getSerializable(ARG_ALBUM_INFO);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this, mFragment);
    }

    @Override
    public void initEvent() {
        String imagePath = mAlbumInfo.getPhotoList().get(mCurrentPosition).getImagePath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPhotoView.setTransitionName(imagePath);
        }

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_invoice_loading)
                .error(R.drawable.ic_invoice_loading_error);

        Glide.with(mContext)
                .load(imagePath)
                .listener(mRequestListener)
                .apply(options)
                .into(mPhotoView);

        if (mStartPosition == mCurrentPosition) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {

                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
    //                    Animator animator = ViewAnimationUtils.createCircularReveal(
    //                            , , , , )
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });
            }
        }
    }

    @Override
    public void setPresenter(IPreviewContract.IPresenter presenter) {
        this.mPresenter = presenter;
    }
}
