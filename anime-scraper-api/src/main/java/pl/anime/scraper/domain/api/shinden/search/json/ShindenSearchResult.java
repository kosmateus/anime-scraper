package pl.anime.scraper.domain.api.shinden.search.json;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import pl.anime.scraper.domain.api.shinden.search.ShindenSearchParameters;

@Getter
@Setter
public class ShindenSearchResult {

    private List<ShindenSearchSeries> foundSeries;
    private Integer currentPage;
    private Integer lastPage;
    private ShindenSearchParameters searchParameters;
}
