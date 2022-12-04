package com.hewutao.db.dao.repository;

import com.hewutao.db.model.Instance;
import com.hewutao.db.model.Node;

import java.util.List;

public interface NodeRepository {
    List<Node> getAllNodeByInstance(Instance instance);

    void saveNode(Node node);

    void saveAllNodes(List<Node> nodes);
}
