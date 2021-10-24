package pl.anime.scraper.domain.api.shinden.search.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShindenDatePrecision {

    YEAR(1),
    MONTH_YEAR(2),
    DAY_MONTH_YEAR(3);

    private final Integer value;
}
