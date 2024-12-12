package com.conceptile.flowchart.repository;

import com.conceptile.flowchart.entity.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, Long> {

  List<Edge> findByFlowchartIdOrderById(Long flowchartId);

  Integer deleteByFlowchartId(Long flowchartId);

  Edge findByFlowchartIdAndSourceNodeIdAndTargetNodeId(Long flowchartId, Long sourceId,
      Long targetId);

  Integer deleteByFlowchartIdAndSourceNodeIdInOrTargetNodeIdIn(Long flowchartId,
      List<Long> sourceNodeIds, List<Long> targetNodeIds);
}
