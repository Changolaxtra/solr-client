package com.dan.rojas.epam.solr.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class BookQueryResponse {
  List<Book> books;
  List<Facet> facets;
  long numFound;
}
