package pl.anime.scraper.domain.api.shinden.series.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenSeriesStatistics {

    @JsonInclude(Include.NON_NULL)
    private Integer watching;
    @JsonInclude(Include.NON_NULL)
    private Integer completed;
    @JsonInclude(Include.NON_NULL)
    private Integer skip;
    @JsonInclude(Include.NON_NULL)
    private Integer planToWatch;
    @JsonInclude(Include.NON_NULL)
    private Integer dropped;
    @JsonInclude(Include.NON_NULL)
    private Integer onHold;
    @JsonInclude(Include.NON_NULL)
    private Integer favorites;

    public boolean areAllNull() {
        return watching == null && completed == null && skip == null && planToWatch == null && dropped == null
                && onHold == null && favorites == null;
    }
}
