package pl.anime.scraper.api;

import java.util.List;
import pl.anime.scraper.domain.api.shinden.ShindenService;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedEpisode;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedSeriesEpisodes;
import pl.anime.scraper.domain.api.shinden.login.LoginRequestData;
import pl.anime.scraper.domain.api.shinden.login.ShindenSessionLoginDetailsService;
import pl.anime.scraper.domain.api.shinden.login.json.ShindenLoginResult;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeries;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesCharactersAndCrew;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeOverview;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeSource;
import pl.anime.scraper.utils.api.APIHandler;

public class ShindenAPI {

    private static ShindenAPI INSTANCE;

    private final ShindenService shindenService;

    private ShindenAPI(ShindenSessionLoginDetailsService shindenSessionLoginDetailsService,
            int maxThreadsForEpisodeSourcesParsing) {
        this.shindenService = ShindenService.getInstance(shindenSessionLoginDetailsService,
                maxThreadsForEpisodeSourcesParsing);
    }

    public static ShindenAPI with(ShindenSessionLoginDetailsService shindenSessionLoginDetailsService,
            int maxThreadsForEpisodeSourcesParsing) {
        if (INSTANCE == null) {
            INSTANCE = new ShindenAPI(shindenSessionLoginDetailsService, maxThreadsForEpisodeSourcesParsing);
        }
        return INSTANCE;
    }

    public ShindenLoginResult checkLoginStatus() {
        return shindenService.checkLoginStatus();
    }

    public ShindenLoginResult loginUser(LoginRequestData loginRequestData) {
        return shindenService.loginUser(loginRequestData);
    }

    public APIHandler<List<ShindenRecentlyAddedEpisode>> getRecentlyAddedEpisodes(String discordToken) {
        return shindenService.getRecentlyAddedEpisodes(discordToken);
    }

    public APIHandler<List<ShindenRecentlyAddedEpisode>> getRecentlyAddedEpisodes(String discordToken, int limit) {
        return shindenService.getRecentlyAddedEpisodes(discordToken, limit);
    }

    public APIHandler<List<ShindenRecentlyAddedSeriesEpisodes>> getRecentlyAddedEpisodesGroupedBySeries(
            String discordToken) {
        return shindenService.getRecentlyAddedEpisodesGroupedBySeries(discordToken);
    }

    public APIHandler<List<ShindenRecentlyAddedSeriesEpisodes>> getRecentlyAddedEpisodesGroupedBySeries(
            String discordToken, int limit) {
        return shindenService.getRecentlyAddedEpisodesGroupedBySeries(discordToken, limit);
    }

    public APIHandler<ShindenSeries> findSeries(String seriesId) {
        return shindenService.findSeries(seriesId);
    }

    public APIHandler<ShindenSeriesCharactersAndCrew> findSeriesCharacters(String seriesId) {
        return shindenService.findSeriesCharacters(seriesId);
    }

    public APIHandler<List<ShindenSeriesEpisodeOverview>> findSeriesEpisodes(String seriesId) {
        return shindenService.findSeriesEpisodes(seriesId);
    }

    public APIHandler<List<ShindenSeriesEpisodeSource>> findSeriesEpisodeSources(String seriesId, Long episodeId) {
        return shindenService.findSeriesEpisodeSources(seriesId, episodeId);
    }
}
