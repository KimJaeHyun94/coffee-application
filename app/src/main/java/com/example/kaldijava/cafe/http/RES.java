package com.example.kaldijava.cafe.http;

import com.google.gson.annotations.SerializedName;

public class RES<T> {
    @SerializedName("code")
    int code;
    @SerializedName("result")
    T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
