package com.hewutao.db.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class InstanceImpl extends BaseEntity implements Instance {
    private final Field<String> nameField;
    private final Field<String> engineIdField;
    private final Field<InstanceMode> modeField;
    private final Field<EntityStatus> statusField;
    private final ListField<Endpoint> endpointsField;
    private final ListField<Node> nodesField;

    public InstanceImpl(String id, String name, String engineId, InstanceMode mode, EntityStatus status) {
        super(id, false);
        this.nameField = Field.of(name);
        this.engineIdField = Field.of(engineId);
        this.modeField = Field.of(mode);
        this.statusField = Field.of(status);

        this.endpointsField = Field.ofEntities(new ArrayList<>());
        this.nodesField = Field.ofEntities(new ArrayList<>());
    }

    public InstanceImpl(String id, String name, String engineId, InstanceMode mode, EntityStatus status,
                        Supplier<List<Endpoint>> endpointsSupplier, Supplier<List<Node>> nodesSupplier) {
        super(id, true);
        this.nameField = Field.of(name);
        this.engineIdField = Field.of(engineId);
        this.modeField = Field.of(mode);
        this.statusField = Field.of(status);

        this.endpointsField = Field.ofDelayEntities(endpointsSupplier);
        this.nodesField = Field.ofDelayEntities(nodesSupplier);
    }

    @Override
    public void delete() {
        statusField.update(EntityStatus.DELETED);
    }

    @Override
    public boolean isDeleted() {
        return statusField.getValue() == EntityStatus.DELETED;
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
    public String getEngineId() {
        return engineIdField.getValue();
    }

    @Override
    public void setEngineId(String engineId) {
        engineIdField.update(engineId);
    }

    @Override
    public InstanceMode getMode() {
        return modeField.getValue();
    }

    @Override
    public void setMode(InstanceMode mode) {
        modeField.update(mode);
    }

    @Override
    public List<Endpoint> getEndpoints() {
        return endpointsField.getValue();
    }

    @Override
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

    @Override
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

    @Override
    public boolean addEndpoint(Endpoint endpoint) {
        return endpointsField.add(endpoint);
    }

    @Override
    public List<Node> getNodes() {
        return nodesField.getValue();
    }

    @Override
    public Node getNode(String id) {
        return getNodes().stream()
                .filter(node -> node.getId().equals(id))
                .filter(node -> !node.isDeleted())
                .findFirst().orElse(null);
    }

    @Override
    public boolean addNode(Node node) {
        return nodesField.add(node);
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
    public void cascadeDelete() {
        super.cascadeDelete();
    }

    static class InstanceBuilderImpl extends BaseEntityBuilder<Instance, InstanceBuilder, InstanceBuilderImpl> implements InstanceBuilder {
        private String name;
        private String engineId;
        private InstanceMode mode;
        private EntityStatus status;
        private Supplier<List<Endpoint>> endpointsSupplier;
        private Supplier<List<Node>> nodesSupplier;

        @Override
        public InstanceBuilderImpl name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public InstanceBuilderImpl engineId(String engineId) {
            this.engineId = engineId;
            return this;
        }

        @Override
        public InstanceBuilderImpl mode(InstanceMode mode) {
            this.mode = mode;
            return this;
        }

        @Override
        public InstanceBuilderImpl status(EntityStatus status) {
            this.status = status;
            return this;
        }

        public InstanceBuilderImpl endpointsSupplier(Supplier<List<Endpoint>> endpointsSupplier) {
            this.endpointsSupplier = endpointsSupplier;
            return this;
        }

        public InstanceBuilderImpl nodesSupplier(Supplier<List<Node>> nodesSupplier) {
            this.nodesSupplier = nodesSupplier;
            return this;
        }


        @Override
        public InstanceImpl build() {
            if (endpointsSupplier != null) {
                return new InstanceImpl(id, name, engineId, mode, status, endpointsSupplier, nodesSupplier);
            } else {
                return new InstanceImpl(id, name, engineId, mode, status);
            }

        }
    }
}
