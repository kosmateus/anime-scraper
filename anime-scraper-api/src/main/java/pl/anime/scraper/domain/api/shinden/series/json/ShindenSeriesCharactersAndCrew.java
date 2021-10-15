package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenSeriesCharactersAndCrew {

    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesCharacter> characters;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesPerson> crew;
}
