package com.hewutao.db.service;

import com.hewutao.db.service.dto.CreateInstanceReq;
import com.hewutao.db.service.dto.CreateInstanceResp;
import com.hewutao.db.service.dto.DeleteInstanceReq;
import com.hewutao.db.service.dto.DeleteInstanceResp;
import com.hewutao.db.service.dto.ModifyEndpointIpReq;
import com.hewutao.db.service.dto.ModifyEndpointIpResp;
import com.hewutao.db.service.dto.ModifyInstanceModeReq;
import com.hewutao.db.service.dto.ModifyInstanceModeResp;
import com.hewutao.db.service.dto.QueryInstanceListReq;
import com.hewutao.db.service.dto.QueryInstanceListResp;
import com.hewutao.db.service.dto.QueryInstanceReq;
import com.hewutao.db.service.dto.QueryInstanceResp;

public interface InstanceService {
    CreateInstanceResp createInstance(CreateInstanceReq req);
    QueryInstanceResp queryInstance(QueryInstanceReq req);
    DeleteInstanceResp deleteInstance(DeleteInstanceReq req);
    QueryInstanceListResp queryInstanceList(QueryInstanceListReq req);
    ModifyInstanceModeResp modifyInstanceMode(ModifyInstanceModeReq req);
    ModifyEndpointIpResp modifyEndpointIp(ModifyEndpointIpReq req);
}
