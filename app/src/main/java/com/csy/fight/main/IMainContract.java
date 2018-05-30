package com.csy.fight.main;

import com.csy.fight.IBasePresenter;
import com.csy.fight.IBaseView;
import com.csy.fight.data.RootEntity;

/**
 * Created by chengshengyang on 2018/1/29.
 *
 * @author chengshengyang
 */

public interface IMainContract {

    /**
     * view可以通过presenter操作model中的数据接口
     */
    interface IPresenter extends IBasePresenter {
        /**
         * 获取热门
         * @return
         */
        RootEntity getHot();

        /**
         * 获取最近使用
         * @return
         */
        RootEntity getLatest();

        /**
         * 获取精选
         * @return
         */
        RootEntity getBest();

        /**
         * 获取最新
         * @return
         */
        RootEntity getNewest();
    }

    /**
     * 页面可以实现的功能在这里定义
     */
    interface IView extends IBaseView<IPresenter> {
        /**
         * 设置页面标题文字
         */
        void setTitle();

        /**
         * 刷新页面内容
         */
        void refresh();

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
