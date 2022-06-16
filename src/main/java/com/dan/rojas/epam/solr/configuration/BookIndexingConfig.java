package com.dan.rojas.epam.solr.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class BookIndexingConfig {

  @Value("${books.core.directory}")
  private String coreDirectory;

  @Value("${book.directory}")
  private String directory;

  @Value("${book.collection.name}")
  private String collection;
}
