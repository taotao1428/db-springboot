package com.hewutao.db.model;

import java.util.ArrayList;
import java.util.List;

public class SimpleEntityListField<T extends Entity> extends AbstractEntityListField<T> {
    private List<T> entities;

    public SimpleEntityListField(List<T> entities) {
        this.entities = new ArrayList<>(entities);
    }

    @Override
    protected void doInit() {
        setEntities(entities);
        entities = null;
    }
}
