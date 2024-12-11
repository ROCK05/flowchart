package com.conceptile.flowchart.Service.impl;

import com.conceptile.flowchart.Service.EdgeService;
import com.conceptile.flowchart.Service.FlowchartService;
import com.conceptile.flowchart.Service.NodeService;
import com.conceptile.flowchart.entity.Flowchart;
import com.conceptile.flowchart.model.FlowchartModel;
import com.conceptile.flowchart.model.NodeModel;
import com.conceptile.flowchart.repository.FlowchartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service("FlowchartImpl")
public class FlowchartServiceImpl implements FlowchartService {

  private final FlowchartRepository flowchartRepository;
  private final NodeService nodeService;
  private final EdgeService edgeService;

  public FlowchartServiceImpl(FlowchartRepository flowchartRepository, NodeService nodeService,
      EdgeService edgeService) {
    this.flowchartRepository = flowchartRepository;
    this.nodeService = nodeService;
    this.edgeService = edgeService;
  }

  @Override
  @Transactional
  public FlowchartModel createFlowchart(FlowchartModel flowchartModel) {
    log.info("Creating flowchart for request: {}", flowchartModel);

    Flowchart flowchart = Flowchart.builder().name(flowchartModel.getName()).build();
    flowchart = flowchartRepository.save(flowchart);

    Map<String, NodeModel> nodeModelMap =
        nodeService.createNewNodes(flowchart.getId(), flowchartModel.getNodes());

    edgeService.createEdges(nodeModelMap, flowchartModel.getEdges());
    return FlowchartModel.builder().id(flowchart.getId()).name(flowchart.getName()).build();
  }

}
