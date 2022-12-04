package com.hewutao.db.model;

public interface Entity {
    boolean isExisted();
    String getId();
    void delete();
    void cascadeDelete();
    boolean isDeleted();

    interface EntityBuilder<T extends Entity, B extends EntityBuilder<T, B>> {
        B id(String id);
        T build();
    }
}
