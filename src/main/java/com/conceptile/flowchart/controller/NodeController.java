package com.conceptile.flowchart.controller;

import com.conceptile.flowchart.Service.NodeService;
import com.conceptile.flowchart.model.EdgeModel;
import com.conceptile.flowchart.model.NodeModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
public class NodeController {

  private final NodeService nodeService;

  public NodeController(NodeService nodeService) {
    this.nodeService = nodeService;
  }

  @RequestMapping(value = "/flowchart/{id}/nodes/delete", method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<Boolean> deleteNodes(@PathVariable("id") Long flowchartId,
      @RequestBody List<NodeModel> nodes) {
    log.info("Request came to delete nodes: {} with flowchartId: {}", nodes, flowchartId);
    return new ResponseEntity<>(nodeService.deleteNodes(flowchartId, nodes), HttpStatus.OK);
  }

  @RequestMapping(value = "/flowchart/{id}/nodes/add", method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<List<NodeModel>> addNodes(@PathVariable("id") Long flowchartId,
      @RequestBody List<NodeModel> nodes) {
    log.info("Request came to add nodes: {} with flowchartId: {}", nodes, flowchartId);
    return new ResponseEntity<>(nodeService.addNodes(flowchartId, nodes), HttpStatus.OK);
  }

  @RequestMapping(value = "/flowchart/{id}/target-nodes", method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<List<EdgeModel>> getOutgoingEdges(@PathVariable("id") Long flowchartId,
      @RequestBody NodeModel node) {
    log.info("Request came to  get outgoing edges for node: {} with flowchartId: {}", node,
        flowchartId);
    return new ResponseEntity<>(nodeService.getAllTargetEdges(flowchartId, node), HttpStatus.OK);
  }
}
