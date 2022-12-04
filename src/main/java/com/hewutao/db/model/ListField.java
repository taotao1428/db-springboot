package com.hewutao.db.model;

import java.util.List;

public interface ListField<T> extends Field<List<T>> {
    boolean add(T ele);
    boolean remove(String id);

    List<T> getAdded();
    List<T> getRemoved();
}
