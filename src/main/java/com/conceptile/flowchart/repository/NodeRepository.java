package com.conceptile.flowchart.repository;

import com.conceptile.flowchart.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("NodeRepository")
public interface NodeRepository extends JpaRepository<Node, Long> {

}
