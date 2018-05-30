package com.csy.fight.factory;

/**
 * Created by chengshengyang on 2018/4/18.
 *
 * 工厂接口
 *
 * @author chengshengyang
 */
public interface IFactory {
    /**
     * 用于创建产品
     * @return
     */
    AbstractOperation createOperation();
}
