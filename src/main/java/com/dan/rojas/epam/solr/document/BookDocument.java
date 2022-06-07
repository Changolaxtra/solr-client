package com.dan.rojas.epam.solr.document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.List;

@Value
@Builder
@EqualsAndHashCode(of = {"id"})
@ToString
@SolrDocument(collection = "books")
public class BookDocument {
  @Id
  @Indexed(name = "id", type = "string")
  String id;
  @Indexed(name = "title", type = "string")
  String title;
  List<String> authors;
  String language;
  String content;
}
