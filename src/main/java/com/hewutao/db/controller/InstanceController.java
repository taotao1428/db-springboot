package com.hewutao.db.controller;

import com.hewutao.db.controller.vo.ModifyInstanceModeReqVO;
import com.hewutao.db.service.InstanceService;
import com.hewutao.db.service.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/instances/{instanceId}/actions/modify_mode")
    public ModifyInstanceModeResp modifyMode(@PathVariable String instanceId, @RequestBody ModifyInstanceModeReqVO reqVO) {
        ModifyInstanceModeReq req = ModifyInstanceModeReq.builder()
                .instanceId(instanceId)
                .mode(reqVO.getMode())
                .build();

        return instanceService.modifyInstanceMode(req);
    }
}
