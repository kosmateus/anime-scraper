package pl.anime.scraper.domain.api.shinden.search.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShindenSeriesStatus {

    DECLARATION("Proposal"),
    ANNOUNCEMENT("Not yet aired"),
    CURRENTLY_AIRING("Currently Airing"),
    FINISHED("Finished Airing");


    private final String value;

}
