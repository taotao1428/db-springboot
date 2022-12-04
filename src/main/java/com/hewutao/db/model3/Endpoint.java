package com.hewutao.db.model3;

import com.hewutao.db.model.EndpointPurpose;
import com.hewutao.db.model.EntityStatus;

public interface Endpoint extends Entity {
    String getIp();
    String getIaasId();
    EndpointPurpose getPurpose();
    Instance getInstance();
    EntityStatus getStatus();
    void setStatus(EntityStatus status);

    interface EndpointBuilder extends EntityBuilder<Endpoint, EndpointBuilder> {
        EndpointBuilder ip(String ip);
        EndpointBuilder iaasId(String iaasId);
        EndpointBuilder purpose(EndpointPurpose purpose);
        EndpointBuilder instance(Instance instance);
        EndpointBuilder status(EntityStatus status);
    }

    static EndpointBuilder builder() {
        return new EndpointImpl.EndpointBuilderImpl();
    }
}