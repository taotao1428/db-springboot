package com.hewutao.db.dao.repository;

import com.hewutao.db.model2.Endpoint;
import com.hewutao.db.model2.Instance;

import java.util.List;

public interface EndpointRepository {
    List<Endpoint> getEndpointsByInstance(Instance instance);
    void saveEndpoint(Endpoint endpoint);
    void saveAllEndpoint(List<Endpoint> endpoints);
}
