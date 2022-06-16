package com.dan.rojas.epam.solr.service;

import com.dan.rojas.epam.solr.document.BookDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookMappingService {

  private static final String AUTHOR_TEMPLATE = "%s %s";

  public BookDocument getBookDocument(final Book epub) {
    final BookDocument.BookDocumentBuilder builder = BookDocument.builder();
    mapId(builder, epub);
    mapTitle(builder, epub);
    mapLanguage(builder, epub);
    mapAuthors(builder, epub);
    mapContent(builder, epub);
    return builder.build();
  }

  private void mapId(final BookDocument.BookDocumentBuilder builder, final Book epub) {
    final String title = Optional.ofNullable(epub).map(Book::getTitle).orElse("");
    final byte[] md5Digest = DigestUtils.md5Digest((title + String.join("", getAuthors(epub)))
        .getBytes(StandardCharsets.UTF_8));
    final String hexId = new String(Hex.encodeHex(md5Digest));
    builder.id(hexId);
    log.info("Id {} generated for {}", hexId, title);
  }

  private void mapTitle(final BookDocument.BookDocumentBuilder builder, final Book epub) {
    final String title = Optional.ofNullable(epub)
        .map(Book::getMetadata)
        .map(Metadata::getFirstTitle)
        .orElse(null);
    builder.title(title);
  }

  private void mapLanguage(final BookDocument.BookDocumentBuilder builder, final Book epub) {
    final String language = Optional.ofNullable(epub)
        .map(Book::getMetadata)
        .map(Metadata::getLanguage)
        .orElse(null);
    builder.language(language);
  }

  private void mapAuthors(final BookDocument.BookDocumentBuilder builder, final Book epub) {
    final List<String> authors = getAuthors(epub);
    builder.authors(authors);
  }

  private void mapContent(final BookDocument.BookDocumentBuilder builder, final Book epub) {
    final String content = Optional.ofNullable(epub)
        .map(Book::getContents)
        .orElse(new ArrayList<>())
        .stream()
        .map(this::getContent)
        .collect(Collectors.joining(" "));
    builder.content(content);
  }

  private String getContent(final Resource resource) {
    String content;
    try {
      content = new String(resource.getData(), StandardCharsets.UTF_8).replaceAll("<[^>]*>", "");
    } catch (IOException e) {
      log.error("Error getting the data content", e);
      content = "";
    }
    return content;
  }

  private List<String> getAuthors(final Book epub) {
    return Optional.ofNullable(epub)
        .map(Book::getMetadata)
        .map(Metadata::getAuthors)
        .orElse(new ArrayList<>())
        .stream()
        .map(author -> String.format(AUTHOR_TEMPLATE, author.getFirstname(), author.getLastname()))
        .collect(Collectors.toList());
  }


}
