package com.conceptile.flowchart.entity;

import com.conceptile.flowchart.enums.NodeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "NODE")
public class Node {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "FLOWCHART_ID")
  private Long flowchartId;

  @Column(name = "NODE_VALUE")
  private String value;

  @Column(name = "TYPE")
  @Enumerated(EnumType.STRING)
  private NodeType type;

  @Column(name = "CREATED_ON")
  @Builder.Default
  private Timestamp createdOn = Timestamp.valueOf(formatter.format(Instant.now()));

  @Column(name = "UPDATED_ON")
  private Timestamp updatedOn;
}
