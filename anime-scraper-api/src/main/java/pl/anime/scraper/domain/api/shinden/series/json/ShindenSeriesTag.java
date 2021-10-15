package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenSeriesTag {

    private ShindenAccessKey key;
    private String text;
    @JsonInclude(Include.NON_EMPTY)
    private String description;
}
