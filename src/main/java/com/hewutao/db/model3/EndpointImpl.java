package com.hewutao.db.model3;

import com.hewutao.db.model.EndpointPurpose;
import com.hewutao.db.model.EntityStatus;

public class EndpointImpl extends BaseEntity implements Endpoint {
    private final Field<String> ipField;
    private final Field<String> iaasIdField;
    private final Field<EndpointPurpose> purposeField;
    private final InstanceImpl instance;
    private final Field<EntityStatus> statusField;


    private EndpointImpl(String id, String ip, String iaasId, EndpointPurpose purpose, Instance instance, EntityStatus status, boolean existed) {
        // TODO 增加创建对象时的校验
        super(id, existed);
        ipField = Field.of(ip);
        iaasIdField = Field.of(iaasId);
        purposeField = Field.of(purpose);
        this.instance = (InstanceImpl) instance;
        statusField = Field.of(status);
    }


    @Override
    public String getIp() {
        return ipField.getValue();
    }

    @Override
    public String getIaasId() {
        return iaasIdField.getValue();
    }

    @Override
    public Instance getInstance() {
        return instance;
    }

    @Override
    public EntityStatus getStatus() {
        return statusField.getValue();
    }

    @Override
    public void setStatus(EntityStatus status) {
        if (status == EntityStatus.DELETED) {
            throw new IllegalArgumentException("entity cannot be set deleted status");
        }
        statusField.update(status);
    }

    @Override
    public void delete() {
        statusField.update(EntityStatus.DELETED);
    }

    @Override
    public EndpointPurpose getPurpose() {
        return purposeField.getValue();
    }

    @Override
    public boolean isDeleted() {
        return statusField.getValue() == EntityStatus.DELETED;
    }


    static class EndpointBuilderImpl extends BaseEntityBuilder<Endpoint, EndpointBuilder, EndpointBuilderImpl> implements EndpointBuilder {
        private String ip;
        private String iaasId;
        private EntityStatus status;
        private Instance instance;
        private EndpointPurpose purpose;

        @Override
        public EndpointBuilderImpl ip(String ip) {
            this.ip = ip;
            return this;
        }

        @Override
        public EndpointBuilderImpl iaasId(String iaasId) {
            this.iaasId = iaasId;
            return this;
        }

        @Override
        public EndpointBuilderImpl status(EntityStatus status) {
            this.status = status;
            return this;
        }

        @Override
        public EndpointBuilderImpl purpose(EndpointPurpose purpose) {
            this.purpose = purpose;
            return this;
        }

        public EndpointBuilderImpl instance(Instance instance) {
            this.instance = instance;
            return this;
        }

        @Override
        public EndpointImpl build() {
            return new EndpointImpl(id, ip, iaasId, purpose, instance, status, existed);
        }
    }
}
