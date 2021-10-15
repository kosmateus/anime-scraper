package pl.anime.scraper.infra.discord.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordMessageEmbedThumbnail {

    private final String url;
    private final String proxyUrl;
    private final int width;
    private final int height;

    @JsonCreator
    public DiscordMessageEmbedThumbnail(
            @JsonProperty("url") String url,
            @JsonProperty("proxy_url") String proxyUrl,
            @JsonProperty("width") int width,
            @JsonProperty("height") int height) {
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.width = width;
        this.height = height;
    }
}
