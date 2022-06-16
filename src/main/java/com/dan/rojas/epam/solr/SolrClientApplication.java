package com.dan.rojas.epam.solr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@EnableSolrRepositories
@SpringBootApplication
public class SolrClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolrClientApplication.class, args);
	}

}
