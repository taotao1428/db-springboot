package com.hewutao.db.model;

import com.hewutao.db.dao.base.model.NodePO;

public class Node extends Entity {
    private String name;
    private EntityStatus status;
    private final Instance instance;

    public Node(String id, String name, EntityStatus status, Instance instance) {
        this(id, name, status, instance, null);
    }

    public Node(String id, String name, EntityStatus status, Instance instance, NodePO original) {
        super(id, original);
        this.name = name;
        this.status = status;
        this.instance = instance;
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
    public boolean isDeleted() {
        return this.status == EntityStatus.DELETED;
    }

    @Override
    public void delete() {
        this.status = EntityStatus.DELETED;
    }

}
