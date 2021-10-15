package pl.anime.scraper.infra.discord.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordMessageEmbed {

    private final String type;
    private final String url;
    private final String title;
    private final int color;
    private final List<DiscordMessageEmbedField> fields;
    private final DiscordMessageEmbedAuthor author;
    private final DiscordMessageEmbedThumbnail thumbnail;

    @JsonCreator
    public DiscordMessageEmbed(
            @JsonProperty("type") String type,
            @JsonProperty("url") String url,
            @JsonProperty("title") String title,
            @JsonProperty("color") int color,
            @JsonProperty("fields") List<DiscordMessageEmbedField> fields,
            @JsonProperty("author") DiscordMessageEmbedAuthor author,
            @JsonProperty("thumbnail") DiscordMessageEmbedThumbnail thumbnail) {
        this.type = type;
        this.url = url;
        this.title = title;
        this.color = color;
        this.fields = fields;
        this.author = author;
        this.thumbnail = thumbnail;
    }
}
