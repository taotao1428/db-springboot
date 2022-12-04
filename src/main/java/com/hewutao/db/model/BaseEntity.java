package com.hewutao.db.model;

public abstract class BaseEntity implements Entity {
    protected String id;
    protected boolean existed;


    protected BaseEntity(String id, boolean existed) {
        this.id = id;
        this.existed = existed;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void cascadeDelete() {
        delete();
    }

    @Override
    public boolean isExisted() {
        return existed;
    }

    protected static abstract class BaseEntityBuilder<T extends Entity, B extends EntityBuilder<T, B>, I extends B> implements EntityBuilder<T, B> {
        protected String id;
        protected boolean existed;

        @SuppressWarnings("unchecked")
        @Override
        public I id(String id) {
            this.id = id;
            return (I) this;
        }

        @SuppressWarnings("unchecked")
        public I existed(boolean existed) {
            this.existed = existed;
            return (I) this;
        }
    }
}
