package com.hewutao.db.model2;

import com.hewutao.db.model.EndpointPurpose;
import com.hewutao.db.model.EntityStatus;

public class Endpoint extends Entity {
    private String ip;
    private String iaasId;
    private EndpointPurpose purpose;
    private Instance instance;
    private EntityStatus status;

    public Endpoint(String id, String ip, String iaasId, EndpointPurpose purpose, EntityStatus status, Instance instance) {
        this(id, ip, iaasId, purpose, status, instance, false);
    }

    public Endpoint(String id, String ip, String iaasId, EndpointPurpose purpose, EntityStatus status, Instance instance, boolean existed) {
        super(id, existed);
        this.ip = ip;
        this.iaasId = iaasId;
        this.purpose = purpose;
        this.status = status;
        this.instance = instance;

        if (existed) {
            saveOriginal();
        }
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
    public Endpoint getOriginal() {
        return (Endpoint) super.getOriginal();
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
    protected Endpoint createOriginal() {
        return new Endpoint(id, ip, iaasId, purpose, status, instance, existed);
    }
}
