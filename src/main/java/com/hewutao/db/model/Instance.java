package com.hewutao.db.model;

import com.hewutao.db.dao.base.model.InstancePO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Instance extends Entity {
    private String name;
    private String engineId;
    private InstanceMode mode;
    private EntityStatus status;
    private final List<Endpoint> endpoints;
    private final List<Node> nodes;

    public Instance(String id, String name, String engineId, InstanceMode mode, EntityStatus status) {
        this(id, name, engineId, mode, status, new ArrayList<>(), new ArrayList<>(), null);
    }

    public Instance(String id, String name, String engineId, InstanceMode mode, EntityStatus status, List<Endpoint> endpoints, List<Node> nodes) {
        this(id, name, engineId, mode, status, endpoints, nodes, null);
    }

    public Instance(String id, String name, String engineId, InstanceMode mode, EntityStatus status, List<Endpoint> endpoints, List<Node> nodes, InstancePO original) {
        super(id, original);
        this.name = name;
        this.engineId = engineId;
        this.mode = mode;
        this.status = status;
        this.endpoints = endpoints;
        this.nodes = nodes;
    }




    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngineId() {
        return this.engineId;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public InstanceMode getMode() {
        return this.mode;
    }

    public void setMode(InstanceMode mode) {
        this.mode = mode;
    }

    public List<Endpoint> getEndpoints() {
        return Collections.unmodifiableList(this.endpoints);
    }

    // 供内部使用
    public List<Endpoint> innerGetEndpoints() {
        return this.endpoints;
    }

    public Endpoint getDataEndpoint() {
        List<Endpoint> dataEndpoints = getEndpoints().stream()
                .filter(endpoint -> endpoint.getPurpose() == EndpointPurpose.DATA)
                .filter(endpoint -> !endpoint.isDeleted())
                .collect(Collectors.toList());

        if (dataEndpoints.size() > 1) {
            throw new IllegalArgumentException("more than one data endpoint");
        }

        return dataEndpoints.size() == 1 ? dataEndpoints.get(0) : null;
    }

    public Endpoint getMgntEndpoint() {
        List<Endpoint> mgntEndpoints = getEndpoints().stream()
                .filter(endpoint -> endpoint.getPurpose() == EndpointPurpose.MANAGEMENT)
                .filter(endpoint -> !endpoint.isDeleted())
                .collect(Collectors.toList());

        if (mgntEndpoints.size() > 1) {
            throw new IllegalArgumentException("more than one management endpoint");
        }

        return mgntEndpoints.size() == 1 ? mgntEndpoints.get(0) : null;
    }

    public boolean addEndpoint(Endpoint endpoint) {
        boolean existed = endpoints.stream().anyMatch(e -> e.getId().equals(endpoint.getId()));
        if (existed) {
            return false;
        }
        this.endpoints.add(endpoint);
        return true;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Node> innerGetNodes() {
        return this.nodes;
    }

    public Node getNode(String id) {
        return nodes.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    public boolean addNode(Node node) {
        boolean existed = nodes.stream().anyMatch(e -> e.getId().equals(node.getId()));
        if (existed) {
            return false;
        }
        this.nodes.add(node);
        return true;
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

    @Override
    public void cascadeDelete() {
        delete();
        for (Node node : nodes) {
            node.cascadeDelete();
        }
        for (Endpoint ep : endpoints) {
            ep.cascadeDelete();
        }
    }
}
