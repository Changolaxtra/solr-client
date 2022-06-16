package com.dan.rojas.epam.solr.repository;

import com.dan.rojas.epam.solr.configuration.BookIndexingConfig;
import com.dan.rojas.epam.solr.document.BookDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookSolrIndexingRepository {

  private final SolrTemplate solrTemplate;
  private final BookIndexingConfig bookIndexingConfig;

  public void createIndex(final BookDocument bookDocument) {
    solrTemplate.saveBean(bookIndexingConfig.getCollection(), bookDocument);
  }

}
