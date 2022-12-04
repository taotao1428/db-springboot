package com.hewutao.db.service.impl;

import com.hewutao.db.dao.repository.InstanceRepository;
import com.hewutao.db.model.EndpointPurpose;
import com.hewutao.db.model.EntityStatus;
import com.hewutao.db.model.QueryInstanceCondition;
import com.hewutao.db.model2.Endpoint;
import com.hewutao.db.model2.Instance;
import com.hewutao.db.model.InstanceMode;
import com.hewutao.db.model2.Node;
import com.hewutao.db.service.InstanceService;
import com.hewutao.db.service.dto.CreateInstanceReq;
import com.hewutao.db.service.dto.CreateInstanceResp;
import com.hewutao.db.service.dto.InstanceDTO;
import com.hewutao.db.service.dto.QueryInstanceListReq;
import com.hewutao.db.service.dto.QueryInstanceListResp;
import com.hewutao.db.service.dto.QueryInstanceReq;
import com.hewutao.db.service.dto.QueryInstanceResp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstanceServiceImpl implements InstanceService {
    private final InstanceRepository instanceRepository;

    @Override
    public CreateInstanceResp createInstance(CreateInstanceReq req) {
        String instanceId = UUID.randomUUID().toString();
        InstanceMode mode = InstanceMode.from(req.getMode());

        Instance instance = new Instance(
                instanceId,
                req.getName(),
                req.getEngineId(),
                InstanceMode.from(req.getMode()),
                EntityStatus.NORMAL
        );

        if (mode == InstanceMode.HA) {
            Node masterNode = new Node(
                    UUID.randomUUID().toString(),
                    req.getName() + "_node1",
                    EntityStatus.NORMAL,
                    instance
            );

            Node slaveNode = new Node(
                    UUID.randomUUID().toString(),
                    req.getName() + "_node2",
                    EntityStatus.NORMAL,
                    instance
            );
            instance.addNode(masterNode);
            instance.addNode(slaveNode);
        } else if (mode == InstanceMode.SINGLE) {
            Node masterNode = new Node(
                    UUID.randomUUID().toString(),
                    req.getName() + "_node1",
                    EntityStatus.NORMAL,
                    instance
            );
            instance.addNode(masterNode);
        } else {
            throw new IllegalStateException("unsupported mode [" + mode + "]");
        }

        Endpoint dataEndpoint = new Endpoint(
                UUID.randomUUID().toString(),
                "192.168.10.10",
                UUID.randomUUID().toString(),
                EndpointPurpose.DATA,
                EntityStatus.NORMAL,
                instance
        );

        Endpoint mgntEndpoint = new Endpoint(
                UUID.randomUUID().toString(),
                "172.100.10.10",
                UUID.randomUUID().toString(),
                EndpointPurpose.MANAGEMENT,
                EntityStatus.NORMAL,
                instance
        );

        instance.addEndpoint(dataEndpoint);
        instance.addEndpoint(mgntEndpoint);

        instanceRepository.saveInstance(instance);


        return CreateInstanceResp.builder()
                .instance(convertInstance(instance))
                .build();
    }

    private static InstanceDTO convertInstance(Instance instance) {
        return InstanceDTO.builder()
                .id(instance.getId())
                .name(instance.getId())
                .mode(instance.getMode().name())
                .enginId(instance.getEngineId())
                .build();
    }

    @Override
    public QueryInstanceResp queryInstance(QueryInstanceReq req) {
        Instance instance = instanceRepository.getById(req.getId());

        return QueryInstanceResp.builder()
                .instance(convertInstance(instance))
                .build();
    }

    @Override
    public QueryInstanceListResp queryInstanceList(QueryInstanceListReq req) {
        QueryInstanceCondition condition = QueryInstanceCondition.builder()
                .name(req.getName())
                .mode(req.getMode())
                .engineId(req.getEngineId())
                .build();


        List<InstanceDTO> instanceDTOS = instanceRepository.query(condition).stream()
                .map(instance -> convertInstance(instance))
                .collect(Collectors.toList());

        return QueryInstanceListResp.builder()
                .instances(instanceDTOS)
                .build();
    }
}
