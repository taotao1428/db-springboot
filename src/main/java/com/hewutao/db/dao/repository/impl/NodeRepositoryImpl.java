package com.hewutao.db.dao.repository.impl;

import com.hewutao.db.dao.base.dao.NodeDAO;
import com.hewutao.db.dao.base.dao.ParentDAO;
import com.hewutao.db.dao.base.model.NodePO;
import com.hewutao.db.dao.repository.NodeRepository;
import com.hewutao.db.model.EntityStatus;
import com.hewutao.db.model.Instance;
import com.hewutao.db.model.Node;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class NodeRepositoryImpl implements NodeRepository {
    private ParentDAO parentDAO;
    private NodeDAO nodeDAO;

    @Override
    public List<Node> getAllNodeByInstance(Instance instance) {
        List<String> nodeIds = parentDAO.getNodeIdsByInstanceId(instance.getId());
        List<NodePO> nodePOS = nodeDAO.getAllUndeletedNodeByIds(nodeIds);

        return nodePOS.stream()
                .map(po -> new Node(
                        po.getId(),
                        po.getName(),
                        EntityStatus.from(po.getStatus()),
                        instance,
                        true
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveNode(Node node) {
        Instance instance = node.getInstance();
        if (node.isDeleted()) {
            if (node.isExisted()) {
                parentDAO.removeNodeInstanceParentShip(node.getId(), instance.getId());
                updateNode(node);
            }
        } else {
            if (node.isExisted()) {
                updateNode(node);
            } else {
                parentDAO.addNodeInstanceParentShip(node.getId(), instance.getId());
                addNode(node);
            }
        }
    }

    public void updateNode(Node node) {
        Node original = node.getOriginal();

        if (original == null || (Objects.equals(node.getName(), original.getName())
                && Objects.equals(node.getStatus(), original.getStatus()))) {
            return;
        }

        nodeDAO.add(convertToPo(node));
    }

    public void addNode(Node node) {
        nodeDAO.update(convertToPo(node));
    }

    private NodePO convertToPo(Node node) {
        NodePO po = new NodePO();
        po.setId(node.getId());
        po.setName(node.getName());
        po.setStatus(node.getStatus().name());

        return po;
    }

    @Transactional
    @Override
    public void saveAllNodes(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }

        for (Node node : nodes) {
            saveNode(node);
        }
    }
}
