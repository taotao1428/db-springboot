package com.hewutao.db.model2;

import com.hewutao.db.model.EntityStatus;

public class Node extends Entity {
    private String name;
    private EntityStatus status;
    private final Instance instance;

    public Node(String id, String name, EntityStatus status, Instance instance) {
        this(id, name, status, instance, false);
    }

    public Node(String id, String name, EntityStatus status, Instance instance, boolean existed) {
        super(id, existed);
        this.name = name;
        this.status = status;
        this.instance = instance;

        if (existed) {
            saveOriginal();
        }
    }


    public Instance getInstance() {
        return instance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public EntityStatus getStatus() {
        return status;
    }


    @Override
    public Node getOriginal() {
        return (Node) super.getOriginal();
    }

    @Override
    public boolean isDeleted() {
        return this.status == EntityStatus.DELETED;
    }

    @Override
    public void delete() {
        this.status = EntityStatus.DELETED;
    }

    @Override
    protected Entity createOriginal() {
        return new Node(id, name, status, instance, existed);
    }
}
