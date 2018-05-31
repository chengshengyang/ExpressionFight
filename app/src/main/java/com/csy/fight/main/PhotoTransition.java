package com.csy.fight.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionSet;
import android.util.AttributeSet;

/**
 * 转换动画
 *
 * Created by csy on 2018/5/31 11:14
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PhotoTransition extends TransitionSet {

    public PhotoTransition() {
        init();
    }

    // 允许资源文件使用
    public PhotoTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform());
    }
}
