package com.csy.fight.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chengshengyang on 2018/4/28.
 *
 * @author chengshengyang
 */
public class StringResult {

    @SerializedName(value = "stringValue", alternate = {"userName"})
    private String stringValue;

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
