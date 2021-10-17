package pl.anime.scraper.app.view.shinden.series;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.anime.scraper.api.ShindenAPI;
import pl.anime.scraper.app.config.ShindenRestContestants;
import pl.anime.scraper.app.utils.view.ResponseEntityUtils;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeries;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesCharactersAndCrew;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeOverview;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeSource;

@RestController
@RequestMapping(ShindenRestContestants.SERIES)
@RequiredArgsConstructor
public class ShindenSeriesController {

    private final ShindenAPI shindenAPI;

    @GetMapping("/{seriesId}")
    public ResponseEntity<ShindenSeries> getSeries(@PathVariable("seriesId") String seriesId) {

        return ResponseEntityUtils.ofApi(() -> shindenAPI.findSeries(seriesId));
    }

    @GetMapping("/{seriesId}/characters")
    public ResponseEntity<ShindenSeriesCharactersAndCrew> getSeriesCharacters(
            @PathVariable("seriesId") String seriesId) {
        return ResponseEntityUtils.ofApi(() -> shindenAPI.findSeriesCharacters(seriesId));
    }

    @GetMapping("/{seriesId}/episodes")
    public ResponseEntity<List<ShindenSeriesEpisodeOverview>> getEpisodesList(
            @PathVariable("seriesId") String seriesId) {
        return ResponseEntityUtils.ofApi(() -> shindenAPI.findSeriesEpisodes(seriesId));
    }


    @GetMapping("/{seriesId}/episodes/view/{episodeId}")
    public ResponseEntity<List<ShindenSeriesEpisodeSource>> getEpisodeSourcesList(
            @PathVariable("seriesId") String seriesId, @PathVariable("episodeId") Long episodeId) {
        return ResponseEntityUtils.ofApi(() -> shindenAPI.findSeriesEpisodeSources(seriesId, episodeId));
    }
}
