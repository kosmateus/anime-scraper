package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenSeriesInfo {

    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesTag> places;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesTag> archetype;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesTag> genres;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesTag> targetGroup;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesTag> typesOfCharacters;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesTag> otherTags;
    @JsonInclude(Include.NON_EMPTY)
    private String type;
    @JsonInclude(Include.NON_EMPTY)
    private String status;
    @JsonInclude(Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonInclude(Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @JsonInclude(Include.NON_NULL)
    private Integer episodes;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenSeriesTag> studios;
    @JsonInclude(Include.NON_EMPTY)
    private String episodeLength;
    @JsonInclude(Include.NON_EMPTY)
    private String mpaa;


}
