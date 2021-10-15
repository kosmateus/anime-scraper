package pl.anime.scraper.infra.discord.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordMessageReaction {

    private final DiscordMessageReactionEmoji emoji;
    private final int count;
    private final boolean me;

    @JsonCreator
    public DiscordMessageReaction(
            @JsonProperty("emoji") DiscordMessageReactionEmoji emoji,
            @JsonProperty("count") int count,
            @JsonProperty("me") boolean me) {
        this.emoji = emoji;
        this.count = count;
        this.me = me;
    }
}
