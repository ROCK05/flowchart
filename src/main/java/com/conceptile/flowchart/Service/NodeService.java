package com.conceptile.flowchart.Service;

import com.conceptile.flowchart.model.NodeModel;

import java.util.List;
import java.util.Map;

public interface NodeService {

  Map<String, NodeModel> createNewNodes(Long flowchartId, List<NodeModel> nodes);
}
