package pl.anime.scraper.domain.api.shinden.series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pl.anime.scraper.domain.api.shinden.login.ShindenSessionLoginDetailsService;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesEpisodeSource;
import pl.anime.scraper.infra.shinden.ShindenJsoupClient;
import pl.anime.scraper.utils.exception.JsoupParserException;
import pl.anime.scraper.utils.parser.ApiParserUtil;

public class ShindenSeriesEpisodeSourceRetriever implements Callable<ShindenSeriesEpisodeSource> {

    private static final String POLISH = "Polski";
    private static final String ENGLISH = "Angielski";
    private static final String JAPANESE = "Japo\u0144ski";

    private final Element elementToParse;
    private final SimpleDateFormat dateFormat;
    private final Map<String, String> cookies;
    private final String authToken;

    private ShindenSeriesEpisodeSourceRetriever(Element elementToParse,
            ShindenSessionLoginDetailsService sessionLoginDetailsService) {
        this.elementToParse = elementToParse;
        this.cookies = sessionLoginDetailsService.getCookies();
        this.authToken = sessionLoginDetailsService.getAuthToken();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:s");
    }

    static Callable<ShindenSeriesEpisodeSource> prepare(Element element,
            ShindenSessionLoginDetailsService sessionLoginDetailsService) {
        return new ShindenSeriesEpisodeSourceRetriever(element, sessionLoginDetailsService);
    }

    @Override
    public ShindenSeriesEpisodeSource call() {

        ShindenSeriesEpisodeSource source = new ShindenSeriesEpisodeSource();
        var id = parseId(elementToParse);
        var url = retrieveSourceUrl(id);

        if (url == null) {
            return null;
        }

        source.setId(id);
        source.setUrl(url);
        source.setService(parseService(elementToParse));
        source.setQuality(parseQuality(elementToParse));
        source.setAudio(parseAudio(elementToParse));
        source.setSubtitles(parseSubtitles(elementToParse));
        source.setAdded(parseCreateDate(elementToParse));

        return source;
    }

    private Long parseId(Element element) {
        String idString = element.getElementsByTag("a").get(0).attr("id");
        return Long.valueOf(idString.replaceAll("player_data_", ""));
    }

    private String parseService(Element element) {
        return element.getElementsByTag("td").get(0).text();
    }

    private String parseQuality(Element element) {
        return element.getElementsByTag("td").get(1).text();
    }

    private String getLanguage(String text) {
        if (POLISH.equalsIgnoreCase(text)) {
            return "pl";
        } else if (ENGLISH.equalsIgnoreCase(text)) {
            return "en";
        } else if (JAPANESE.equalsIgnoreCase(text)) {
            return "jp";
        }

        return "--".equalsIgnoreCase(text) ? null : text;
    }

    private String parseAudio(Element element) {
        var audioText = element.getElementsByTag("td").get(2).text();
        return getLanguage(audioText.trim());
    }

    private String parseSubtitles(Element element) {
        var subtitlesText = element.getElementsByTag("td").get(3).text();
        return getLanguage(subtitlesText.trim());
    }

    private Date parseCreateDate(Element element) {
        String createDateString = element.getElementsByTag("td").get(4).text();
        if (StringUtils.isEmpty(createDateString)) {
            return null;
        }
        try {
            return dateFormat.parse(createDateString);
        } catch (ParseException e) {
            throw new JsoupParserException(SeriesErrorCode.INVALID_PARSE_TO_SOURCE_DATE);
        }

    }

    private String retrieveSourceUrl(Long episodeId) {
        try {

            var playerLoadHandler = ShindenJsoupClient.getPlayerLoad(episodeId, authToken, cookies);
            var waitTime = ApiParserUtil.getAndParse(
                    () -> playerLoadHandler,
                    document -> Integer.valueOf(document.body().text().trim()),
                    SeriesErrorCode.BAD_RESPONSE_FOR_PLAYER_LOAD_FROM_API);

            if (!waitTime.isPresent()) {
                return "Couldn't retrieve source URL, errorCode: " + SeriesErrorCode.NO_WAIT_TIME;
            }
            Thread.sleep(waitTime.getResult() * 1000);

            var updatedCookies = new HashMap<>(cookies);
            updatedCookies.putAll(playerLoadHandler.getCookies());
            return ApiParserUtil.getAndParse(
                    () -> ShindenJsoupClient.getPlayerShow(episodeId, authToken, updatedCookies),
                    this::parseSourceUrl,
                    SeriesErrorCode.BAD_RESPONSE_FOR_PLAYER_SHOW_FROM_API
            ).orElseHandleEmptyReason(apiEmptyReason ->
                    "Couldn't retrieve source URL, errorCode: " + apiEmptyReason.getErrorDetails().getCode()
            );

        } catch (InterruptedException e) {
            return "Couldn't retrieve source URL, reason: " + e.getMessage();
        }


    }

    private String parseSourceUrl(Document document) {
        var rawSourceUrl = document.body().getElementsByTag("iframe").attr("src");
        var clearedUrl = rawSourceUrl.startsWith("//") ? rawSourceUrl.replace("//", "") : rawSourceUrl;
        if (StringUtils.isBlank(clearedUrl)) {
            var buttonPlayer = document.getElementsByClass("button-player")
                    .stream().findFirst();
            if (buttonPlayer.isEmpty()) {
                return null;
            }

            clearedUrl = buttonPlayer.get().attr("href");
        }

        return clearedUrl.startsWith("http") ? clearedUrl : "https://" + clearedUrl;
    }
}
