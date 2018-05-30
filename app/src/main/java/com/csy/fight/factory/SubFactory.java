package com.csy.fight.factory;

/**
 * Created by chengshengyang on 2018/4/18.
 *
 * 具体工厂(Concrete Creator)角色：减法类工厂
 *
 * @author chengshengyang
 */
public class SubFactory implements IFactory {
    @Override
    public AbstractOperation createOperation() {
        return new OperationSub();
    }
}
