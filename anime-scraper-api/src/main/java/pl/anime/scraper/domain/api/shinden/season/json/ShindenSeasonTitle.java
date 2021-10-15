package pl.anime.scraper.domain.api.shinden.season.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenSeasonTitle {

    private Long id;
    private String title;
    private String url;
    private String image;
    @JsonInclude(Include.NON_EMPTY)
    private String description;
    @JsonInclude(Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonInclude(Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @JsonInclude(Include.NON_EMPTY)
    private List<ShindenTitleGenre> genres;
    @JsonInclude(Include.NON_NULL)
    private ShindenTitleRate rate;
    private String type;
    @JsonInclude(Include.NON_NULL)
    private Integer episodes;
    private ShindenTitleStatistics statistics;
}
