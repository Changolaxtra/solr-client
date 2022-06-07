package com.dan.rojas.epam.solr.controller;

import com.dan.rojas.epam.solr.service.BookIndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/indexing")
@RequiredArgsConstructor
public class BookIndexingController {

  private final BookIndexingService bookIndexingService;

  @GetMapping("/start")
  public void indexBooks() {
    bookIndexingService.processFiles();
  }
}