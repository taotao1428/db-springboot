package com.hewutao.db.service;

import com.hewutao.db.service.dto.*;

public interface InstanceService {
    CreateInstanceResp createInstance(CreateInstanceReq req);
    QueryInstanceResp queryInstance(QueryInstanceReq req);
    QueryInstanceListResp queryInstanceList(QueryInstanceListReq req);
}
