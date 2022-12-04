package com.hewutao.db.model3;

import com.hewutao.db.model.EntityStatus;

public class NodeImpl extends BaseEntity implements Node {
    private final Field<String> nameField;
    private final InstanceImpl instance;
    private final Field<EntityStatus> statusField;

    public NodeImpl(String id, String name, Instance instance, EntityStatus status, boolean existed) {
        super(id, existed);
        this.nameField = Field.of(name);
        this.instance = (InstanceImpl) instance;
        this.statusField = Field.of(status);
    }


    @Override
    public String getName() {
        return nameField.getValue();
    }

    @Override
    public void setName(String name) {
        nameField.update(name);
    }

    @Override
    public InstanceImpl getInstance() {
        return instance;
    }

    @Override
    public EntityStatus getStatus() {
        return statusField.getValue();
    }

    @Override
    public void setStatus(EntityStatus status) {
        statusField.update(status);
    }


    @Override
    public void delete() {
        statusField.update(EntityStatus.DELETED);
    }

    @Override
    public boolean isDeleted() {
        return statusField.getValue() == EntityStatus.DELETED;
    }

    static class NodeBuilderImpl extends BaseEntityBuilder<Node, NodeBuilder, NodeBuilderImpl> implements NodeBuilder {
        private String name;
        private Instance instance;
        private EntityStatus status;

        @Override
        public NodeBuilderImpl name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public NodeBuilderImpl instance(Instance instance) {
            this.instance = instance;
            return this;
        }

        @Override
        public NodeBuilderImpl status(EntityStatus status) {
            this.status = status;
            return this;
        }

        @Override
        public NodeImpl build() {
            return new NodeImpl(id, name, instance, status, existed);
        }
    }
}
