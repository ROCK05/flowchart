package com.conceptile.flowchart.Service.impl;

import com.conceptile.flowchart.Service.EdgeService;
import com.conceptile.flowchart.Service.FlowchartService;
import com.conceptile.flowchart.Service.NodeService;
import com.conceptile.flowchart.common.FlowchartException;
import com.conceptile.flowchart.entity.Node;
import com.conceptile.flowchart.model.EdgeModel;
import com.conceptile.flowchart.model.NodeModel;
import com.conceptile.flowchart.repository.NodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service("NodeServiceImpl")
public class NodeServiceImpl implements NodeService {

  private final NodeRepository nodeRepository;
  private final EdgeService edgeService;

  public NodeServiceImpl(NodeRepository nodeRepository, EdgeService edgeService) {
    this.nodeRepository = nodeRepository;
    this.edgeService = edgeService;
  }

  @Autowired
  @Qualifier("FlowchartServiceImpl")
  private FlowchartService flowchartService;

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

  @Override
  public List<NodeModel> getNodesForChart(Long flowchartId) {
    List<Node> nodes = nodeRepository.findByFlowchartIdOrderById(flowchartId);
    List<NodeModel> nodeModels = new ArrayList<>();
    if (!CollectionUtils.isEmpty(nodes)) {
      nodes.forEach(node -> {
        nodeModels.add(NodeModel.builder().id(node.getId()).value(node.getValue()).build());
      });
    }
    return nodeModels;
  }

  @Override
  @Transactional
  public void deleteNodesForFlowchart(Long flowchartId) {
    nodeRepository.deleteByFlowchartId(flowchartId);
  }

  @Override
  @Transactional
  public Boolean deleteNodes(Long flowchartId, List<NodeModel> nodes) {
    log.info("Deleting nodes: {} for flowchart: {}", nodes, flowchartId);
    //Validate flowchart
    flowchartService.getFlowchartById(flowchartId);
    if (!CollectionUtils.isEmpty(nodes)) {
      Set<String> values = nodes.stream().map(NodeModel::getValue).collect(Collectors.toSet());
      List<Node> nodesInDb = nodeRepository.findByFlowchartIdAndValueIn(flowchartId, values);
      List<Long> ids = new ArrayList<>();
      if (!CollectionUtils.isEmpty(nodesInDb)) {
        nodesInDb.forEach(node -> ids.add(node.getId()));
      }
      nodeRepository.deleteAllInBatch(nodesInDb);
      edgeService.deleteEdgesForNodes(flowchartId, new ArrayList<>(ids));
    }
    return Boolean.TRUE;
  }

  @Override
  public List<NodeModel> addNodes(Long flowchartId, List<NodeModel> nodes) {
    log.info("Adding nodes: {} for flowchart: {}", nodes, flowchartId);
    //Validate flowchart
    flowchartService.getFlowchartById(flowchartId);

    Map<String, NodeModel> valueToNodeMap = new HashMap<>();
    //Filtering duplicate nodes
    nodes.forEach(node -> valueToNodeMap.putIfAbsent(node.getValue(), node));
    Map<String, Node> nodesInDb = getNodesForValues(flowchartId, valueToNodeMap.keySet());

    List<Node> nodesToSave = new ArrayList<>();
    for (Map.Entry<String, NodeModel> entry : valueToNodeMap.entrySet()) {
      if (!nodesInDb.containsKey(entry.getKey())) {
        nodesToSave.add(Node.builder().value(entry.getKey()).flowchartId(flowchartId)
            .type(entry.getValue().getNodeType()).build());
      } else {
        entry.getValue().setId(nodesInDb.get(entry.getKey()).getId());
      }
    }
    if (!CollectionUtils.isEmpty(nodesToSave)) {
      nodesToSave = nodeRepository.saveAll(nodesToSave);
    }
    nodesToSave.forEach(node -> valueToNodeMap.get(node.getValue()).setId(node.getId()));

    return new ArrayList<>(valueToNodeMap.values());
  }

  @Override
  public Map<String, Node> getNodesForValues(Long flowchartIds, Set<String> values) {
    List<Node> nodes = nodeRepository.findByFlowchartIdAndValueIn(flowchartIds, values);
    Map<String, Node> valueToNodeMap = new HashMap<>();
    if (!CollectionUtils.isEmpty(nodes)) {
      nodes.forEach(node -> valueToNodeMap.putIfAbsent(node.getValue(), node));
    }
    return valueToNodeMap;
  }

  @Override
  public List<EdgeModel> getAllTargetEdges(Long flowchartId, NodeModel nodeModel) {
    log.info("Getting all out going edge for node: {} with flowchartId: {}", nodeModel,
        flowchartId);

    List<Node> node =
        nodeRepository.findByFlowchartIdAndValueIn(flowchartId, Set.of(nodeModel.getValue()));
    if (CollectionUtils.isEmpty(node)) {
      throw new FlowchartException("Node does not exist!");
    }

    List<Long> targetNodeIds = new ArrayList<>();
    List<EdgeModel> edges = edgeService.getOutgoingEdges(flowchartId, node.get(0), targetNodeIds);
    if (!CollectionUtils.isEmpty(targetNodeIds)) {
    List<Node> targetNodes = nodeRepository.findByFlowchartIdAndIdIn(flowchartId, targetNodeIds);
    Map<Long, Node> nodeModelMap =
        targetNodes.stream().collect(Collectors.toMap(Node::getId, Function.identity()));

    edges.forEach(edge -> edge.setTarget(nodeModelMap.get(edge.getTargetId()).getValue()));
    }
    return edges;
  }
}
