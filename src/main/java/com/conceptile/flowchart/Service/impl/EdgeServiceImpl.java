package com.conceptile.flowchart.Service.impl;

import com.conceptile.flowchart.Service.EdgeService;
import com.conceptile.flowchart.Service.FlowchartService;
import com.conceptile.flowchart.Service.NodeService;
import com.conceptile.flowchart.common.FlowchartException;
import com.conceptile.flowchart.entity.Edge;
import com.conceptile.flowchart.entity.Node;
import com.conceptile.flowchart.model.EdgeModel;
import com.conceptile.flowchart.model.NodeModel;
import com.conceptile.flowchart.repository.EdgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service("EdgeServiceImpl")
public class EdgeServiceImpl implements EdgeService {

  @Autowired
  private final EdgeRepository edgeRepository;

  public EdgeServiceImpl(EdgeRepository edgeRepository) {
    this.edgeRepository = edgeRepository;
  }

  @Autowired
  @Qualifier("FlowchartServiceImpl")
  private FlowchartService flowchartService;

  @Autowired
  @Qualifier("NodeServiceImpl")
  private NodeService nodeService;

  @Override
  @Transactional
  public void createEdges(Long flowchartId, Map<String, NodeModel> nodeModelMap,
      List<EdgeModel> edges) {
    List<Edge> edgesToSave = new ArrayList<>();
    for (EdgeModel edge : edges) {
      if (nodeModelMap.containsKey(edge.getSource()) && nodeModelMap.containsKey(
          edge.getTarget())) {
        edgesToSave.add(Edge.builder().flowchartId(flowchartId)
            .sourceNodeId(nodeModelMap.get(edge.getSource()).getId())
            .targetNodeId(nodeModelMap.get(edge.getTarget()).getId()).build());
      }
    }
    edgeRepository.saveAll(edgesToSave);
  }

  @Override
  public List<EdgeModel> getEdges(Map<Long, NodeModel> nodeModelMap, Long flowchartId) {
    List<Edge> edges = edgeRepository.findByFlowchartIdOrderById(flowchartId);
    List<EdgeModel> edgeModels = new ArrayList<>();
    if (!CollectionUtils.isEmpty(edges)) {
      edges.forEach(edge -> edgeModels.add(EdgeModel.builder().id(edge.getId())
          .source(nodeModelMap.get(edge.getSourceNodeId()).getValue())
          .sourceId(edge.getSourceNodeId())
          .target(nodeModelMap.get(edge.getTargetNodeId()).getValue())
          .targetId(edge.getTargetNodeId()).build()));
    }
    return edgeModels;
  }

  @Override
  @Transactional
  public void deleteEdgesForFlowchart(Long flowchartId) {
    edgeRepository.deleteByFlowchartId(flowchartId);
  }

  @Override
  @Transactional
  public Boolean deleteEdges(Long flowchartId, List<EdgeModel> edges) {
    //Validate flowchart
    flowchartService.getFlowchartById(flowchartId);
    if (!CollectionUtils.isEmpty(edges)) {
      Map<String, Node> nodeMap = getNodesMap(flowchartId, edges);

      List<Edge> edgesToDelete = new ArrayList<>();
      for (EdgeModel edge : edges) {
        validateNodes(edge, nodeMap);
        Edge edgeInDb = edgeRepository.findByFlowchartIdAndSourceNodeIdAndTargetNodeId(flowchartId,
            nodeMap.get(edge.getSource()).getId(), nodeMap.get(edge.getTarget()).getId());
        if (Objects.isNull(edgeInDb)) {
          throw new FlowchartException("Edge does not exist!");
        }
        edgesToDelete.add(edgeInDb);
      }
      edgeRepository.deleteAllInBatch(edgesToDelete);
    }
    return Boolean.TRUE;
  }

  @Override
  @Transactional
  public Boolean addEdges(Long flowchartId, List<EdgeModel> edges) {
    //Validate flowchart
    flowchartService.getFlowchartById(flowchartId);
    Map<String, Node> nodeMap = getNodesMap(flowchartId, edges);
    List<Edge> edgesToSave = new ArrayList<>();
    edges.forEach(edge -> {
      validateNodes(edge, nodeMap);
      Edge edgeInDb = edgeRepository.findByFlowchartIdAndSourceNodeIdAndTargetNodeId(flowchartId,
          nodeMap.get(edge.getSource()).getId(), nodeMap.get(edge.getTarget()).getId());

      if (Objects.isNull(edgeInDb)) {
        edgesToSave.add(Edge.builder().flowchartId(flowchartId)
            .sourceNodeId(nodeMap.get(edge.getSource()).getId())
            .targetNodeId(nodeMap.get(edge.getTarget()).getId()).build());
      }
    });
    edgeRepository.saveAll(edgesToSave);
    return Boolean.TRUE;
  }

  private void validateNodes(EdgeModel edge, Map<String, Node> nodeMap) {
    if (!nodeMap.containsKey(edge.getSource()) || !nodeMap.containsKey(edge.getTarget())) {
      throw new FlowchartException("Node does not exist!");
    }
  }

  @Override
  @Transactional
  public void deleteEdgesForNodes(Long flowchartId, List<Long> nodeIds) {
    edgeRepository.deleteByFlowchartIdAndSourceNodeIdInOrTargetNodeIdIn(flowchartId, nodeIds,
        nodeIds);
  }

  private Map<String, Node> getNodesMap(Long flowchartId, List<EdgeModel> edges) {
    Set<String> nodeValues = new HashSet<>();
    edges.forEach(edge -> {
      nodeValues.add(edge.getSource());
      nodeValues.add(edge.getTarget());
    });

    return nodeService.getNodesForValues(flowchartId, nodeValues);
  }

  @Override
  public List<EdgeModel> getOutgoingEdges(Long flowchartId, Node node, List<Long> targetNodeIds) {
    List<Edge> edgesInDb =
        edgeRepository.findByFlowchartIdAndSourceNodeId(flowchartId, node.getId());
    List<EdgeModel> edges = new ArrayList<>();
    if (!CollectionUtils.isEmpty(edgesInDb)) {
      edgesInDb.forEach(edge -> {
        edges.add(EdgeModel.builder().id(edge.getId()).sourceId(edge.getSourceNodeId())
            .source(node.getValue()).targetId(edge.getTargetNodeId()).build());
        targetNodeIds.add(edge.getTargetNodeId());
      });
    }
    return edges;
  }
}
