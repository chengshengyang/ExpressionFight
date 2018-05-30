package com.csy.fight.factory;

/**
 * Created by chengshengyang on 2018/4/18.
 *
 * 具体工厂(Concrete Creator)角色：加法类工厂
 *
 * @author chengshengyang
 */
public class AddFactory implements IFactory {

    @Override
    public AbstractOperation createOperation() {
        return new OperationAdd();
    }
}
