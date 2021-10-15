package pl.anime.scraper.domain.api.shinden.season.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenTitleStatistics {
    private Integer watching;
    private Integer completed;
    private Integer planToWatch;
    private Integer dropped;
    private Integer onHold;
    private Integer favorites;

}
