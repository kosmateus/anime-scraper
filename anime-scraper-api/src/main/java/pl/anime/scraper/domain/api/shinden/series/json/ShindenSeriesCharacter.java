package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenSeriesCharacter {

    @JsonInclude(Include.NON_NULL)
    private ShindenCharacter character;
    @JsonInclude(Include.NON_EMPTY)
    private Map<String, ShindenSeriesPerson> cast;
}
