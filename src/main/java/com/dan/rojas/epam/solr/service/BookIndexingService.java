package com.dan.rojas.epam.solr.service;

import com.dan.rojas.epam.solr.configuration.BookIndexingConfig;
import com.dan.rojas.epam.solr.document.BookDocument;
import com.dan.rojas.epam.solr.repository.BookSolrIndexingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookIndexingService {

  private final BookIndexingConfig bookIndexingConfig;
  private final BookMappingService bookMappingService;
  private final BookSolrIndexingRepository bookSolrIndexingRepository;

  public void processFiles() {
    final File directory = new File(bookIndexingConfig.getDirectory());
    Arrays.stream(Optional.of(directory)
            .map(File::listFiles)
            .orElse(new File[]{}))
        .map(this::mapToInputStream)
        .filter(Objects::nonNull)
        .forEach(this::index);
  }

  private FileInputStream mapToInputStream(final File file) {
    FileInputStream fileInputStream;
    try {
      fileInputStream = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      log.error("Error reading the file {} : {}", file.getAbsolutePath(), e);
      fileInputStream = null;
    }
    return fileInputStream;
  }

  private void index(final FileInputStream fileInputStream) {
    try {
      final EpubReader epubReader = new EpubReader();
      final Book epub = epubReader.readEpub(fileInputStream);
      final BookDocument bookDocument = bookMappingService.getBookDocument(epub);
      CompletableFuture.runAsync(() -> {
        bookSolrIndexingRepository.createIndex(bookDocument);
        log.info("Processed Book: {}", bookDocument.getTitle());
      });
    } catch (IOException e) {
      log.error("Error creating book for solr", e);
    }
  }

}
