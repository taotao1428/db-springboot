package com.hewutao.db.controller;

import com.hewutao.db.controller.vo.ModifyEndpointIpReqVO;
import com.hewutao.db.controller.vo.ModifyInstanceModeReqVO;
import com.hewutao.db.service.InstanceService;
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
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class InstanceController {
    private final InstanceService instanceService;

    @PostMapping("/instances")
    public CreateInstanceResp create(@RequestBody CreateInstanceReq req) {
        return instanceService.createInstance(req);
    }

    @GetMapping("/instances/{instanceId}")
    public QueryInstanceResp query(@PathVariable String instanceId) {
        QueryInstanceReq req = QueryInstanceReq.builder()
                .id(instanceId)
                .build();
        return instanceService.queryInstance(req);
    }

    @GetMapping("/instances")
    public QueryInstanceListResp queryList(@RequestParam String name, @RequestParam String mode, @RequestParam String enginId) {
        QueryInstanceListReq req = QueryInstanceListReq.builder()
                .name(name)
                .mode(mode)
                .engineId(enginId)
                .build();

        return instanceService.queryInstanceList(req);
    }

    @DeleteMapping(value = "/instances/{instanceId}", produces = "application/json")
    public DeleteInstanceResp delete(@PathVariable String instanceId) {
        DeleteInstanceReq req = DeleteInstanceReq.builder()
                .id(instanceId)
                .build();

        return instanceService.deleteInstance(req);
    }

    @PutMapping("/instances/{instanceId}/actions/modify_mode")
    public ModifyInstanceModeResp modifyMode(@PathVariable String instanceId, @RequestBody ModifyInstanceModeReqVO reqVO) {
        ModifyInstanceModeReq req = ModifyInstanceModeReq.builder()
                .instanceId(instanceId)
                .mode(reqVO.getMode())
                .build();

        return instanceService.modifyInstanceMode(req);
    }

    @PutMapping("/instances/{instanceId}/actions/modify_ip")
    public ModifyEndpointIpResp modifyIp(@PathVariable String instanceId, @RequestBody ModifyEndpointIpReqVO reqVO) {
        ModifyEndpointIpReq req = ModifyEndpointIpReq.builder()
                .instanceId(instanceId)
                .ip(reqVO.getIp())
                .build();

        return instanceService.modifyEndpointIp(req);
    }
}
