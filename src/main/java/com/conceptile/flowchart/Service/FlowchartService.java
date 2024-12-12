package com.conceptile.flowchart.Service;

import com.conceptile.flowchart.entity.Flowchart;
import com.conceptile.flowchart.model.FlowchartModel;

public interface FlowchartService {

  Flowchart getFlowchartById(Long id);

  FlowchartModel upsertFlowchart(FlowchartModel flowchartModel);

  FlowchartModel getFlowchart(Long id);

  Boolean deleteFlowchart(Long id);
}
