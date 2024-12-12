package com.conceptile.flowchart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

import static com.conceptile.flowchart.common.Constants.formatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "EDGE")
public class Edge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "FLOWCHART_ID")
  private Long flowchartId;

  @Column(name = "SOURCE_NODE_ID")
  private Long sourceNodeId;

  @Column(name = "TARGET_NODE_ID")
  private Long targetNodeId;

  @Column(name = "CREATED_ON")
  @Builder.Default
  private Timestamp createdOn = Timestamp.valueOf(formatter.format(Instant.now()));

}
