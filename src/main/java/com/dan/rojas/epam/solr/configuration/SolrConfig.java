package com.dan.rojas.epam.solr.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SolrConfig {

  private final SolrClient solrClient;
  private final BookIndexingConfig bookIndexingConfig;

  @PostConstruct
  public void initializeCollection() throws IOException, SolrServerException {
    final String coreName = bookIndexingConfig.getCollection();
    final String coreDirectory = bookIndexingConfig.getCoreDirectory();
    log.info("Initializing collection in directory {}...", coreDirectory);
    CoreAdminRequest.createCore(coreName, coreDirectory, solrClient);
  }
}
