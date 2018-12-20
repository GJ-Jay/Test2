package com.gj.buycarej.core;

public interface Callback {
    void onSuccess(String tag,Object o);
    void onFailure(String tag,Exception e);
}