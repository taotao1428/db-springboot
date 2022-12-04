package com.hewutao.db.model;

public interface Delay<T> {
    boolean loaded();
    T getOriginal();
}
