package com.hewutao.db.model;

public interface Node extends Entity {
    String getName();
    void setName(String name);
    Instance getInstance();
    EntityStatus getStatus();
    void setStatus(EntityStatus status);

    interface NodeBuilder extends EntityBuilder<Node, NodeBuilder> {
        NodeBuilder name(String name);
        NodeBuilder instance(Instance instance);
        NodeBuilder status(EntityStatus status);
    }

    static NodeBuilder builder() {
        return new NodeImpl.NodeBuilderImpl();
    }
}
