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
import com.hewutao.db.model.support.SaveEntityEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class InstanceRepositoryImpl implements InstanceRepository {
    private ApplicationEventPublisher publisher;
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
                instancePO
        );

        delayEndpointList.setEntity(instance);
        delayNodeList.setEntity(instance);
        return instance;
    }

    @Transactional
    @Override
    public void saveInstance(Instance instance) {
        if (instance.isDeleted() && !instance.isExisted()) {
            log.info("instance [{}] is deleted and is not existed, dont need save", instance.getId());
            return;
        }

        saveCascadeEntity(instance);

        if (instance.isDeleted()) {
            if (instance.isExisted()) {
                updateInstance(instance);
            }
        } else {
            if (instance.isExisted()) {
                updateInstance(instance);
            } else {
                addInstance(instance);
            }
        }
    }

    private void saveCascadeEntity(Instance instance) {
        List<Endpoint> endpoints = instance.innerGetEndpoints();

        if (hasLoaded(endpoints)) {
            endpointRepository.saveAllEndpoint(endpoints);
        }

        List<Node> nodes = instance.innerGetNodes();

        if (hasLoaded(nodes)) {
            nodeRepository.saveAllNodes(nodes);
        }
    }

    private void publishEventForRollback(Instance instance, InstancePO current) {
        // 发布保存实例的事件，方便回滚
        instance.innerPrepareForSave(current);
        log.info("instance [{}] is updated or added, publish event", instance.getId());
        publisher.publishEvent(new SaveEntityEvent(instance));
    }

    private boolean hasLoaded(List<? extends Entity> entityList) {
        return !(entityList instanceof DelayList) || ((DelayList<?, ?>) entityList).loaded();
    }

    private void updateInstance(Instance instance) {
        InstancePO original = (InstancePO) instance.innerGetOriginal();
        InstancePO current = convertToPo(instance);
        // 没有变化
        if (Objects.equals(current.getName(), original.getName())
                && Objects.equals(current.getMode(), original.getMode())
                && Objects.equals(current.getEngineId(), original.getEngineId())
                && Objects.equals(current.getStatus(), original.getStatus())) {
            return;
        }
        instanceDAO.update(current);

        publishEventForRollback(instance, current);
    }

    private void addInstance(Instance instance) {
        InstancePO current = convertToPo(instance);
        instanceDAO.add(current);
        publishEventForRollback(instance, current);
    }

    private InstancePO convertToPo(Instance instance) {
        InstancePO po = new InstancePO();
        po.setId(instance.getId());
        po.setName(instance.getName());
        po.setMode(instance.getMode().name());
        po.setEngineId(instance.getEngineId());
        po.setStatus(instance.getStatus().getDbStatus());

        return po;
    }

    @Override
    public List<Instance> query(QueryInstanceCondition condition) {
        List<InstancePO> instancePOS = instanceDAO.queryUndeletedInstances(null, condition.getName(), condition.getMode(), condition.getEngineId());
        return instancePOS.stream()
                .map(po -> convertFromPo(po))
                .collect(Collectors.toList());
    }
}
