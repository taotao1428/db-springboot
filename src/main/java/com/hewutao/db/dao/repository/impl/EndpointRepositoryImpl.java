package com.hewutao.db.dao.repository.impl;

import com.hewutao.db.dao.base.dao.EndpointDAO;
import com.hewutao.db.dao.base.dao.ParentDAO;
import com.hewutao.db.dao.base.model.EndpointPO;
import com.hewutao.db.dao.repository.EndpointRepository;
import com.hewutao.db.model.EndpointPurpose;
import com.hewutao.db.model.EntityStatus;
import com.hewutao.db.model.Endpoint;
import com.hewutao.db.model.Instance;
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
public class EndpointRepositoryImpl implements EndpointRepository {
    private ApplicationEventPublisher publisher;
    private ParentDAO parentDAO;
    private EndpointDAO endpointDAO;

    @Override
    public List<Endpoint> getEndpointsByInstance(Instance instance) {
        List<String> endpointIds = parentDAO.getEndpointIdsByInstanceId(instance.getId());
        List<EndpointPO> endpointPOS = endpointDAO.getAllUndeletedEndpointsByIds(endpointIds);
        return endpointPOS.stream()
                .map(po -> new Endpoint(
                        po.getId(),
                        po.getIp(),
                        po.getIaasId(),
                        EndpointPurpose.from(po.getPurpose()),
                        EntityStatus.from(po.getStatus()),
                        instance,
                        po
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveEndpoint(Endpoint endpoint) {
        if (endpoint.isDeleted() && !endpoint.isExisted()) {
            log.debug("endpoint [{}] is deleted and is not existed, dont need save", endpoint.getId());
            return;
        }

        Instance instance = endpoint.getInstance();
        if (endpoint.isDeleted()) {
            if (endpoint.isExisted()) {
                parentDAO.removeEndpointInstanceParentShip(endpoint.getId(), instance.getId());
                updateEndpoint(endpoint);
            }
        } else {
            if (!endpoint.isExisted()) {
                parentDAO.addEndpointInstanceParentShip(endpoint.getId(), instance.getId());
                addEndpoint(endpoint);
            } else {
                updateEndpoint(endpoint);
            }
        }
    }

    private void publishEventForRollback(Endpoint endpoint, EndpointPO current) {
        // 发送事件，方便回滚
        endpoint.innerPrepareForSave(current);
        log.info("endpoint [{}] is updated or added, publish event", endpoint.getId());
        publisher.publishEvent(new SaveEntityEvent(endpoint));
    }

    private void updateEndpoint(Endpoint endpoint) {
        EndpointPO original = (EndpointPO) endpoint.innerGetOriginal();
        EndpointPO current = convertToPO(endpoint);
        // 没有改变
        if (Objects.equals(current.getIp(), original.getIp())
                && Objects.equals(current.getIaasId(), original.getIaasId())
                && Objects.equals(current.getPurpose(), original.getPurpose())
                && Objects.equals(current.getStatus(), original.getStatus())) {
            return;
        }

        endpointDAO.update(current);
        publishEventForRollback(endpoint, current);
    }

    private void addEndpoint(Endpoint endpoint) {
        EndpointPO current = convertToPO(endpoint);
        endpointDAO.add(current);
        publishEventForRollback(endpoint, current);
    }


    private EndpointPO convertToPO(Endpoint endpoint) {
        EndpointPO po = new EndpointPO();
        po.setId(endpoint.getId());
        po.setIp(endpoint.getIp());
        po.setIaasId(endpoint.getIaasId());
        po.setPurpose(endpoint.getPurpose().name());
        po.setStatus(endpoint.getStatus().getDbStatus());

        return po;
    }


    @Transactional
    @Override
    public void saveAllEndpoint(List<Endpoint> endpoints) {
        if (endpoints == null || endpoints.isEmpty()) {
            return;
        }

        for (Endpoint endpoint : endpoints) {
            saveEndpoint(endpoint);
        }
    }
}
