package com.csy.fight.preview;

import android.annotation.TargetApi;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
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
public class PreviewFragment extends BaseFragment {

    private static final String TAG = PreviewFragment.class.getSimpleName();

    public static final String ARG_CURRENT_POSITION = "current_position";
    public static final String ARG_START_POSITION   = "start_position";
    public static final String ARG_ALBUM_INFO   = "album_info";

    @BindView(R.id.iv_preview) PhotoView mPhotoView;

    private Context     mContext;
    private int         mCurrentPosition;
    private int         mStartPosition;
    private AlbumInfo   mAlbumInfo;

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

        Bundle bundle = getArguments();
        mStartPosition = bundle.getInt(ARG_START_POSITION);
        mCurrentPosition = bundle.getInt(ARG_CURRENT_POSITION);
        mAlbumInfo = (AlbumInfo) bundle.getSerializable(ARG_ALBUM_INFO);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragment = inflater.inflate(R.layout.fragment_preview, container, false);
        ButterKnife.bind(this, mFragment);

        String imagePath = mAlbumInfo.getPhotoList().get(mCurrentPosition).getImagePath();
        mPhotoView.setTransitionName(imagePath);
        Glide.with(mContext)
                .load(imagePath)
                .fitCenter()
                .dontAnimate()
                .skipMemoryCache(false)
                .listener(mRequestListener)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_invoice_loading)
                .error(R.drawable.ic_invoice_loading_error)
                .into(mPhotoView);

        if (mStartPosition == mCurrentPosition) {
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
        return mFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onPreDraw() {
                    mPhotoView.getViewTreeObserver().removeOnPreDrawListener(this);
                    getActivity().startPostponedEnterTransition();
                    return true;
                }
            });
        }
    }

    RequestListener mRequestListener = new RequestListener() {

        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            Log.d(TAG, "onException: " + e.toString() + "  \nmodel:" + model
                    + " isFirstResource: " + isFirstResource);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target,
                                       boolean isFromMemoryCache, boolean isFirstResource) {
            startPostponedEnterTransition();

            Log.e(TAG, "Ready: isFirstResource: " + isFirstResource
                    + ", isFromMemoryCache: " + isFromMemoryCache + ", ||| \nmodel:" + model);
            return false;
        }
    };

    @Override
    public void showProgress(boolean show) {

    }
}
