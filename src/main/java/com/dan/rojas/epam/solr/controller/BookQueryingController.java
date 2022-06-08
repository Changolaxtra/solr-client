package com.dan.rojas.epam.solr.controller;

import com.dan.rojas.epam.solr.dto.BookQueryRequest;
import com.dan.rojas.epam.solr.dto.BookQueryResponse;
import com.dan.rojas.epam.solr.repository.BookSolrQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookQueryingController {

  private final BookSolrQueryRepository bookSolrQueryRepository;

  @PostMapping("/query")
  public BookQueryResponse query(@RequestBody final BookQueryRequest bookQueryRequest) {
    return bookSolrQueryRepository.executeQuery(bookQueryRequest);
  }
}
