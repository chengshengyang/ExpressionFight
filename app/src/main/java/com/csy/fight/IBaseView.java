package com.csy.fight;

/**
 * Created by chengshengyang on 2018/1/29.
 *
 * @author chengshengyang
 */

public interface IBaseView<T> {
    /**
     * 给view设置presenter
     * @param presenter
     */
    void setPresenter(T presenter);
}
