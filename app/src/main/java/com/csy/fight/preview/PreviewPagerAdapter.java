package com.csy.fight.preview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.csy.fight.R;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.entity.PhotoInfo;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * Created by csy on 2018/5/18 13:56
 * @author csy
 */
public class PreviewPagerAdapter extends PagerAdapter {

    private static final String TAG = "PreviewPagerAdapter";
    private Context mContext;
    private List<PhotoInfo> mPhotoList;
    PhotoView photoView;

    PreviewPagerAdapter(Context context, AlbumInfo info) {
        this.mContext = context;
        this.mPhotoList = info.getPhotoList();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener(final String filePath) {
        Transition transition = ((Activity) mContext).getWindow().getSharedElementEnterTransition();
        if (null != transition) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    transition.removeListener(this);
                    loadFullSizeImage(filePath);
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return mPhotoList == null ? 0 : mPhotoList.size();
    }

    RequestListener mRequestListener = new RequestListener() {

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            Log.d(TAG, "onException: " + e.toString() + "  \nmodel:" + model
                    + " isFirstResource: " + isFirstResource);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
            Log.e(TAG, "Ready: isFirstResource: " + isFirstResource
                    + ", isFromMemoryCache: " + dataSource.name() + ", ||| \nmodel:" + model);
            return false;
        }
    };

    RequestOptions options = new RequestOptions()
            .fitCenter()
            .dontAnimate()
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_invoice_loading)
            .error(R.drawable.ic_invoice_loading_error);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_preview, null);
        photoView = view.findViewById(R.id.iv_preview);

        PhotoInfo pInfo = mPhotoList.get(position);
        String filePath = pInfo.getImagePath();

        if (addTransitionListener(filePath)) {
            loadThumbnailImage(filePath);
            ViewCompat.setTransitionName(photoView, "xxx");
        } else {
            loadFullSizeImage(filePath);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    private void loadFullSizeImage(String filePath) {
        Glide.with(mContext)
                .load(filePath)
                .listener(mRequestListener)
                .apply(options)
                .into(photoView);
    }

    private void loadThumbnailImage(String filePath) {
        Glide.with(mContext)
                .load(filePath)
                .listener(mRequestListener)
                .apply(options)
                .into(photoView);
    }
}
