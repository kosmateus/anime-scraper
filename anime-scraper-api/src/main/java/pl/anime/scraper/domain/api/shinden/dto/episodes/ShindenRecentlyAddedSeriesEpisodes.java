package pl.anime.scraper.domain.api.shinden.dto.episodes;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenRecentlyAddedSeriesEpisodes {

    private String title;
    private String url;
    private List<SimpleShindenLatestAddedEpisode> latestEpisodes;

}
