package com.hewutao.db.model;

import com.hewutao.db.dao.base.model.EndpointPO;

public class Endpoint extends Entity {
    private String ip;
    private String iaasId;
    private EndpointPurpose purpose;
    private Instance instance;
    private EntityStatus status;

    public Endpoint(String id, String ip, String iaasId, EndpointPurpose purpose, EntityStatus status, Instance instance) {
        this(id, ip, iaasId, purpose, status, instance, null);
    }

    public Endpoint(String id, String ip, String iaasId, EndpointPurpose purpose, EntityStatus status, Instance instance, EndpointPO original) {
        super(id, original);
        this.ip = ip;
        this.iaasId = iaasId;
        this.purpose = purpose;
        this.status = status;
        this.instance = instance;
    }

    public String getIp() {
        return this.ip;
    }

    public String getIaasId() {
        return this.iaasId;
    }

    public EndpointPurpose getPurpose() {
        return purpose;
    }

    public Instance getInstance() {
        return instance;
    }

    public EntityStatus getStatus() {
        return this.status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
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
