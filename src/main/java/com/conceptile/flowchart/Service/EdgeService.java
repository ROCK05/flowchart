package com.conceptile.flowchart.Service;

import com.conceptile.flowchart.model.EdgeModel;
import com.conceptile.flowchart.model.NodeModel;

import java.util.List;
import java.util.Map;

public interface EdgeService {

  void createEdges(Map<String, NodeModel> nodeModelMap, List<EdgeModel> edges);

}
