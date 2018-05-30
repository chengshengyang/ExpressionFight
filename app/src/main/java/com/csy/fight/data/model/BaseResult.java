package com.csy.fight.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chengshengyang on 2018/4/28.
 *
 * @author chengshengyang
 */
public class BaseResult<T> implements Serializable {

    @SerializedName("ret_code")
    private CodeBean retCode;

    @SerializedName("ret_value")
    private T retValue;

    public CodeBean getRetCode() {
        return retCode;
    }
    public void setRetCode(CodeBean retCode) {
        this.retCode = retCode;
    }

    public T getRetValue() {
        return retValue;
    }

    public void setRetValue(T retValue) {
        this.retValue = retValue;
    }

    public static class CodeBean implements Serializable {
        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
