package com.hewutao.db.model;

public abstract class Entity  {
    // 保留原来的值, 方便回滚
    protected Object oldOriginal;
    protected Object original;
    protected final String id;

    protected Entity(String id, Object original) {
        this.id = id;
        this.original = original;
    }

    public String getId() {
        return id;
    }

    public abstract boolean isDeleted();

    public abstract void delete();

    public void cascadeDelete() {
        this.delete();
    }

    public Object innerGetOriginal() {
        return this.original;
    }


    public void innerPrepareForSave(Object original) {
        this.oldOriginal = this.original;
        this.original = original;
    }

    public void innerRollback() {
        this.original = this.oldOriginal;
        this.oldOriginal = null;
    }

    public boolean isExisted() {
        return this.original != null;
    }
}
