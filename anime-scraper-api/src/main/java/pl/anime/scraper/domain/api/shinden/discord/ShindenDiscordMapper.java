package pl.anime.scraper.domain.api.shinden.discord;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedEpisode;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedSeriesEpisodes;
import pl.anime.scraper.domain.api.shinden.dto.episodes.SimpleShindenLatestAddedEpisode;
import pl.anime.scraper.infra.discord.json.DiscordMessageData;
import pl.anime.scraper.infra.discord.json.DiscordMessageEmbed;
import pl.anime.scraper.infra.discord.json.DiscordMessageEmbedField;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ShindenDiscordMapper {

    private static final String LATEST_ADDED_EPISODE_AUTHOR = "Sanakan";
    private static final String EPISODE_DURATION_NAME = "Długość";
    private static final String EPISODE_TYPE_NAME = "Typ";
    private static final String EPISODE_NUMBER_PREFIX = "Epizod: ";
    private static final String CONTENT_PL = "<@&434247198407131136>";
    private static final String CONTENT_EN = "<@&434247290371178496>";
    private static final String LANGUAGE_PL = "pl";
    private static final String LANGUAGE_EN = "en";
    private static final String LANGUAGE_JP = "jp";

    static List<ShindenRecentlyAddedSeriesEpisodes> mapToLatestAddedEpisodesGroupedBySeries(
            List<DiscordMessageData> discordMessageData) {
        var shindenLatestAddedEpisodes = mapToLastAddedEpisodes(discordMessageData);
        return shindenLatestAddedEpisodes.stream()
                .collect(Collectors.groupingBy(
                        episode -> new ImmutablePair<>(episode.getSeriesTitle(), episode.getSeriesUrl()),
                        LinkedHashMap::new,
                        Collectors.toList()))
                .entrySet()
                .stream()
                .map(entry -> {
                    var episode = new ShindenRecentlyAddedSeriesEpisodes();
                    episode.setTitle(entry.getKey().getLeft());
                    episode.setUrl(entry.getKey().getRight());
                    episode.setLatestEpisodes(convertToSimpleEpisodes(entry));
                    return episode;
                })
                .collect(Collectors.toList());

    }

    static List<ShindenRecentlyAddedEpisode> mapToLastAddedEpisodes(List<DiscordMessageData> discordMessageData) {
        return discordMessageData.stream()
                .filter(message -> LATEST_ADDED_EPISODE_AUTHOR.equalsIgnoreCase(message.getAuthor().getUsername()))
                .map(ShindenDiscordMapper::mapToLastAddedEpisode)
                .collect(Collectors.toList());
    }

    private static List<SimpleShindenLatestAddedEpisode> convertToSimpleEpisodes(
            Entry<ImmutablePair<String, String>, List<ShindenRecentlyAddedEpisode>> entry) {
        return entry.getValue()
                .stream()
                .map(ep -> {
                    var simpleEpisode = new SimpleShindenLatestAddedEpisode();
                    simpleEpisode.setId(ep.getId());
                    simpleEpisode.setNumber(ep.getEpisodeNumber());
                    simpleEpisode.setUrl(ep.getEpisodeUrl());
                    simpleEpisode.setDuration(ep.getEpisodeDuration());
                    simpleEpisode.setType(ep.getEpisodeType());
                    simpleEpisode.setTimestamp(ep.getTimestamp());
                    simpleEpisode.setLanguage(ep.getEpisodeLanguage());
                    return simpleEpisode;
                })
                .sorted((o1, o2) -> {
                    int result = o1.getNumber().compareTo(o2.getNumber());
                    if (result == 0) {
                        return o1.getTimestamp().compareTo(o2.getTimestamp());
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }


    private static ShindenRecentlyAddedEpisode mapToLastAddedEpisode(DiscordMessageData discordMessageData) {
        ShindenRecentlyAddedEpisode latestAddedEpisode = new ShindenRecentlyAddedEpisode();
        DiscordMessageEmbed discordMessageEmbed = discordMessageData.getEmbeds().get(0);
        List<DiscordMessageEmbedField> fields = discordMessageEmbed.getFields();
        Optional<DiscordMessageEmbedField> episodeDuration = fields.stream()
                .filter(field -> EPISODE_DURATION_NAME.equalsIgnoreCase(field.getName())).findFirst();

        Optional<DiscordMessageEmbedField> episodeType = fields.stream()
                .filter(field -> EPISODE_TYPE_NAME.equalsIgnoreCase(field.getName())).findFirst();
        latestAddedEpisode.setId(Long.valueOf(discordMessageData.getId()));
        latestAddedEpisode.setSeriesTitle(discordMessageEmbed.getTitle());
        latestAddedEpisode.setSeriesUrl(discordMessageEmbed.getUrl());
        latestAddedEpisode.setEpisodeNumber(
                Double.valueOf(discordMessageEmbed.getAuthor().getName().substring(EPISODE_NUMBER_PREFIX.length())));
        latestAddedEpisode.setEpisodeUrl(discordMessageEmbed.getAuthor().getUrl());
        latestAddedEpisode.setEpisodeDuration(episodeDuration.map(DiscordMessageEmbedField::getValue).orElse(null));
        latestAddedEpisode.setEpisodeLanguage(defineEpisodeLanguage(discordMessageData.getContent()));
        latestAddedEpisode.setEpisodeType(episodeType.map(DiscordMessageEmbedField::getValue).orElse(null));
        latestAddedEpisode.setTimestamp(discordMessageData.getTimestamp());
        return latestAddedEpisode;
    }

    private static String defineEpisodeLanguage(String content) {
        if (CONTENT_PL.equalsIgnoreCase(content)) {
            return LANGUAGE_PL;
        } else if (CONTENT_EN.equalsIgnoreCase(content)) {
            return LANGUAGE_EN;
        }
        return LANGUAGE_JP;
    }
}
