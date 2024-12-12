package com.conceptile.flowchart.Service;

import com.conceptile.flowchart.entity.Node;
import com.conceptile.flowchart.model.NodeModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NodeService {

  Map<String, NodeModel> createNewNodes(Long flowchartId, List<NodeModel> nodes);

  List<NodeModel> getNodesForChart(Long flowchartId);

  void deleteNodesForFlowchart(Long flowchartId);

  Boolean deleteNodes(Long flowchartId, List<NodeModel> nodes);

  List<NodeModel> addNodes(Long flowchartId, List<NodeModel> nodes);

  Map<String, Node> getNodesForValues(Long flowchartIds, Set<String> values);
}
