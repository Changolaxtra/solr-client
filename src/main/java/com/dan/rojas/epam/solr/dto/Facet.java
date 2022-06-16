package com.dan.rojas.epam.solr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Setter
@Getter
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class Facet {
  private long valueCount;
  private String value;
  private Name field;
  private Name key;
}
