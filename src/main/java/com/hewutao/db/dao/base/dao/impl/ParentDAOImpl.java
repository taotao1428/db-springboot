package com.hewutao.db.dao.base.dao.impl;

import com.hewutao.db.dao.base.dao.ParentDAO;
import com.hewutao.db.dao.base.mapper.ParentPOMapper;
import com.hewutao.db.dao.base.model.ParentPO;
import com.hewutao.db.dao.base.model.ParentPOExample;
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
                .andParentTypeEqualTo("instance")
                .andChildTypeEqualTo("endpoint")
                .andStatusEqualTo("normal");

        List<ParentPO> parentPOS = parentPOMapper.selectByExample(example);

        return parentPOS.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getNodeIdsByInstanceId(String instanceId) {
        ParentPOExample example = new ParentPOExample();
        example.or().andParentIdEqualTo(instanceId)
                .andParentTypeEqualTo("instance")
                .andChildTypeEqualTo("node")
                .andStatusEqualTo("normal");

        List<ParentPO> parentPOS = parentPOMapper.selectByExample(example);

        return parentPOS.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
    }

    @Override
    public void removeEndpointInstanceParentShip(String endpointId, String instanceId) {
        ParentPOExample example = new ParentPOExample();
        example.or()
                .andChildIdEqualTo(endpointId)
                .andChildTypeEqualTo("endpoint")
                .andParentIdEqualTo(instanceId)
                .andParentTypeEqualTo("instance");

        parentPOMapper.deleteByExample(example);
    }

    @Override
    public void addEndpointInstanceParentShip(String endpointId, String instanceId) {
        ParentPO po = new ParentPO();
        po.setId(UUID.randomUUID().toString());
        po.setChildId(endpointId);
        po.setChildType("endpoint");
        po.setParentId(instanceId);
        po.setParentType("instance");
        po.setStatus("normal");

        parentPOMapper.insert(po);
    }

    @Override
    public void removeNodeInstanceParentShip(String nodeId, String instanceId) {
        ParentPOExample example = new ParentPOExample();
        example.or()
                .andChildIdEqualTo(nodeId)
                .andChildTypeEqualTo("node")
                .andParentIdEqualTo(instanceId)
                .andParentTypeEqualTo("instance");

        parentPOMapper.deleteByExample(example);
    }

    @Override
    public void addNodeInstanceParentShip(String nodeId, String instanceId) {
        ParentPO po = new ParentPO();
        po.setId(UUID.randomUUID().toString());
        po.setChildId(nodeId);
        po.setChildType("node");
        po.setParentId(instanceId);
        po.setParentType("instance");
        po.setStatus("normal");

        parentPOMapper.insert(po);
    }
}
