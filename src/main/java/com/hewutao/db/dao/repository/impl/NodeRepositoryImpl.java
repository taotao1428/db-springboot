package com.hewutao.db.dao.repository.impl;

import com.hewutao.db.dao.base.dao.NodeDAO;
import com.hewutao.db.dao.base.dao.ParentDAO;
import com.hewutao.db.dao.base.model.NodePO;
import com.hewutao.db.dao.repository.NodeRepository;
import com.hewutao.db.model.EntityStatus;
import com.hewutao.db.model.Instance;
import com.hewutao.db.model.Node;
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
public class NodeRepositoryImpl implements NodeRepository {
    private ApplicationEventPublisher publisher;
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
                        po
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveNode(Node node) {
        if (node.isDeleted() && !node.isExisted()) {
            log.info("node [{}] is deleted and is not existed, dont need save", node.getId());
            return;
        }

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

    private void publishEventForRollback(Node node, NodePO current) {
        node.innerPrepareForSave(current);
        log.info("node [{}] is updated or added, publish event", node.getId());
        publisher.publishEvent(new SaveEntityEvent(node));
    }

    public void updateNode(Node node) {
        NodePO original = (NodePO) node.innerGetOriginal();
        NodePO current = convertToPo(node);

        if (Objects.equals(current.getName(), original.getName())
                && Objects.equals(current.getStatus(), original.getStatus())) {
            return;
        }

        nodeDAO.update(current);

        publishEventForRollback(node, current);
    }

    public void addNode(Node node) {
        NodePO current = convertToPo(node);
        nodeDAO.add(current);
        publishEventForRollback(node, current);
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
