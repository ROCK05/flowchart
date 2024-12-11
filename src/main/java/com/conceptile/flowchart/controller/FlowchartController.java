package com.conceptile.flowchart.controller;

import com.conceptile.flowchart.Service.FlowchartService;
import com.conceptile.flowchart.model.FlowchartModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class FlowchartController {

  private final FlowchartService flowchartService;

  public FlowchartController(FlowchartService flowchartService) {
    this.flowchartService = flowchartService;
  }

  @RequestMapping(value = "/create", method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<FlowchartModel> createFlowchart(
      @RequestBody FlowchartModel flowchartModel) {
    log.info("Request came to crate flowchart with request: {}", flowchartModel);
    return new ResponseEntity<>(flowchartService.createFlowchart(flowchartModel), HttpStatus.OK);
  }

}
