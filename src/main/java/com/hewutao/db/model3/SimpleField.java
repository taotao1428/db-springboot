package com.hewutao.db.model3;

import java.util.Objects;

public class SimpleField<T> implements Field<T> {
    private final T oldValue;
    private T value;

    public SimpleField(T value) {
        this.oldValue = value;
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void update(T value) {
        this.value = value;
    }

    @Override
    public boolean isUpdated() {
        return Objects.equals(oldValue, value);
    }
}
