package com.nomad.socialspring.search;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class SearchController {

  private final SearchService searchService;

  @GetMapping
  public SearchResult search(
      @RequestParam(name = "query", defaultValue = "") String query,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "25") int size
  ) {
    return searchService.search(query, page, size);
  }

}
