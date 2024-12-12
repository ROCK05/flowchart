package com.conceptile.flowchart.Service;

import com.conceptile.flowchart.model.EdgeModel;
import com.conceptile.flowchart.model.NodeModel;

import java.util.List;
import java.util.Map;

public interface EdgeService {

  void createEdges(Long flowchartTd, Map<String, NodeModel> nodeModelMap, List<EdgeModel> edges);

  List<EdgeModel> getEdges(Map<Long, NodeModel> nodeModelMap, Long flowchartId);

  void deleteEdgesForFlowchart(Long flowchartId);

  Boolean deleteEdges(Long flowchartId, List<EdgeModel> edges);

  Boolean addEdges(Long flowchartId, List<EdgeModel> edges);

  void deleteEdgesForNodes(Long flowchartId, List<Long> nodeIds);
}
