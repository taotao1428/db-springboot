package com.hewutao.db.model3;

import java.util.List;
import java.util.function.Supplier;

public interface Field<T> {
    T getValue();
    void update(T value);
    boolean isUpdated();


     static <T> Field<T> of (T value) {
         return new SimpleField<>(value);
     }

     static <T extends Entity> ListField<T> ofEntities(List<T> entities) {
         return new SimpleEntityListField<>(entities);
     }

     static <T extends Entity> ListField<T> ofDelayEntities(Supplier<List<T>> supplier) {
         return new DelayEntityListField<>(supplier);
     }
}
