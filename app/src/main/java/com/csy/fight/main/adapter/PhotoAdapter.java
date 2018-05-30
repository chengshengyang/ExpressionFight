package com.csy.fight.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.csy.fight.R;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.entity.PhotoInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 图片列表适配器
 *
 * @author chengsy
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private static final String TAG = PhotoAdapter.class.getSimpleName();
    private Context mContext;
    private AlbumInfo mAlbumInfo;
    private List<PhotoInfo> mPhotoList;
    private int imageSize = 0;
    private OnGridClickListener mListener;

    public void setListener(OnGridClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnGridClickListener {
        /**
         * item点击事件
         *
         * @param view
         * @param albumInfo
         * @param position
         */
        void onGridItemClick(View view, AlbumInfo albumInfo, int position);

        /**
         * item 长按事件
         *
         * @param v
         */
        void onGridItemLongClick(View v);
    }

    public PhotoAdapter(Context context, AlbumInfo mInfo) {
        super();
        this.mContext = context;
        this.mAlbumInfo = mInfo;
        this.mPhotoList = mInfo.getPhotoList();
        this.imageSize = getImageSize();
    }

    public void setPhotoList(AlbumInfo albumInfo) {
        this.mPhotoList = albumInfo.getPhotoList();
        notifyDataSetChanged();
    }

    private int getImageSize() {
        DisplayMetrics dMetrics = mContext.getResources().getDisplayMetrics();
        return dMetrics.widthPixels / 3;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_grid, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        LayoutParams layoutParams = (LayoutParams) holder.ivPhoto.getLayoutParams();
        layoutParams.width = imageSize;
        layoutParams.height = imageSize;
        holder.ivPhoto.setLayoutParams(layoutParams);
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGridItemClick(holder.ivPhoto, mAlbumInfo, holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final PhotoInfo photoInfo = mPhotoList.get(position);
        if (photoInfo != null) {
            String sourcePath = photoInfo.getImagePath();
            Glide.with(mContext)
                    .load(sourcePath)
                    .asBitmap()
                    .centerCrop()
                    .listener(mRequestListener)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_invoice_loading)
                    .error(R.drawable.ic_invoice_loading_error)
                    .into(holder.ivPhoto);

            if (sourcePath.endsWith(".gif")) {
                holder.tvBadge.setVisibility(View.VISIBLE);
            } else {
                holder.tvBadge.setVisibility(View.GONE);
            }

            /**
             * 设置共享元素的名称
             */
            ViewCompat.setTransitionName(holder.ivPhoto, sourcePath);
            holder.ivPhoto.setTag(R.id.recycler_item_url, sourcePath);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (mPhotoList == null ? 0 : mPhotoList.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_grid_image) ImageView ivPhoto;
        @BindView(R.id.tv_source_badge) TextView tvBadge;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
            Log.e(TAG, "Ready: isFirstResource: " + isFirstResource
                    + ", isFromMemoryCache: " + isFromMemoryCache + ", ||| \nmodel:" + model);
            return false;
        }
    };
}
