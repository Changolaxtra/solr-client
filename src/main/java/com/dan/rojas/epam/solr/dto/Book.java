package com.dan.rojas.epam.solr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Setter
@Getter
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class Book {
  private String id;
  private String title;
  private List<String> authors;
  private String language;
  private String content;
}

