package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenSeriesRate {
    @JsonInclude(Include.NON_NULL)
    private Double overall;
    @JsonInclude(Include.NON_NULL)
    private Double plot;
    @JsonInclude(Include.NON_NULL)
    private Double graphics;
    @JsonInclude(Include.NON_NULL)
    private Double music;
    @JsonInclude(Include.NON_NULL)
    private Double characters;
    @JsonInclude(Include.NON_NULL)
    private Long votes;

}
