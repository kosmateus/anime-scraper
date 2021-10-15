package pl.anime.scraper.infra.discord.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@EqualsAndHashCode
public class DiscordMessageAuthor {

    private final String id;
    private final String username;
    private final String avatar;
    private final String discriminator;
    private final int publicFlags;
    private final boolean bot;

    @JsonCreator
    public DiscordMessageAuthor(
            @JsonProperty("id") String id,
            @JsonProperty("username") String username,
            @JsonProperty("avatar") String avatar,
            @JsonProperty("discriminator") String discriminator,
            @JsonProperty("public_flags") int publicFlags,
            @JsonProperty("bot") boolean bot) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.discriminator = discriminator;
        this.publicFlags = publicFlags;
        this.bot = bot;
    }
}
