package com.conceptile.flowchart.model;

import com.conceptile.flowchart.enums.NodeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeModel {
  private Long id;
  private String value;
  @Builder.Default
  private NodeType nodeType = NodeType.CIRCLE;
}
