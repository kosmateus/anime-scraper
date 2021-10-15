package pl.anime.scraper.infra.discord;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.anime.scraper.config.DiscordRestConstants;
import pl.anime.scraper.infra.discord.json.DiscordMessageData;
import pl.anime.scraper.utils.http.HttpRestClient;
import pl.anime.scraper.utils.http.RequestData;
import pl.anime.scraper.utils.http.ResponseHandler;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DiscordClient {

    public static ResponseHandler<DiscordMessageData[]> getMessages(String token, Long channelId,
            Integer messagesLimit) {
        return HttpRestClient.get(
                RequestData.builder()
                        .withTarget(DiscordRestConstants.DISCORD_API_URL)
                        .withPath(DiscordRestConstants.MESSAGES)
                        .withPathParams(Map.of(DiscordRestConstants.CHANNEL_ID_PARAM, String.valueOf(channelId)))
                        .withQueryParams(Map.of("limit", String.valueOf(messagesLimit)))
                        .withHeaders(Map.of("authorization", token, "host", "discord.com"))
                        .build(),
                DiscordMessageData[].class
        );

    }
}
