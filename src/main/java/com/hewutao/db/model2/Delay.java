package com.hewutao.db.model2;

public interface Delay<T> {
    boolean loaded();
    T getOriginal();
}
