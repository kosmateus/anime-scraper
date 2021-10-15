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
public class SimpleShindenLatestAddedEpisode {

    private Long id;

    @JsonSerialize(using = DoubleZeroDecimalSerializer.class)
    private Double number;
    private String url;

    @JsonInclude(Include.NON_NULL)
    private String type;

    @JsonInclude(Include.NON_NULL)
    private String duration;
    private String language;
    private Date timestamp;
}
