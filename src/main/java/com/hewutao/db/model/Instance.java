package com.hewutao.db.model;

import java.util.List;

public interface Instance extends Entity {
    String getName();
    void setName(String name);

    String getEngineId();
    void setEngineId(String engineId);

    InstanceMode getMode();
    void setMode(InstanceMode mode);

    List<Endpoint> getEndpoints();
    Endpoint getDataEndpoint();
    Endpoint getMgntEndpoint();
    boolean addEndpoint(Endpoint endpoint);


    List<Node> getNodes();
    Node getNode(String id);
    boolean addNode(Node node);


    EntityStatus getStatus();
    void setStatus(EntityStatus status);

    interface InstanceBuilder extends EntityBuilder<Instance, InstanceBuilder> {
        InstanceBuilder name(String name);
        InstanceBuilder engineId(String engineId);
        InstanceBuilder mode(InstanceMode mode);
        // InstanceBuilder endpoints(List<Endpoint> endpoints);
        // InstanceBuilder endpoint(Endpoint endpoint);
        // InstanceBuilder nodes(List<Node> nodes);
        // InstanceBuilder node(Node node);
        InstanceBuilder status(EntityStatus status);
    }

    static InstanceBuilder builder() {
        return new InstanceImpl.InstanceBuilderImpl();
    }
}
