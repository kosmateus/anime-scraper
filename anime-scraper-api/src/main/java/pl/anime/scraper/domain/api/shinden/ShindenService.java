package pl.anime.scraper.domain.api.shinden;

import java.util.List;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.anime.scraper.domain.api.shinden.discord.ShindenDiscordService;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedEpisode;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedSeriesEpisodes;
import pl.anime.scraper.domain.api.shinden.login.LoginRequestData;
import pl.anime.scraper.domain.api.shinden.login.ShindenLoginService;
import pl.anime.scraper.domain.api.shinden.login.ShindenSessionLoginDetailsService;
import pl.anime.scraper.domain.api.shinden.login.json.ShindenLoginResult;
import pl.anime.scraper.domain.api.shinden.search.ShindenSearchParameters;
import pl.anime.scraper.domain.api.shinden.search.ShindenSearchService;
import pl.anime.scraper.domain.api.shinden.search.json.ShindenSearchResult;
import pl.anime.scraper.domain.api.shinden.series.ShindenSeriesService;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeries;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesCharactersAndCrew;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeOverview;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeSource;
import pl.anime.scraper.utils.api.APIHandler;
import pl.anime.scraper.utils.api.ApiEmptyReason;
import pl.anime.scraper.utils.api.ApiErrorDetails;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShindenService {

    private static ShindenService INSTANCE;

    private ShindenSessionLoginDetailsService sessionLoginDetails;
    private int maxThreadsForEpisodeSourcesParsing;

    public static ShindenService getInstance(ShindenSessionLoginDetailsService sessionLoginDetails,
            int maxThreadsForEpisodeSourcesParsing) {
        if (INSTANCE == null) {
            INSTANCE = new ShindenService(sessionLoginDetails, maxThreadsForEpisodeSourcesParsing);

        }
        return INSTANCE;
    }

    public APIHandler<List<ShindenRecentlyAddedEpisode>> getRecentlyAddedEpisodes(String discordToken) {
        return getRecentlyAddedEpisodes(discordToken, 50);
    }

    public APIHandler<List<ShindenRecentlyAddedEpisode>> getRecentlyAddedEpisodes(String discordToken,
            int limit) {
        return executeWithLimitValidation(limit,
                () -> ShindenDiscordService.getRecentlyAddedEpisodes(discordToken, limit));
    }

    public APIHandler<List<ShindenRecentlyAddedSeriesEpisodes>> getRecentlyAddedEpisodesGroupedBySeries(
            String discordToken) {
        return getRecentlyAddedEpisodesGroupedBySeries(discordToken, 50);
    }

    public APIHandler<List<ShindenRecentlyAddedSeriesEpisodes>> getRecentlyAddedEpisodesGroupedBySeries(
            String discordToken, int limit) {
        return executeWithLimitValidation(limit,
                () -> ShindenDiscordService.getRecentlyAddedEpisodesGroupedBySeries(discordToken, limit));
    }

    public APIHandler<ShindenSeries> findSeries(String seriesId) {
        return ShindenSeriesService.findSeries(seriesId);
    }

    public APIHandler<ShindenSeriesCharactersAndCrew> findSeriesCharacters(String seriesId) {
        return ShindenSeriesService.findSeriesCharacters(seriesId);
    }


    public APIHandler<List<ShindenSeriesEpisodeOverview>> findSeriesEpisodes(String seriesId) {
        return ShindenSeriesService.findSeriesEpisodes(seriesId, sessionLoginDetails);
    }

    public APIHandler<List<ShindenSeriesEpisodeSource>> findSeriesEpisodeSources(String seriesId, Long episodeId) {
        return ShindenSeriesService.findSeriesEpisodeSources(seriesId, episodeId, sessionLoginDetails,
                maxThreadsForEpisodeSourcesParsing);
    }

    public APIHandler<ShindenSearchResult> searchSeries(ShindenSearchParameters searchParameters) {
        return ShindenSearchService.searchSeries(searchParameters, sessionLoginDetails);
    }

    public ShindenLoginResult loginUser(LoginRequestData loginRequestData) {
        return ShindenLoginService.loginUser(loginRequestData, this.sessionLoginDetails);
    }

    public ShindenLoginResult checkLoginStatus() {
        return ShindenLoginService.checkLogin(this.sessionLoginDetails);
    }

    private <T> APIHandler<T> executeWithLimitValidation(int limit, Supplier<APIHandler<T>> serviceExecution) {
        if (isLimitNotValid(limit)) {
            return APIHandler.empty(ApiEmptyReason.badRequestParameters(
                    ApiErrorDetails.badRequestParameters(GenericErrorCodes.INVALID_LIMIT_PARAMETER,
                            "Limit parameter should be between 1 and 100")));
        }
        return serviceExecution.get();
    }

    private boolean isLimitNotValid(int limit) {
        return limit < 1 || limit > 100;
    }
}
