package pl.anime.scraper.domain.api.shinden.search.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShindenEpisodeLength {

    LESS_THAN_7_MINUTES("less_7"),
    FROM_7_TO_18_MINUTES("7_to_18"),
    FROM_19_TO_27_MINUTES("19_to_27"),
    FROM_28_TO_48_MINUTES("28_to_48"),
    MORE_THAN_48_MINUTES("over_48");

    private final String value;
}
