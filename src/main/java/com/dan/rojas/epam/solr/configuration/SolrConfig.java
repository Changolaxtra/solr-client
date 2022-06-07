package com.dan.rojas.epam.solr.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.CoreStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

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
    final CoreStatus coreStatus = CoreAdminRequest.getCoreStatus(coreName, solrClient);
    log.info("CoreStatus: {}", coreStatus);
    if (StringUtils.isEmpty(coreStatus.getInstanceDirectory())) {
      log.info("Initializing collection in directory {}...", coreDirectory);
      CoreAdminRequest.createCore(coreName, coreDirectory, solrClient);
    }
  }

  @Bean
  public SolrTemplate solrTemplate() {
    return new SolrTemplate(solrClient);
  }
}
