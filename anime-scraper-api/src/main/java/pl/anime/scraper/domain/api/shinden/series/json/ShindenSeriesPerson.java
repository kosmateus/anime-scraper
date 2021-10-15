package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenSeriesPerson {

    private ShindenAccessKey key;
    @JsonInclude(Include.NON_EMPTY)
    private String name;
    @JsonInclude(Include.NON_EMPTY)
    private String firstName;
    @JsonInclude(Include.NON_EMPTY)
    private String lastName;
    @JsonInclude(Include.NON_EMPTY)
    private String smallImage;
    @JsonInclude(Include.NON_EMPTY)
    private String image;
    @JsonInclude(Include.NON_EMPTY)
    private String role;
}
