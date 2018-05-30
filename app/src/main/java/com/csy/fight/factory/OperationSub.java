package com.csy.fight.factory;

/**
 * Created by chengshengyang on 2018/4/18.
 *
 * 具体产品(Concrete Product)角色：减法类
 *
 * @author chengshengyang
 */
public class OperationSub extends AbstractOperation {

    @Override
    public double getResult() {
        double result = 0;
        result = super.getNumberA() - super.getNumberB();
        return result;
    }
}
