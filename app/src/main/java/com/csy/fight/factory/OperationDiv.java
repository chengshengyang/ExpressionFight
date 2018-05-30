package com.csy.fight.factory;

/**
 * Created by chengshengyang on 2018/4/18.
 *
 * 具体产品(Concrete Product)角色：除法类
 *
 * @author chengshengyang
 */
public class OperationDiv extends AbstractOperation {
    @Override
    public double getResult() {
        double result = 0;
        if (super.getNumberB() == 0) {
            throw new ArithmeticException("除数不能为0.");
        }
        result = super.getNumberA() / super.getNumberB();
        return result;
    }
}
