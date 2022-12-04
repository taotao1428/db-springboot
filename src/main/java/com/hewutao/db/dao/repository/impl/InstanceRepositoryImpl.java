package com.hewutao.db.dao.repository.impl;

import com.hewutao.db.dao.base.dao.InstanceDAO;
import com.hewutao.db.dao.base.model.InstancePO;
import com.hewutao.db.dao.repository.EndpointRepository;
import com.hewutao.db.dao.repository.InstanceRepository;
import com.hewutao.db.dao.repository.NodeRepository;
import com.hewutao.db.model.EntityStatus;
import com.hewutao.db.model.InstanceMode;
import com.hewutao.db.model.QueryInstanceCondition;
import com.hewutao.db.model.Endpoint;
import com.hewutao.db.model.Entity;
import com.hewutao.db.model.Instance;
import com.hewutao.db.model.Node;
import com.hewutao.db.model.support.DelayList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class InstanceRepositoryImpl implements InstanceRepository {

    private InstanceDAO instanceDAO;
    private EndpointRepository endpointRepository;
    private NodeRepository nodeRepository;

    @Override
    public Instance getById(String id) {
        InstancePO instancePO = instanceDAO.getUndeleteById(id);
        if (instancePO == null) {
            return null;
        }

        return convertFromPo(instancePO);
    }

    private Instance convertFromPo(InstancePO instancePO) {
        DelayList<Instance, Endpoint> delayEndpointList = new DelayList<>(ins -> endpointRepository.getEndpointsByInstance(ins));
        DelayList<Instance, Node> delayNodeList = new DelayList<>(ins -> nodeRepository.getAllNodeByInstance(ins));

        Instance instance = new Instance(
                instancePO.getId(),
                instancePO.getName(),
                instancePO.getEngineId(),
                InstanceMode.from(instancePO.getMode()),
                EntityStatus.from(instancePO.getStatus()),
                delayEndpointList,
                delayNodeList,
                true
        );

        delayEndpointList.setEntity(instance);
        delayNodeList.setEntity(instance);
        return instance;
    }

    @Transactional
    @Override
    public void saveInstance(Instance instance) {
        Instance original = instance.getOriginal();
        saveInstance(instance, original);

        List<Endpoint> endpoints = instance.innerGetEndpoints();

        if (hasLoaded(endpoints)) {
            endpointRepository.saveAllEndpoint(endpoints);
        }

        List<Node> nodes = instance.innerGetNodes();

        if (hasLoaded(nodes)) {
            nodeRepository.saveAllNodes(nodes);
        }
    }

    private boolean hasLoaded(List<? extends Entity> entityList) {
        return !(entityList instanceof DelayList) || ((DelayList<?, ?>) entityList).loaded();
    }

    private void saveInstance(Instance instance, Instance original) {
        // 没有变化
        if (original == null || (Objects.equals(instance.getName(), original.getName())
            && Objects.equals(instance.getMode(), original.getMode())
            && Objects.equals(instance.getStatus(), original.getStatus()))) {
            return;
        }

        InstancePO instancePO = new InstancePO();
        instancePO.setId(instance.getId());
        instancePO.setName(instance.getName());
        instancePO.setMode(instance.getMode().name());
        instancePO.setStatus(instance.getStatus().name());

        if (instance.isExisted()) {
            instanceDAO.add(instancePO);
        } else {
            instanceDAO.update(instancePO);
        }
    }

    @Override
    public List<Instance> query(QueryInstanceCondition condition) {
        List<InstancePO> instancePOS = instanceDAO.queryUndeletedInstances(null, condition.getName(), condition.getMode(), condition.getEngineId());
        return instancePOS.stream()
                .map(po -> convertFromPo(po))
                .collect(Collectors.toList());
    }
}
