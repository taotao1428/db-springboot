package com.hewutao.db.model;

import java.util.List;
import java.util.function.Supplier;

public class DelayEntityListField<T extends Entity> extends AbstractEntityListField<T> {
    private Supplier<List<T>> entitiesSupplier;

    public DelayEntityListField(Supplier<List<T>> entitiesSupplier) {
        this.entitiesSupplier = entitiesSupplier;
    }

    @Override
    protected void doInit() {
        setEntities(entitiesSupplier.get());
        entitiesSupplier = null;
    }
}
