package com.hewutao.db.dao.base.dao.impl;

import com.hewutao.db.dao.base.dao.ParentDAO;
import com.hewutao.db.dao.base.mapper.ParentPOMapper;
import com.hewutao.db.dao.base.model.ParentPO;
import com.hewutao.db.dao.base.model.ParentPOExample;
import com.hewutao.db.dao.base.support.DbStatus;
import com.hewutao.db.dao.base.support.EntityType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ParentDAOImpl implements ParentDAO {
    private ParentPOMapper parentPOMapper;

    @Override
    public List<String> getEndpointIdsByInstanceId(String instanceId) {
        ParentPOExample example = new ParentPOExample();
        example.or().andParentIdEqualTo(instanceId)
                .andParentTypeEqualTo(EntityType.INSTANCE)
                .andChildTypeEqualTo(EntityType.ENDPOINT)
                .andStatusEqualTo(DbStatus.NORMAL);

        List<ParentPO> parentPOS = parentPOMapper.selectByExample(example);

        return parentPOS.stream()
                .map(p -> p.getChildId())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getNodeIdsByInstanceId(String instanceId) {
        ParentPOExample example = new ParentPOExample();
        example.or().andParentIdEqualTo(instanceId)
                .andParentTypeEqualTo(EntityType.INSTANCE)
                .andChildTypeEqualTo(EntityType.NODE)
                .andStatusEqualTo(DbStatus.NORMAL);

        List<ParentPO> parentPOS = parentPOMapper.selectByExample(example);

        return parentPOS.stream()
                .map(p -> p.getChildId())
                .collect(Collectors.toList());
    }

    @Override
    public void removeEndpointInstanceParentShip(String endpointId, String instanceId) {
        ParentPOExample example = new ParentPOExample();
        example.or()
                .andChildIdEqualTo(endpointId)
                .andChildTypeEqualTo(EntityType.ENDPOINT)
                .andParentIdEqualTo(instanceId)
                .andParentTypeEqualTo(EntityType.INSTANCE);

        parentPOMapper.deleteByExample(example);
    }

    @Override
    public void addEndpointInstanceParentShip(String endpointId, String instanceId) {
        ParentPO po = new ParentPO();
        po.setId(UUID.randomUUID().toString());
        po.setChildId(endpointId);
        po.setChildType(EntityType.ENDPOINT);
        po.setParentId(instanceId);
        po.setParentType(EntityType.INSTANCE);
        po.setStatus(DbStatus.NORMAL);

        parentPOMapper.insert(po);
    }

    @Override
    public void removeNodeInstanceParentShip(String nodeId, String instanceId) {
        ParentPOExample example = new ParentPOExample();
        example.or()
                .andChildIdEqualTo(nodeId)
                .andChildTypeEqualTo(EntityType.NODE)
                .andParentIdEqualTo(instanceId)
                .andParentTypeEqualTo(EntityType.INSTANCE);

        parentPOMapper.deleteByExample(example);
    }

    @Override
    public void addNodeInstanceParentShip(String nodeId, String instanceId) {
        ParentPO po = new ParentPO();
        po.setId(UUID.randomUUID().toString());
        po.setChildId(nodeId);
        po.setChildType(EntityType.NODE);
        po.setParentId(instanceId);
        po.setParentType(EntityType.INSTANCE);
        po.setStatus(DbStatus.NORMAL);

        parentPOMapper.insert(po);
    }
}
