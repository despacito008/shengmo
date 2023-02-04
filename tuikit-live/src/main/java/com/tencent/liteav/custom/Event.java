package com.tencent.liteav.custom;

class Event<T> {
    private T mData;
    private int Code;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public void setData(T data) {
        mData = data;
    }

    public T getData() {
        return mData;
    }
}
