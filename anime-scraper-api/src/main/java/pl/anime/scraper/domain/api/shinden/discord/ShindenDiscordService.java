package pl.anime.scraper.domain.api.shinden.discord;

import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.anime.scraper.config.ShindenConstants;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedEpisode;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedSeriesEpisodes;
import pl.anime.scraper.infra.discord.DiscordClient;
import pl.anime.scraper.utils.api.APIHandler;
import pl.anime.scraper.utils.api.ApiEmptyReason;
import pl.anime.scraper.utils.api.ApiErrorDetails;
import pl.anime.scraper.utils.api.ApiExternalRequestErrorDetails;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShindenDiscordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShindenDiscordService.class);

    public static APIHandler<List<ShindenRecentlyAddedEpisode>> getRecentlyAddedEpisodes(String token,
            int messagesLimit) {
        var responseHandler = DiscordClient.getMessages(token, ShindenConstants.DISCORD_EPISODES_CHANNEL_ID,
                messagesLimit);

        if (responseHandler.isPresent()) {
            return APIHandler.of(
                    ShindenDiscordMapper.mapToLastAddedEpisodes(Arrays.asList(responseHandler.getEntity()))
            );
        } else if (responseHandler.isOk()) {
            return APIHandler.emptyOk();
        }
        LOGGER.warn(
                String.format("Response from discord on fetching messages from channelId: %s was with status code: %s",
                        ShindenConstants.DISCORD_EPISODES_CHANNEL_ID, responseHandler.getHttpStatus()));

        var errorDetails = responseHandler.getEmptyReason().getErrorDetails();
        return APIHandler.empty(ApiEmptyReason.badResponseFromExternalService(
                        ApiErrorDetails.builder()
                                .withCode(ErrorCode.INVALID_RESPONSE_FROM_SHINDEN_DISCORD)
                                .withErrorName("Invalid response from external service")
                                .withMessage("Something went wrong during call do shinden discord for latest episodes")
                                .withExternalRequestErrorDetails(
                                        ApiExternalRequestErrorDetails.of(errorDetails, responseHandler.getHttpStatus())
                                )
                                .build()
                )
        );
    }

    public static APIHandler<List<ShindenRecentlyAddedSeriesEpisodes>> getRecentlyAddedEpisodesGroupedBySeries(
            String token, int messagesLimit) {
        var responseHandler = DiscordClient.getMessages(token, ShindenConstants.DISCORD_EPISODES_CHANNEL_ID,
                messagesLimit);

        if (responseHandler.isPresent()) {
            return APIHandler.of(
                    ShindenDiscordMapper.mapToLatestAddedEpisodesGroupedBySeries(
                            Arrays.asList(responseHandler.getEntity()))
            );
        } else if (responseHandler.isOk()) {
            return APIHandler.emptyOk();
        }
        LOGGER.warn(
                String.format("Response from discord on fetching messages from channelId: %s was with status code: %s",
                        ShindenConstants.DISCORD_EPISODES_CHANNEL_ID, responseHandler.getHttpStatus()));

        var errorDetails = responseHandler.getEmptyReason().getErrorDetails();
        return APIHandler.empty(ApiEmptyReason.badResponseFromExternalService(
                        ApiErrorDetails.builder()
                                .withCode(ErrorCode.INVALID_RESPONSE_FROM_SHINDEN_DISCORD)
                                .withErrorName("Invalid response from external service")
                                .withMessage("Something went wrong during call do shinden discord for latest episodes")
                                .withExternalRequestErrorDetails(
                                        ApiExternalRequestErrorDetails.of(errorDetails, responseHandler.getHttpStatus())
                                )
                                .build()
                )
        );
    }

}
