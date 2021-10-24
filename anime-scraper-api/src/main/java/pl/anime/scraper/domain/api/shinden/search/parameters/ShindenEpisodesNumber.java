package pl.anime.scraper.domain.api.shinden.search.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShindenEpisodesNumber {

    ONLY_ONE_EPISODE("only_1"),
    FROM_2_TO_14_EPISODES("2_to_14"),
    FROM_15_TO_28_EPISODES("15_to_28"),
    FROM_29_TO_100_EPISODES("29_to_100"),
    MORE_THAN_100_EPISODES("over_100");

    private final String value;
}
