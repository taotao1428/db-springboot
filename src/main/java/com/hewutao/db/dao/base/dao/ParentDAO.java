package com.hewutao.db.dao.base.dao;

import java.util.List;

public interface ParentDAO {
    List<String> getEndpointIdsByInstanceId(String instanceId);
    List<String> getNodeIdsByInstanceId(String instanceId);

    void removeEndpointInstanceParentShip(String endpointId, String instanceId);
    void addEndpointInstanceParentShip(String endpointId, String instanceId);

    void removeNodeInstanceParentShip(String nodeId, String instanceId);
    void addNodeInstanceParentShip(String nodeId, String instanceId);

}
