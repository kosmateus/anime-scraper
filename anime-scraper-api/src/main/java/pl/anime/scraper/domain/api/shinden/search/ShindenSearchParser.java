package pl.anime.scraper.domain.api.shinden.search;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import pl.anime.scraper.config.ShindenConstants;
import pl.anime.scraper.domain.api.shinden.search.json.ShindenSearchResult;
import pl.anime.scraper.domain.api.shinden.search.json.ShindenSearchSeries;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenAccessKey;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesRate;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesTag;
import pl.anime.scraper.domain.api.shinden.utils.ShindenUtils;
import pl.anime.scraper.utils.exception.JsoupParserException;

class ShindenSearchParser {

    static ShindenSearchResult parseToSearchResult(Document document, ShindenSearchParameters parameters) {

        ShindenSearchResult result = new ShindenSearchResult();
        result.setSearchParameters(parameters);
        result.setCurrentPage(acquireCurrentPage(document));
        result.setLastPage(acquireLastPage(document));
        result.setFoundSeries(acquireSeries(document));
        return result;
    }

    private static Integer acquireCurrentPage(Document document) {
        var pagination = document.getElementsByClass("pagination")
                .stream()
                .findFirst();

        if (pagination.isEmpty()) {
            return 1;
        }

        return Integer.valueOf(
                pagination.get().getElementsByTag("strong")
                        .stream()
                        .findFirst()
                        .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_CURRENT_PAGE_IN_PAGINATION))
                        .text()
        );
    }

    private static Integer acquireLastPage(Document document) {
        var pagination = document.getElementsByClass("pagination")
                .stream()
                .findFirst();

        if (pagination.isEmpty()) {
            return 1;
        }

        var liElements = pagination.get().getElementsByTag("li");

        int foundIndex = -1;
        for (int i = 0; i < liElements.size(); i++) {
            if (liElements.get(i).hasClass("pagination-next")) {
                foundIndex = i - 1;
                break;
            }
        }

        if (foundIndex == -1) {
            throw new JsoupParserException(SearchErrorCodes.NO_LAST_PAGE_IN_PAGINATION);
        }

        return Integer.valueOf(
                liElements.get(foundIndex)
                        .getElementsByTag("a")
                        .stream()
                        .findFirst()
                        .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_LAST_PAGE_IN_PAGINATION))
                        .text()
        );
    }

    private static List<ShindenSearchSeries> acquireSeries(Document document) {
        var article = document.getElementsByTag("article")
                .stream()
                .findFirst();

        if (article.isEmpty()) {
            return Collections.emptyList();
        }

        return article.get()
                .getElementsByClass("div-row")
                .stream()
                .map(ShindenSearchParser::acquireSeriesDetails)
                .collect(Collectors.toList());
    }

    private static ShindenSearchSeries acquireSeriesDetails(Element element) {
        ShindenSearchSeries series = new ShindenSearchSeries();
        series.setKey(acquireKey(element));
        series.setImage(acquireImageUrl(element));
        series.setTitle(acquireTitle(element));
        series.setGenres(acquireGenres(element));
        series.setType(acquireType(element));
        series.setEpisodesNumber(acquireEpisodesNumber(element));
        series.setEpisodesLength(acquireEpisodesLength(element));
        series.setRate(acquireRates(element));
        series.setStatus(acquireStatus(element));
        series.setTopRate(acquireTopRate(element));
        return series;
    }

    private static ShindenAccessKey acquireKey(Element element) {
        var hrefAttr = element.getElementsByTag("h3")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_ACCESS_KEY))
                .getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_ACCESS_KEY))
                .attr("href");
        return ShindenUtils.acquireAccessKey(hrefAttr);

    }

    private static Double acquireTopRate(Element element) {
        var rate = element.getElementsByClass("rate-top")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_RATE_TOP))
                .text()
                .trim()
                .replace(",", ".");

        return NumberUtils.isCreatable(rate) ? Double.valueOf(rate) : null;
    }

    private static String acquireStatus(Element element) {
        return element.getElementsByClass("title-status-col")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_STATUS))
                .text()
                .trim();
    }

    private static ShindenSeriesRate acquireRates(Element element) {

        var ratings = element.getElementsByClass("ratings-col")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_RATE));
        ShindenSeriesRate rate = new ShindenSeriesRate();
        rate.setOverall(acquireRate(ratings, "rating rating-total", SearchErrorCodes.NO_OVERALL_RATE));
        rate.setPlot(acquireRate(ratings, "rating rating-story", SearchErrorCodes.NO_PLOT_RATE));
        rate.setGraphics(acquireRate(ratings, "rating rating-graphics", SearchErrorCodes.NO_GRAPHICS_RATE));
        rate.setMusic(acquireRate(ratings, "rating rating-music", SearchErrorCodes.NO_MUSIC_RATE));
        rate.setCharacters(acquireRate(ratings, "rating rating-titlecahracters", SearchErrorCodes.NO_CHARACTERS_RATE));
        return rate;
    }

    private static Double acquireRate(Element ratings, String className, SearchErrorCodes code) {
        var span = ratings.getElementsByClass(className)
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(code))
                .getElementsByTag("span")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(code));

        var description = span.getElementsByTag("em")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(code))
                .text()
                .trim();
        var rate = span
                .text()
                .trim();
        var onlyRate = rate.replace(description, "").trim().replace(",", ".");
        return NumberUtils.isCreatable(onlyRate) ? Double.valueOf(onlyRate) : null;
    }

    private static String acquireEpisodesLength(Element element) {
        var episodeDuration = element.getElementsByClass("episodes-col")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_EPISODES_LENGTH))
                .attr("title");

        var splitEpisodeDuration = episodeDuration.split("x");
        return splitEpisodeDuration.length > 1 ? splitEpisodeDuration[1].trim() : null;
    }

    private static Integer acquireEpisodesNumber(Element element) {
        var number = element.getElementsByClass("episodes-col")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_EPISODES_NUMBER))
                .text();

        return NumberUtils.isCreatable(number) ? Integer.valueOf(number) : null;
    }

    private static String acquireType(Element element) {
        return element.getElementsByClass("title-kind-col")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_SERIES_TYPE))
                .text();
    }

    private static List<ShindenSeriesTag> acquireGenres(Element element) {
        return element.getElementsByClass("tags")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_GENRES))
                .getElementsByTag("a")
                .stream()
                .map(ShindenUtils::createTagFromHtmlATag)
                .collect(Collectors.toList());
    }

    private static String acquireTitle(Element element) {
        return element.getElementsByTag("h3")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_TITLE))
                .getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_TITLE))
                .text();
    }

    private static String acquireImageUrl(Element element) {
        return ShindenConstants.SHINDEN_URL + element.getElementsByClass("cover-col")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_IMAGE_URL))
                .getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SearchErrorCodes.NO_IMAGE_URL))
                .attr("href");

    }

}
