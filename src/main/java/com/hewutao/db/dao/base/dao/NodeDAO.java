package com.hewutao.db.dao.base.dao;

import com.hewutao.db.dao.base.model.NodePO;

import java.util.List;

public interface NodeDAO {
    List<NodePO> getAllUndeletedNodeByIds(List<String> nodeIds);

    void add(NodePO node);

    void update(NodePO node);
}
