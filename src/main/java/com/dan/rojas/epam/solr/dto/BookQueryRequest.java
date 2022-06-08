package com.dan.rojas.epam.solr.dto;

import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
public class BookQueryRequest {
  String field; // field for filtering
  String value; // value of the field for filtering
  String facetField; // field for facet
  boolean fulltext; // is full text search
  String q; // query for full text search
}
