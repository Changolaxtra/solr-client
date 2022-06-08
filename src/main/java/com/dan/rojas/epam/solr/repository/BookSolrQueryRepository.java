package com.dan.rojas.epam.solr.repository;

import com.dan.rojas.epam.solr.configuration.BookIndexingConfig;
import com.dan.rojas.epam.solr.document.BookDocument;
import com.dan.rojas.epam.solr.dto.Book;
import com.dan.rojas.epam.solr.dto.BookQueryRequest;
import com.dan.rojas.epam.solr.dto.BookQueryResponse;
import com.dan.rojas.epam.solr.dto.Facet;
import com.dan.rojas.epam.solr.dto.Name;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Repository
public class BookSolrQueryRepository {

  private final SolrTemplate solrTemplate;
  private final BookIndexingConfig bookIndexingConfig;

  public BookQueryResponse executeQuery(final BookQueryRequest bookQueryRequest) {
    final FacetPage<BookDocument> bookDocumentFacetPage =
        solrTemplate.queryForFacetPage(bookIndexingConfig.getCollection(),
            createQuery(bookQueryRequest),
            BookDocument.class);


    final List<Book> bookList = getBooks(bookDocumentFacetPage);
    final List<Facet> facetList = getFacets(bookDocumentFacetPage, bookQueryRequest.getFacetField());

    final BookQueryResponse.BookQueryResponseBuilder responseBuilder = BookQueryResponse.builder();
    responseBuilder.books(bookList);
    responseBuilder.facets(facetList);
    responseBuilder.numFound(bookList.size());

    return responseBuilder.build();
  }

  private SimpleFacetQuery createQuery(final BookQueryRequest bookQueryRequest) {
    final SimpleFacetQuery simpleFacetQuery = new SimpleFacetQuery(new SimpleStringCriteria(bookQueryRequest.getQ()));
    simpleFacetQuery.setFacetOptions(new FacetOptions().addFacetOnField(bookQueryRequest.getFacetField()));
    final Criteria criteria = new Criteria(bookQueryRequest.getField());
    SimpleFilterQuery simpleFilterQuery;
    if (bookQueryRequest.isFulltext()) {
      simpleFilterQuery = new SimpleFilterQuery(criteria.contains(bookQueryRequest.getValue()));
    } else {
      simpleFilterQuery = new SimpleFilterQuery(criteria.fuzzy(bookQueryRequest.getValue()));
    }
    simpleFacetQuery.addFilterQuery(simpleFilterQuery);
    return simpleFacetQuery;
  }

  private List<Book> getBooks(final FacetPage<BookDocument> bookDocumentFacetPage) {
    final ModelMapper mapper = new ModelMapper();
    return Optional.of(bookDocumentFacetPage.getContent())
        .orElse(new ArrayList<>())
        .stream()
        .map(solrBook -> mapper.map(solrBook, Book.class))
        .collect(Collectors.toList());
  }

  private List<Facet> getFacets(final FacetPage<BookDocument> bookDocumentFacetPage, final String facetField) {
    return Optional.of(bookDocumentFacetPage)
        .map(facetPage -> facetPage.getFacetResultPage(facetField))
        .map(Page::getContent)
        .orElse(new ArrayList<>())
        .stream()
        .map(facetFieldEntry -> {
              final Facet.FacetBuilder facetBuilder = Facet.builder();
              facetBuilder.field(Name.builder().name(facetFieldEntry.getField().getName()).build());
              facetBuilder.key(Name.builder().name(facetFieldEntry.getKey().getName()).build());
              facetBuilder.value(facetFieldEntry.getValue());
              facetBuilder.valueCount(facetFieldEntry.getValueCount());
              return facetBuilder.build();
            }
        ).collect(Collectors.toList());
  }

}
