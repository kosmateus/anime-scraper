package pl.anime.scraper.infra.discord.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordMessageEmbedAuthor {

    private final String name;
    private final String url;

    @JsonCreator
    public DiscordMessageEmbedAuthor(
            @JsonProperty("name") String name,
            @JsonProperty("url") String url) {
        this.name = name;
        this.url = url;
    }
}
