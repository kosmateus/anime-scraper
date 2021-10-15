package pl.anime.scraper.domain.api.shinden.series.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShindenRelatedSeries {

   private ShindenAccessKey key;
    private String seriesType;
    private String relationType;
    private String title;
    private String image;
    private String smallImage;

}
