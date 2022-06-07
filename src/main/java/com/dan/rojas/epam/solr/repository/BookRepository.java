package com.dan.rojas.epam.solr.repository;

import com.dan.rojas.epam.solr.document.BookDocument;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends SolrCrudRepository<BookDocument, String> {
}
