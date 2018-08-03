package com.csy.fight.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.csy.fight.R;
import com.csy.fight.entity.AlbumInfo;
import com.csy.fight.entity.PhotoInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 相册列表适配器
 *
 * @author chengsy
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context mContext;
    private String mPhotoCountFormat;
    private List<AlbumInfo> mAlbumList;
    private OnAlbumItemClickListener mListener;

    public AlbumAdapter(Context context) {
        super();
        this.mContext = context;
        mPhotoCountFormat = mContext.getString(R.string.album_count);
    }

    public void setAlbumList(List<AlbumInfo> list) {
        this.mAlbumList = list;
        notifyDataSetChanged();
    }

    public void setAlbumItemClickListener(OnAlbumItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnAlbumItemClickListener {
        /**
         * item点击事件
         *
         * @param holder
         * @param info
         * @param position
         */
        void onItemClick(ViewHolder holder, AlbumInfo info, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_list, parent, false);
        return new ViewHolder(view);
    }

    RequestOptions options = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_invoice_loading)
            .error(R.drawable.ic_invoice_loading_error)
            .override(65, 65);

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AlbumInfo aInfo = mAlbumList.get(position);
        PhotoInfo pInfo = aInfo.getPhotoList().get(0);

        // 把每个图片视图设置不同的Transition名称, 防止在一个视图内有多个相同的名称, 在变换的时候造成混乱
        // Fragment支持多个View进行变换, 使用适配器时, 需要加以区分
        ViewCompat.setTransitionName(holder.iv_album, pInfo.getImagePath());

        Glide.with(mContext)
                .load(pInfo.getImagePath())
                .apply(options)
                .into(holder.iv_album);

        holder.tv_name.setText(aInfo.getAlbumName());

        String sSize = String.format(mPhotoCountFormat, aInfo.getPhotoList().size());
        holder.tv_count.setText(sSize);

        holder.ll_contain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder, aInfo, position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (mAlbumList == null ? 0 : mAlbumList.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_contain) LinearLayout ll_contain;
        @BindView(R.id.album_iv) ImageView iv_album;
        @BindView(R.id.album_name_tv) TextView tv_name;
        @BindView(R.id.album_count_tv) TextView tv_count;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getIv_album() {
            return iv_album;
        }
    }
}
