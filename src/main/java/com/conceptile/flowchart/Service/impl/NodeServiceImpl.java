package com.conceptile.flowchart.Service.impl;

import com.conceptile.flowchart.Service.NodeService;
import com.conceptile.flowchart.entity.Node;
import com.conceptile.flowchart.model.NodeModel;
import com.conceptile.flowchart.repository.NodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("NodeServiceImpl")
public class NodeServiceImpl implements NodeService {

  private final NodeRepository nodeRepository;

  public NodeServiceImpl(NodeRepository nodeRepository) {
    this.nodeRepository = nodeRepository;
  }

  @Override
  @Transactional
  public Map<String, NodeModel> createNewNodes(Long flowchartId, List<NodeModel> nodes) {
    log.info("Creating new nodes: {} for flowchart: {}", nodes, flowchartId);

    Map<String, NodeModel> valueToNodeMap = new HashMap<>();
    //Filtering duplicate nodes
    nodes.forEach(node -> valueToNodeMap.putIfAbsent(node.getValue(), node));
    List<Node> nodesToSave = new ArrayList<>();
    for (Map.Entry<String, NodeModel> entry : valueToNodeMap.entrySet()) {
      nodesToSave.add(Node.builder().value(entry.getKey()).flowchartId(flowchartId)
          .type(entry.getValue().getNodeType()).build());
    }
    nodesToSave = nodeRepository.saveAll(nodesToSave);

    nodesToSave.forEach(node -> valueToNodeMap.get(node.getValue()).setId(node.getId()));
    return valueToNodeMap;
  }

}
