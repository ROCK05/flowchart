package com.conceptile.flowchart.repository;

import com.conceptile.flowchart.entity.Flowchart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("FlowchartRepository")
public interface FlowchartRepository extends JpaRepository<Flowchart, Long> {

}
