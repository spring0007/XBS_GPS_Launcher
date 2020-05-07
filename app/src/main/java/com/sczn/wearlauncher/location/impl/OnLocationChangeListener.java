package com.sczn.wearlauncher.location.impl;

/**
 * Description:
 * Created by Bingo on 2019/1/19.
 */
public interface OnLocationChangeListener<T> {
    void success(T t);

    void fail();
}
