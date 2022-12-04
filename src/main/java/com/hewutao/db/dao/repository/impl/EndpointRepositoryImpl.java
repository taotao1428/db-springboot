package com.hewutao.db.dao.repository.impl;

import com.hewutao.db.dao.base.dao.EndpointDAO;
import com.hewutao.db.dao.base.dao.ParentDAO;
import com.hewutao.db.dao.base.model.EndpointPO;
import com.hewutao.db.dao.repository.EndpointRepository;
import com.hewutao.db.model.EndpointPurpose;
import com.hewutao.db.model.EntityStatus;
import com.hewutao.db.model.Endpoint;
import com.hewutao.db.model.Instance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class EndpointRepositoryImpl implements EndpointRepository {
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
                        true
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveEndpoint(Endpoint endpoint) {
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

    private void updateEndpoint(Endpoint endpoint) {
        Endpoint original = endpoint.getOriginal();

        // 没有改变
        if (original == null || (Objects.equals(endpoint.getIp(), original.getIp())
                && Objects.equals(endpoint.getIaasId(), original.getIaasId())
                && Objects.equals(endpoint.getPurpose(), original.getPurpose())
                && Objects.equals(endpoint.getStatus(), original.getStatus()))) {
            return;
        }

        endpointDAO.update(convertToPO(endpoint));
    }

    private void addEndpoint(Endpoint endpoint) {

        endpointDAO.add(convertToPO(endpoint));
    }


    private EndpointPO convertToPO(Endpoint endpoint) {
        EndpointPO po = new EndpointPO();
        po.setId(endpoint.getId());
        po.setIp(endpoint.getIp());
        po.setIaasId(endpoint.getIaasId());
        po.setPurpose(endpoint.getPurpose().name());
        po.setStatus(endpoint.getStatus().name());

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
