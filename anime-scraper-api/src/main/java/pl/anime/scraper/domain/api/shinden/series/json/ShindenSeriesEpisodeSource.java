package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import pl.anime.scraper.utils.json.DateWithoutMillisecondsSerializer;

@Getter
@Setter
public class ShindenSeriesEpisodeSource {

    private Long id;
    @JsonInclude(Include.NON_EMPTY)
    private String service;
    @JsonInclude(Include.NON_EMPTY)
    private String quality;
    @JsonInclude(Include.NON_EMPTY)
    private String audio;
    @JsonInclude(Include.NON_EMPTY)
    private String subtitles;
    @JsonInclude(Include.NON_EMPTY)
    @JsonSerialize(using = DateWithoutMillisecondsSerializer.class)
    private Date added;
    @JsonInclude(Include.NON_EMPTY)
    private String url;
}
