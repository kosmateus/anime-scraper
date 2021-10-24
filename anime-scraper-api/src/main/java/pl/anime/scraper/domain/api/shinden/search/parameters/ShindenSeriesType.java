package pl.anime.scraper.domain.api.shinden.search.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShindenSeriesType {

    TV("TV"),
    ONA("ONA"),
    OVA("OVA"),
    MOVIE("Movie"),
    SPECIAL("Special"),
    MUSIC("Music");

    private final String value;
}
