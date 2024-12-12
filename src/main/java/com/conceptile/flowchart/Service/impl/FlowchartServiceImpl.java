package com.conceptile.flowchart.Service.impl;

import com.conceptile.flowchart.Service.EdgeService;
import com.conceptile.flowchart.Service.FlowchartService;
import com.conceptile.flowchart.Service.NodeService;
import com.conceptile.flowchart.common.FlowchartException;
import com.conceptile.flowchart.entity.Flowchart;
import com.conceptile.flowchart.model.EdgeModel;
import com.conceptile.flowchart.model.FlowchartModel;
import com.conceptile.flowchart.model.NodeModel;
import com.conceptile.flowchart.repository.FlowchartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.conceptile.flowchart.common.Constants.formatter;

@Slf4j
@Service("FlowchartServiceImpl")
public class FlowchartServiceImpl implements FlowchartService {

  private final FlowchartRepository flowchartRepository;

  public FlowchartServiceImpl(FlowchartRepository flowchartRepository) {
    this.flowchartRepository = flowchartRepository;
  }

  @Autowired
  @Qualifier("NodeServiceImpl")
  private NodeService nodeService;

  @Autowired
  @Qualifier("EdgeServiceImpl")
  private EdgeService edgeService;


  @Override
  @Transactional
  public FlowchartModel upsertFlowchart(FlowchartModel flowchartModel) {
    log.info("Upserting flowchart for request: {}", flowchartModel);

    if (Objects.isNull(flowchartModel.getId())) {
      flowchartModel = createFlowchart(flowchartModel);
    } else {
      flowchartModel = updateFlowchart(flowchartModel);
    }
    return flowchartModel;
  }

  @Transactional
  public FlowchartModel createFlowchart(FlowchartModel flowchartModel) {
    log.info("Creating flowchart for request: {}", flowchartModel);

    Flowchart flowchart = Flowchart.builder().name(flowchartModel.getName()).build();
    flowchart = flowchartRepository.save(flowchart);

    createNodesAndEdges(flowchart, flowchartModel);
    return FlowchartModel.builder().id(flowchart.getId()).name(flowchart.getName()).build();
  }

  @Transactional
  public FlowchartModel updateFlowchart(FlowchartModel flowchartModel) {
    Flowchart flowchart = getFlowchartById(flowchartModel.getId());
    //As ui is not present, deleting existing nodes and edges for simplicity, ideal case should
    // be to update data using ids
    nodeService.deleteNodesForFlowchart(flowchart.getId());
    edgeService.deleteEdgesForFlowchart(flowchart.getId());

    createNodesAndEdges(flowchart, flowchartModel);
    return FlowchartModel.builder().id(flowchart.getId()).name(flowchart.getName()).build();
  }

  private void createNodesAndEdges(Flowchart flowchart, FlowchartModel flowchartModel) {
    Map<String, NodeModel> nodeModelMap =
        nodeService.createNewNodes(flowchart.getId(), flowchartModel.getNodes());

    edgeService.createEdges(flowchart.getId(), nodeModelMap, flowchartModel.getEdges());
  }

  @Override
  public FlowchartModel getFlowchart(Long id) {
    log.info("Getting flow chart with id: {}", id);

    Flowchart flowchart = getFlowchartById(id);
    FlowchartModel flowchartModel =
        FlowchartModel.builder().id(flowchart.getId()).name(flowchart.getName()).build();
    List<NodeModel> nodes = nodeService.getNodesForChart(id);

    if (!CollectionUtils.isEmpty(nodes)) {
      flowchartModel = flowchartModel.toBuilder().nodes(nodes).build();
      Map<Long, NodeModel> nodeModelMap =
          nodes.stream().collect(Collectors.toMap(NodeModel::getId, node -> node));

      List<EdgeModel> edges = edgeService.getEdges(nodeModelMap, id);

      if (!CollectionUtils.isEmpty(edges)) {
        flowchartModel = flowchartModel.toBuilder().edges(edges).build();
      }
    }
    return flowchartModel;
  }

  @Override
  public Flowchart getFlowchartById(Long id) {
    Flowchart flowchart = flowchartRepository.findByIdAndIsActiveTrue(id);

    if (Objects.isNull(flowchart)) {
      throw new FlowchartException("Flowchart not found!");
    }
    return flowchart;
  }

  @Override
  public Boolean deleteFlowchart(Long id) {
    log.info("Deleting flowchart with id: {}", id);
    Flowchart flowchart = getFlowchartById(id);
    flowchart = flowchart.toBuilder().isActive(Boolean.FALSE)
        .updatedOn(Timestamp.valueOf(formatter.format(Instant.now()))).build();

    flowchartRepository.save(flowchart);
    return Boolean.TRUE;
  }
}
