package pl.anime.scraper.infra.discord.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordMessageData {

    private final String id;
    private final int type;
    private final String content;
    private final String channelId;
    private final DiscordMessageAuthor author;
    private final List<Object> attachments;
    private final List<DiscordMessageEmbed> embeds;
    private final List<Object> mentions;
    private final List<String> mentionRoles;
    private final boolean pinned;
    private final boolean mentionEveryone;
    private final boolean tts;
    private final Date timestamp;
    private final Date editedTimestamp;
    private final int flags;
    private final List<Object> components;
    private final List<DiscordMessageReaction> reactions;

    @JsonCreator
    public DiscordMessageData(
            @JsonProperty("id") String id,
            @JsonProperty("type") int type,
            @JsonProperty("content") String content,
            @JsonProperty("channel_id") String channelId,
            @JsonProperty("author") DiscordMessageAuthor author,
            @JsonProperty("attachments") List<Object> attachments,
            @JsonProperty("embeds") List<DiscordMessageEmbed> embeds,
            @JsonProperty("mentions") List<Object> mentions,
            @JsonProperty("mention_roles") List<String> mentionRoles,
            @JsonProperty("pinned") boolean pinned,
            @JsonProperty("mention_everyone") boolean mentionEveryone,
            @JsonProperty("tts") boolean tts,
            @JsonProperty("timestamp") Date timestamp,
            @JsonProperty("edited_timestamp") Date editedTimestamp,
            @JsonProperty("flags") int flags,
            @JsonProperty("components") List<Object> components,
            @JsonProperty("reactions") List<DiscordMessageReaction> reactions) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.channelId = channelId;
        this.author = author;
        this.attachments = attachments;
        this.embeds = embeds;
        this.mentions = mentions;
        this.mentionRoles = mentionRoles;
        this.pinned = pinned;
        this.mentionEveryone = mentionEveryone;
        this.tts = tts;
        this.timestamp = timestamp;
        this.editedTimestamp = editedTimestamp;
        this.flags = flags;
        this.components = components;
        this.reactions = reactions;
    }
}
