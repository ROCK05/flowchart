package com.conceptile.flowchart.repository;

import com.conceptile.flowchart.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository("NodeRepository")
public interface NodeRepository extends JpaRepository<Node, Long> {

  List<Node> findByFlowchartIdOrderById(Long flowchartId);

  Integer deleteByFlowchartId(Long flowchartId);

  List<Node> findByFlowchartIdAndValueIn(Long flowchartId, Set<String> values);
}
