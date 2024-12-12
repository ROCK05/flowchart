package com.conceptile.flowchart.controller;

import com.conceptile.flowchart.Service.EdgeService;
import com.conceptile.flowchart.model.EdgeModel;
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
public class EdgeController {

  private final EdgeService edgeService;

  public EdgeController(EdgeService edgeService) {
    this.edgeService = edgeService;
  }

  @RequestMapping(value = "/flowchart/{id}/edges/delete", method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<Boolean> deleteFlowchart(@PathVariable("id") Long flowchartId,
      @RequestBody List<EdgeModel> edges) {
    log.info("Request came to delete edges: {} with flowchartId: {}", edges, flowchartId);
    return new ResponseEntity<>(edgeService.deleteEdges(flowchartId, edges), HttpStatus.OK);
  }

  @RequestMapping(value = "/flowchart/{id}/edges/add", method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<Boolean> addEdges(@PathVariable("id") Long flowchartId,
      @RequestBody List<EdgeModel> edges) {
    log.info("Request came to add edges: {} with flowchartId: {}", edges, flowchartId);
    return new ResponseEntity<>(edgeService.addEdges(flowchartId, edges), HttpStatus.OK);
  }
}
