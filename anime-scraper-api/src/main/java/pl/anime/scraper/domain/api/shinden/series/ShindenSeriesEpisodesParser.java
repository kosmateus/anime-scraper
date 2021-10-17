package pl.anime.scraper.domain.api.shinden.series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.anime.scraper.domain.api.shinden.login.ShindenSessionLoginDetailsService;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeOverview;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeSource;
import pl.anime.scraper.utils.exception.JsoupParserException;

class ShindenSeriesEpisodesParser {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String POLISH = "Polski";
    private static final String ENGLISH = "Angielski";
    private static final String JAPANESE = "Japoński";

    static List<ShindenSeriesEpisodeOverview> parseEpisodesOverview(Document document) {
        var episodesList = document.getElementsByClass("list-episode-checkboxes")
                .stream()
                .findFirst();

        if (episodesList.isEmpty()) {
            return Collections.emptyList();
        }

        var episodesElement = episodesList.get();

        Elements episodes = episodesElement.getElementsByTag("tr");

        if (episodes.isEmpty()) {
            throw new JsoupParserException(SeriesErrorCode.NO_EPISODES_FOR_LIST);
        }

        return episodes.stream()
                .map(episode -> {
                    Elements tdElements = episode.getElementsByTag("td");
                    var shindenEpisode = new ShindenSeriesEpisodeOverview();
                    shindenEpisode.setNumber(Double.valueOf(tdElements.get(0).text()));
                    shindenEpisode.setTitle(tdElements.get(1).text());
                    shindenEpisode.setOnline(acquireIsOnline(tdElements.get(2)));
                    shindenEpisode.setLanguages(acquireLanguages(tdElements.get(3)));
                    shindenEpisode.setReleaseDate(acquireReleaseDate(tdElements.get(4)));
                    shindenEpisode.setId(acquireEpisodeId(tdElements.get(5)));
                    return shindenEpisode;
                })
                .sorted(Comparator.comparing(ShindenSeriesEpisodeOverview::getNumber))
                .collect(Collectors.toList());
    }

    static List<ShindenSeriesEpisodeSource> parseEpisodeSources(Document document,
            ShindenSessionLoginDetailsService sessionLoginDetailsService, int maxThreadsForEpisodeSourcesParsing) {
        var trElements = document.getElementsByClass("box episode-player-list")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_EPISODE_PLAYER_LIST))
                .getElementsByTag("tbody")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_TBODY_IN_EPISODE_PLAYER_LIST))
                .getElementsByTag("tr");

        if (trElements.isEmpty()) {
            return Collections.emptyList();
        }

        var executorService = Executors.newFixedThreadPool(
                Math.min(trElements.size(), maxThreadsForEpisodeSourcesParsing));
        var futureResults = new ArrayList<Future<ShindenSeriesEpisodeSource>>();

        trElements.forEach(element -> futureResults.add(executorService.submit(
                ShindenSeriesEpisodeSourceRetriever.prepare(element, sessionLoginDetailsService))));

        var episodeSources = futureResults
                .stream()
                .map(task -> {
                    try {
                        return task.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new JsoupParserException(SeriesErrorCode.EXCEPTION_DURING_GETTING_TASK_RESULT);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        executorService.shutdown();
        return episodeSources;
    }

    private static Long acquireEpisodeId(Element episodeElement) {
        var aTag = episodeElement.getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_EPISODE_ID));

        var splitAddress = aTag.attr("href").split("/");

        return Long.valueOf(splitAddress[splitAddress.length - 1]);
    }

    private static Date acquireReleaseDate(Element dateElement) {
        var date = dateElement.text();

        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {

            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new JsoupParserException(SeriesErrorCode.NO_VALID_EPISODE_RELEASE_DATE);
        }
    }

    private static boolean acquireIsOnline(Element isOnlineElement) {
        var iTag = isOnlineElement.getElementsByTag("i")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_IS_ONLINE_FLAG));

        return iTag.hasClass("fa-check");
    }

    private static List<String> acquireLanguages(Element languages) {
        return languages.getElementsByTag("span")
                .stream()
                .map(span -> {
                    var title = span.attr("title");
                    return getLanguage(title.trim());
                })
                .collect(Collectors.toList());
    }

    private static String getLanguage(String text) {
        if (POLISH.equalsIgnoreCase(text)) {
            return "pl";
        } else if (ENGLISH.equalsIgnoreCase(text)) {
            return "en";
        } else if (JAPANESE.equalsIgnoreCase(text)) {
            return "jp";
        }

        return text;
    }
}
