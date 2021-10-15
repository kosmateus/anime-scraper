package pl.anime.scraper.domain.api.shinden.dto.episodes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import pl.anime.scraper.utils.json.DoubleZeroDecimalSerializer;

@Getter
@Setter
public class ShindenRecentlyAddedEpisode {

    private Long id;
    private String seriesTitle;
    private String seriesUrl;

    @JsonSerialize(using = DoubleZeroDecimalSerializer.class)
    private Double episodeNumber;
    private String episodeUrl;

    @JsonInclude(Include.NON_NULL)
    private String episodeType;

    @JsonInclude(Include.NON_NULL)
    private String episodeDuration;
    private String episodeLanguage;
    private Date timestamp;
}
