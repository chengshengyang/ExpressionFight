package com.csy.fight.factory;

/**
 * Created by chengshengyang on 2018/4/18.
 *
 * 抽象产品(Product)角色：运算类
 *
 * @author chengshengyang
 */
public abstract class AbstractOperation {
    private double numberA = 0;
    private double numberB = 0;

    public void setNumberA(double numberA) {
        this.numberA = numberA;
    }
    public void setNumberB(double numberB) {
        this.numberB = numberB;
    }
    public double getNumberA() {
        return numberA;
    }
    public double getNumberB() {
        return numberB;
    }

    /**
     * 计算两个数的运算结果
     * @return
     */
    public abstract double getResult();
}
