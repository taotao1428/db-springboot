package com.hewutao.db.dao.base.dao.impl;

import com.hewutao.db.dao.base.dao.NodeDAO;
import com.hewutao.db.dao.base.mapper.NodePOMapper;
import com.hewutao.db.dao.base.model.NodePO;
import com.hewutao.db.dao.base.model.NodePOExample;
import com.hewutao.db.dao.base.support.DbStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class NodeDAOImpl implements NodeDAO {
    private NodePOMapper nodePOMapper;

    @Override
    public List<NodePO> getAllUndeletedNodeByIds(List<String> nodeIds) {
        NodePOExample example = new NodePOExample();
        example.or()
                .andIdIn(nodeIds)
                .andStatusNotEqualTo(DbStatus.DELETED);

        return nodePOMapper.selectByExample(example);
    }

    @Override
    public void add(NodePO node) {
        nodePOMapper.insert(node);
    }

    @Override
    public void update(NodePO node) {
        nodePOMapper.updateByPrimaryKey(node);
    }
}
