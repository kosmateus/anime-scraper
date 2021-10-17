package pl.anime.scraper.domain.api.shinden.series;


import java.util.List;
import pl.anime.scraper.domain.api.shinden.login.ShindenSessionLoginDetailsService;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeries;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesCharactersAndCrew;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeOverview;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeSource;
import pl.anime.scraper.infra.shinden.ShindenJsoupClient;
import pl.anime.scraper.utils.api.APIHandler;
import pl.anime.scraper.utils.login.LoginUtils;
import pl.anime.scraper.utils.parser.ApiParserUtil;

public class ShindenSeriesService {

    public static APIHandler<ShindenSeries> findSeries(String seriesId) {
        return ApiParserUtil.getAndParse(() -> ShindenJsoupClient.getSeriesPage(seriesId),
                ShindenSeriesParser::parseToShindenSeries,
                SeriesErrorCode.BAD_RESPONSE_FOR_SERIES_PAGE
        );
    }

    public static APIHandler<ShindenSeriesCharactersAndCrew> findSeriesCharacters(String seriesId) {
        return ApiParserUtil.getAndParse(() -> ShindenJsoupClient.getSeriesCharactersPage(seriesId),
                ShindenSeriesParser::parseToShindenSeriesCharactersAndCrew,
                SeriesErrorCode.BAD_RESPONSE_FOR_SERIES_CHARACTERS_PAGE
        );
    }

    public static APIHandler<List<ShindenSeriesEpisodeOverview>> findSeriesEpisodes(String seriesId,
            ShindenSessionLoginDetailsService sessionLoginDetailsService) {

        return ApiParserUtil.getAndParse(
                () -> LoginUtils.isLogin(sessionLoginDetailsService),
                () -> ShindenJsoupClient.getSeriesEpisodesPage(seriesId,
                        LoginUtils.getCookies(sessionLoginDetailsService)),
                ShindenSeriesEpisodesParser::parseEpisodesOverview,
                SeriesErrorCode.BAD_RESPONSE_FOR_SERIES_EPISODES_OVERVIEW,
                SeriesErrorCode.USER_NOT_AUTHENTICATED_FOR_EPISODES_OVERVIEW
        );
    }

    public static APIHandler<List<ShindenSeriesEpisodeSource>> findSeriesEpisodeSources(
            String seriesId,
            Long episodeId,
            ShindenSessionLoginDetailsService sessionLoginDetailsService,
            int maxThreadsForEpisodeSourcesParsing) {
        return ApiParserUtil.getAndParse(
                () -> LoginUtils.isLogin(sessionLoginDetailsService),
                () -> ShindenJsoupClient.getSeriesEpisodeSourcesPage(seriesId, episodeId,
                        LoginUtils.getCookies(sessionLoginDetailsService)),
                (document) -> ShindenSeriesEpisodesParser.parseEpisodeSources(document,
                        sessionLoginDetailsService, maxThreadsForEpisodeSourcesParsing),
                SeriesErrorCode.BAD_RESPONSE_FOR_SERIES_EPISODE_SOURCES,
                SeriesErrorCode.USER_NOT_AUTHENTICATED_FOR_EPISODE_SOURCES
        );
    }
}
