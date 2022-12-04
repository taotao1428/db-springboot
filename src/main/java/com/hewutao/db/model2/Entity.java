package com.hewutao.db.model2;

public abstract class Entity {
    // 保留原来的值
    protected Entity original;
    protected final String id;
    protected boolean existed = false;

    protected Entity(String id, boolean existed) {
        this.id = id;
        this.existed = existed;
    }

    public String getId() {
        return id;
    }

    public abstract boolean isDeleted();

    public abstract void delete();

    public void cascadeDelete() {
        this.delete();
    }

    protected void saveOriginal() {
        if (original == null) {
            original = createOriginal();
        }
    }

    public Entity getOriginal() {
        return this.original;
    }

    public void deleteOriginal() {
        this.original = null;
    }

    public boolean isExisted() {
        return this.existed;
    }

    protected abstract Entity createOriginal();

    public void saved() {
        this.existed = true;
        this.original = createOriginal();
    }
}
