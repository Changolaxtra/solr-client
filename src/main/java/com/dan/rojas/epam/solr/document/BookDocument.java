package com.dan.rojas.epam.solr.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SolrDocument(collection = "books")
public class BookDocument {
  @Id
  @Indexed(name = "id", type = "string")
  @EqualsAndHashCode.Include
  private String id;
  @Indexed(name = "title", type = "string")
  private String title;
  @Field
  private List<String> authors;
  @Field
  private String language;
  @Field
  private String content;
}
