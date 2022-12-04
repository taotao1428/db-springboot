package com.hewutao.db.dao.repository;

import com.hewutao.db.model2.Instance;
import com.hewutao.db.model2.Node;

import java.util.List;

public interface NodeRepository {
    List<Node> getAllNodeByInstance(Instance instance);

    void saveNode(Node node);

    void saveAllNodes(List<Node> nodes);
}
