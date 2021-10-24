package pl.anime.scraper.domain.api.shinden.search.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenAccessKey;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesRate;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesTag;

@Getter
@Setter
public class ShindenSearchSeries {

    private ShindenAccessKey key;
    private String image;
    private String title;
    private List<ShindenSeriesTag> genres;
    @JsonInclude(Include.NON_NULL)
    private String type;
    @JsonInclude(Include.NON_NULL)
    private Integer episodesNumber;
    @JsonInclude(Include.NON_NULL)
    private String episodesLength;
    private ShindenSeriesRate rate;
    @JsonInclude(Include.NON_NULL)
    private String status;
    @JsonInclude(Include.NON_NULL)
    private Double topRate;
}
