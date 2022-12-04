package com.hewutao.db.dao.repository;

import com.hewutao.db.model.Endpoint;
import com.hewutao.db.model.Instance;

import java.util.List;

public interface EndpointRepository {
    List<Endpoint> getEndpointsByInstance(Instance instance);
    void saveEndpoint(Endpoint endpoint);
    void saveAllEndpoint(List<Endpoint> endpoints);
}
