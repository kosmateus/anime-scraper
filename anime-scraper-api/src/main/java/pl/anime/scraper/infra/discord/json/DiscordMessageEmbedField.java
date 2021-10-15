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
public class DiscordMessageEmbedField {

    private final String name;
    private final String value;
    private final boolean inline;

    @JsonCreator
    public DiscordMessageEmbedField(
            @JsonProperty("name") String name,
            @JsonProperty("value") String value,
            @JsonProperty("inline") boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }
}
