package com.hewutao.db.service.impl;

import com.hewutao.db.dao.repository.InstanceRepository;
import com.hewutao.db.exception.InstanceNotExistedException;
import com.hewutao.db.model.EndpointPurpose;
import com.hewutao.db.model.EntityStatus;
import com.hewutao.db.model.QueryInstanceCondition;
import com.hewutao.db.model.Endpoint;
import com.hewutao.db.model.Instance;
import com.hewutao.db.model.InstanceMode;
import com.hewutao.db.model.Node;
import com.hewutao.db.service.InstanceService;
import com.hewutao.db.service.dto.CreateInstanceReq;
import com.hewutao.db.service.dto.CreateInstanceResp;
import com.hewutao.db.service.dto.DeleteInstanceReq;
import com.hewutao.db.service.dto.DeleteInstanceResp;
import com.hewutao.db.service.dto.EndpointDTO;
import com.hewutao.db.service.dto.InstanceDTO;
import com.hewutao.db.service.dto.ModifyEndpointIpReq;
import com.hewutao.db.service.dto.ModifyEndpointIpResp;
import com.hewutao.db.service.dto.ModifyInstanceModeReq;
import com.hewutao.db.service.dto.ModifyInstanceModeResp;
import com.hewutao.db.service.dto.NodeDTO;
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
        if (instance == null) {
            return null;
        }
        return InstanceDTO.builder()
                .id(instance.getId())
                .name(instance.getId())
                .mode(instance.getMode().name())
                .enginId(instance.getEngineId())
                .status(instance.getStatus().getDbStatus())
                .endpoint(convertEndpoint(instance.getDataEndpoint()))
                .nodes(instance.getNodes().stream().map(InstanceServiceImpl::convertNode).collect(Collectors.toList()))
                .build();
    }

    private static NodeDTO convertNode(Node node) {
        if (node == null) {
            return null;
        }

        return NodeDTO.builder()
                .id(node.getId())
                .name(node.getName())
                .status(node.getStatus().getDbStatus())
                .build();
    }

    private static EndpointDTO convertEndpoint(Endpoint endpoint) {
        if (endpoint == null) {
            return null;
        }

        return EndpointDTO.builder()
                .ip(endpoint.getIp())
                .build();
    }

    @Override
    public DeleteInstanceResp deleteInstance(DeleteInstanceReq req) {
        Instance instance = instanceRepository.getById(req.getId());
        if (instance != null) {
            instance.cascadeDelete();
            instanceRepository.saveInstance(instance);
        }

        return DeleteInstanceResp.builder().id(req.getId()).build();
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
                .map(InstanceServiceImpl::convertInstance)
                .collect(Collectors.toList());

        return QueryInstanceListResp.builder()
                .instances(instanceDTOS)
                .build();
    }

    @Override
    public ModifyInstanceModeResp modifyInstanceMode(ModifyInstanceModeReq req) {
        Instance instance = instanceRepository.getById(req.getInstanceId());
        if (instance == null) {
            throw new IllegalArgumentException("instance [" + req.getInstanceId() + "] is not existed");
        }

        InstanceMode targetMode = InstanceMode.from(req.getMode());

        if (instance.getMode() != targetMode) {
            if (instance.getMode() == InstanceMode.SINGLE && targetMode == InstanceMode.HA) {
                modifySingleToHa(instance);
            } else if (instance.getMode() == InstanceMode.HA && targetMode == InstanceMode.SINGLE) {
                modifyHaToSingle(instance);
            } else {
                throw new IllegalArgumentException("unSupport modify mode [" + instance.getMode().name() + "] to mode [" + targetMode.name() + "]");
            }

            instanceRepository.saveInstance(instance);
        }
        return ModifyInstanceModeResp.builder().instance(convertInstance(instance)).build();
    }

    private void modifySingleToHa(Instance instance) {
        List<Node> nodes = instance.getNodes();
        if (nodes.size() != 1) {
            throw new IllegalStateException("there are " + nodes.size() + " node in single instance, expect 1 node");
        }

        Node slaveNode = new Node(
                UUID.randomUUID().toString(),
                instance.getName() + "_node2",
                EntityStatus.NORMAL,
                instance
        );

        instance.addNode(slaveNode);
        instance.setMode(InstanceMode.HA);
    }

    private void modifyHaToSingle(Instance instance) {
        List<Node> nodes = instance.getNodes();
        if (nodes.size() != 2) {
            throw new IllegalStateException("there are " + nodes.size() + " node in ha instance, expect 2 node");
        }

        nodes.get(1).delete();
        instance.setMode(InstanceMode.SINGLE);
    }

    @Override
    public ModifyEndpointIpResp modifyEndpointIp(ModifyEndpointIpReq req) {
        Instance instance = instanceRepository.getById(req.getInstanceId());
        if (instance == null) {
            throw new InstanceNotExistedException(req.getInstanceId());
        }

        Endpoint dataEndpoint = instance.getDataEndpoint();
        if (!dataEndpoint.getIp().equals(req.getIp())) {
            dataEndpoint.delete();
            Endpoint newDataEndpoint = new Endpoint(
                    UUID.randomUUID().toString(),
                    req.getIp(),
                    UUID.randomUUID().toString(),
                    EndpointPurpose.DATA,
                    EntityStatus.NORMAL,
                    instance
            );
            instance.addEndpoint(newDataEndpoint);

            instanceRepository.saveInstance(instance);
        }

        return ModifyEndpointIpResp.builder().instance(convertInstance(instance)).build();
    }
}
