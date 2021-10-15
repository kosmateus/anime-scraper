package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenSeries {

    private String title;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> alternateTitles;
    @JsonInclude(Include.NON_NULL)
    private String smallImage;
    @JsonInclude(Include.NON_NULL)
    private String image;
    @JsonInclude(Include.NON_NULL)
    private String description;
    @JsonInclude(Include.NON_NULL)
    private ShindenSeriesInfo info;
    @JsonInclude(Include.NON_NULL)
    private ShindenSeriesRate rate;
    @JsonInclude(Include.NON_NULL)
    private ShindenSeriesStatistics statistics;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesCharacter> characters;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesPerson> crew;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenRelatedSeries> relatedSeries;
}
