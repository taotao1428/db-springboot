package com.hewutao.db.dao.base.dao;

import com.hewutao.db.dao.base.model.EndpointPO;

import java.util.List;

public interface EndpointDAO {
    List<EndpointPO> getAllUndeletedEndpointsByIds(List<String> endpointIds);

    void add(EndpointPO endpointPO);

    void update(EndpointPO endpointPO);
}
