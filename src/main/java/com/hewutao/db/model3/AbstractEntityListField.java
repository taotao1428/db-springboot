package com.hewutao.db.model3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEntityListField<T extends Entity> implements ListField<T> {
    private List<T> oldEntities;
    private Map<String, T> entityMap;
    private boolean initialed = false;


    protected void setEntities(List<T> entities) {
        this.oldEntities = new ArrayList<>(entities);

        this.entityMap = new HashMap<>();

        for (T entity : entities) {
            if (this.entityMap.put(entity.getId(), entity) != null) {
                throw new IllegalArgumentException("more than one entity has same id");
            }
        }
    }

    protected abstract void doInit();

    private void init() {
        if (initialed) {
            doInit();
        }
    }

    @Override
    public List<T> getValue() {
        init();
        return new ArrayList<>(entityMap.values());
    }

    @Override
    public void update(List<T> values) {
        init();
        entityMap.clear();
        for (T entity : values) {
            entityMap.put(entity.getId(), entity);
        }
    }

    @Override
    public boolean isUpdated() {
        if (!initialed) {
            return false;
        }

        if (oldEntities.size() != entityMap.size()) {
            return true;
        }

        for (T oldEntity : oldEntities) {
            if (!entityMap.containsKey(oldEntity.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(T ele) {
        init();
        return entityMap.put(ele.getId(), ele) == null;
    }

    @Override
    public boolean remove(String id) {
        init();
        return entityMap.remove(id) != null;
    }

    @Override
    public List<T> getAdded() {
        init();
        Map<String, T> copiedEntityMap = new HashMap<>(entityMap);

        for (T oldEntity : oldEntities) {
            copiedEntityMap.remove(oldEntity.getId());
        }

        return new ArrayList<>(copiedEntityMap.values());
    }

    @Override
    public List<T> getRemoved() {
        init();
        List<T> removed = new ArrayList<>();

        for (T oldEntity : oldEntities) {
            if (!entityMap.containsKey(oldEntity.getId())) {
                removed.add(oldEntity);
            }
        }

        return removed;
    }
}
