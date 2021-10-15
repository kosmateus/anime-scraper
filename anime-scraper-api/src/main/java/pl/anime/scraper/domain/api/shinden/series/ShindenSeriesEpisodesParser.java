package pl.anime.scraper.domain.api.shinden.series;

import java.util.Collections;
import java.util.List;
import org.jsoup.nodes.Document;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeOverview;

class ShindenSeriesEpisodesParser {

    static List<ShindenSeriesEpisodeOverview> parseEpisodesOverview(Document document) {
        var episodesList = document.getElementsByClass("list-episode-checkboxes")
                .stream()
                .findFirst();

        if (episodesList.isEmpty()) {
            return Collections.emptyList();
        }
        return null;
    }
}
