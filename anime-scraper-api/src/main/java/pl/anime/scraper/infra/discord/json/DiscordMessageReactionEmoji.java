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
public class DiscordMessageReactionEmoji {

    private final String id;
    private final String name;

    @JsonCreator
    public DiscordMessageReactionEmoji(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
