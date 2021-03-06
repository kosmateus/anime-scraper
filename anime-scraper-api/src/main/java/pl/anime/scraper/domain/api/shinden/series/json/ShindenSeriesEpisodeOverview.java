package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import pl.anime.scraper.utils.json.DoubleZeroDecimalSerializer;

@Getter
@Setter
public class ShindenSeriesEpisodeOverview {

    @JsonInclude(Include.NON_EMPTY)
    private Long id;
    @JsonInclude(Include.NON_EMPTY)
    @JsonSerialize(using = DoubleZeroDecimalSerializer.class)
    private Double number;
    @JsonInclude(Include.NON_EMPTY)
    private String title;
    private boolean online;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> languages;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonInclude(Include.NON_EMPTY)
    private Date releaseDate;

}
