package com.conceptile.flowchart.Service.impl;

import com.conceptile.flowchart.Service.EdgeService;
import com.conceptile.flowchart.entity.Edge;
import com.conceptile.flowchart.model.EdgeModel;
import com.conceptile.flowchart.model.NodeModel;
import com.conceptile.flowchart.repository.EdgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("EdgeServiceImpl")
public class EdgeServiceImpl implements EdgeService {

  private final EdgeRepository edgeRepository;

  public EdgeServiceImpl(EdgeRepository edgeRepository) {
    this.edgeRepository = edgeRepository;
  }

  @Override
  @Transactional
  public void createEdges(Map<String, NodeModel> nodeModelMap, List<EdgeModel> edges) {
    List<Edge> edgesToSave = new ArrayList<>();
    for (EdgeModel<String> edge : edges) {
      edgesToSave.add(Edge.builder().sourceNodeId(nodeModelMap.get(edge.getSource()).getId())
          .targetNodeId(nodeModelMap.get(edge.getTarget()).getId()).build());
    }
    edgeRepository.saveAll(edgesToSave);
  }
}
