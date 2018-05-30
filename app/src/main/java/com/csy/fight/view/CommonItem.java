package com.csy.fight.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csy.fight.R;

/**
 * Created by chengshengyang on 2018/2/8.
 *
 * @author chengshengyang
 */

public class CommonItem extends RelativeLayout {

    private Context mContext;

    private View mView;
    private ImageView mIvIcon;
    private TextView mTvTitle;

    public CommonItem(Context context) {
        this(context, null);
    }

    public CommonItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initViews();
        initXmlParams(attrs);
    }

    private void initViews() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.view_common_item, this, true);
        mIvIcon = mView.findViewById(R.id.imageView);
        mTvTitle = mView.findViewById(R.id.textView);
    }

    private void initXmlParams(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.SegmentItem);

        setTitle(array.getString(R.styleable.SegmentItem_android_text));

        Drawable drawable = array.getDrawable(R.styleable.SegmentItem_android_src);
        if (drawable != null) {
            setIcon(drawable);
        }
    }

    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setIcon(Drawable drawable) {
        mIvIcon.setImageDrawable(drawable);
    }

    public void setIcon(int resId) {
        mIvIcon.setImageResource(resId);
    }
}
