package pl.anime.scraper.domain.api.shinden.search.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShindenGenresSearchType {

    HAS_TO_HAVE_ALL("all_genres"),
    HAS_TO_HAVE_AT_LEAST_ONE("one");

    private final String value;

}
