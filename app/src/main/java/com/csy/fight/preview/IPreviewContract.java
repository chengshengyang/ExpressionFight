package com.csy.fight.preview;

import com.csy.fight.IBasePresenter;
import com.csy.fight.IBaseView;

/**
 * Created by csy on 2018/5/18 10:47
 */
public interface IPreviewContract {

    /**
     * view可以通过presenter操作model中的数据接口
     */
    interface IPresenter extends IBasePresenter {
        /**
         * 获取数据
         * @return
         */
        Object getData();
    }

    /**
     * 页面可以实现的功能在这里定义
     */
    interface IView extends IBaseView<IPreviewContract.IPresenter> {
        /**
         * 设置页面标题文字
         */
        void getExtras();

        /**
         * 初始化布局 控件
         */
        void initView();

        /**
         * 初始化各种事件
         */
        void initEvent();
    }
}
