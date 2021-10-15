package pl.anime.scraper.domain.api.shinden.season;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.anime.scraper.config.ShindenConstants;
import pl.anime.scraper.domain.api.shinden.season.json.ShindenSeason;
import pl.anime.scraper.domain.api.shinden.season.json.ShindenSeasonTitle;
import pl.anime.scraper.domain.api.shinden.season.json.ShindenTitleGenre;
import pl.anime.scraper.domain.api.shinden.season.json.ShindenTitleRate;
import pl.anime.scraper.domain.api.shinden.season.json.ShindenTitleStatistics;
import pl.anime.scraper.utils.exception.JsoupParserException;

class ShindenLatestSeasonParser {

    private static final SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM.yyyy");
    private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

    static List<ShindenSeason> parseToLatestSeasons(Document page) {
        Element latestSeasonsBox = page.getElementsByClass("box  box-new-series")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_BOX_NEW_SERIES));

        Element seasons = latestSeasonsBox.getElementsByClass("seasons-list")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_SEASONS_LIST));

        Elements seasonUrls = seasons.getElementsByTag("a");

        return seasonUrls.stream()
                .map(url -> {
                    String[] hrefElements = url.attr("href")
                            .split("/");
                    var shindenSeason = new ShindenSeason();
                    shindenSeason.setText(url.text());
                    shindenSeason.setYear(Integer.valueOf(hrefElements[hrefElements.length - 2]));
                    shindenSeason.setSeason(hrefElements[hrefElements.length - 1]);
                    return shindenSeason;
                })
                .collect(Collectors.toList());

    }

    static Map<String, List<ShindenSeasonTitle>> parseToSeasonTitles(Document page) {

        var result = new LinkedHashMap<String, List<ShindenSeasonTitle>>();
        result.put("tv", acquireTitlesForGroup(page, "group-tv"));
        result.put("ona", acquireTitlesForGroup(page, "group-ona"));
        result.put("ova", acquireTitlesForGroup(page, "group-ova"));
        result.put("movie", acquireTitlesForGroup(page, "group-movie"));
        result.put("special", acquireTitlesForGroup(page, "group-special"));
        result.put("music", acquireTitlesForGroup(page, "group-music"));
        return result;
    }

    private static List<ShindenSeasonTitle> acquireTitlesForGroup(Document page, String groupName) {
        Element group = Optional.ofNullable(page.getElementById(groupName))
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_GROUP_NAME));
        Elements animes = group.getElementsByClass("title anime");
        return animes.stream()
                .map(animeElement -> {
                    var shindenSeasonTitle = new ShindenSeasonTitle();
                    shindenSeasonTitle.setId(acquireId(animeElement));
                    shindenSeasonTitle.setTitle(acquireTitle(animeElement));
                    shindenSeasonTitle.setUrl(acquireUrl(animeElement));
                    shindenSeasonTitle.setImage(acquireImage(animeElement));
                    shindenSeasonTitle.setDescription(acquireDescription(animeElement));
                    shindenSeasonTitle.setRate(acquireRate(animeElement));
                    shindenSeasonTitle.setEpisodes(acquireEpisodesNumber(animeElement));
                    shindenSeasonTitle.setStartDate(acquireStartDate(animeElement));
                    shindenSeasonTitle.setEndDate(acquireEndDate(animeElement));
                    shindenSeasonTitle.setType(acquireType(animeElement));
                    shindenSeasonTitle.setGenres(acquireGenres(animeElement));
                    shindenSeasonTitle.setStatistics(acquireStatistics(animeElement));
                    return shindenSeasonTitle;
                })
                .collect(Collectors.toList());
    }

    private static Long acquireId(Element animeElement) {
        return Long.valueOf(animeElement.attr("data-id"));
    }

    private static String acquireTitle(Element animeElement) {
        Element titleElement = animeElement.getElementsByClass("box-title")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_BOX_TITLE));

        Element aElement = titleElement.getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_A_TAG_IN_BOX_TITLE));

        return aElement.text();
    }

    private static String acquireUrl(Element animeElement) {
        Element titleElement = animeElement.getElementsByClass("box-title")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_BOX_TITLE));

        Element aElement = titleElement.getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_A_TAG_IN_BOX_TITLE));

        return ShindenConstants.SHINDEN_URL + aElement.attr("href");
    }

    private static String acquireImage(Element animeElement) {
        Element section = animeElement.getElementsByTag("section")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_SECTION));

        Element imageElement = section.getElementsByTag("img")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_IMAGE));

        return ShindenConstants.SHINDEN_URL + imageElement.attr("src");
    }

    private static String acquireDescription(Element animeElement) {
        Element section = animeElement.getElementsByTag("section")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_SECTION));

        Element description = section.getElementsByClass("title-desc")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_DESCRIPTION));

        return description.text();

    }

    private static ShindenTitleRate acquireRate(Element animeElement) {
        Element section = animeElement.getElementsByTag("section")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_SECTION));

        Element statsElement = section.getElementsByClass("stats rate-stats")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_RATE_STATS));

        List<Element> stats = statsElement.getElementsByTag("span")
                .stream()
                .map(statElement ->
                        statElement.getElementsByTag("b")
                                .stream()
                                .findFirst()
                                .orElseThrow(
                                        JsoupParserException.of(SeasonErrorCode.NO_RATE_B_TAG))
                )
                .collect(Collectors.toList());

        ShindenTitleRate shindenTitleRate = new ShindenTitleRate();
        try {
            shindenTitleRate.setOverall(convertToRateValue(stats.get(0).text()));
            shindenTitleRate.setPlot(convertToRateValue(stats.get(1).text()));
            shindenTitleRate.setGraphics(convertToRateValue(stats.get(2).text()));
            shindenTitleRate.setMusic(convertToRateValue(stats.get(3).text()));
            shindenTitleRate.setCharacters(convertToRateValue(stats.get(4).text()));
        } catch (IndexOutOfBoundsException e) {
            throw new JsoupParserException(SeasonErrorCode.INDEX_OUT_OF_BOUND_FOR_RATE);
        }

        return shindenTitleRate.areAllNull() ? null : shindenTitleRate;
    }

    private static Integer acquireEpisodesNumber(Element animeElement) {
        Element footer = animeElement.getElementsByTag("footer")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_FOOTER));

        Element aTag = footer.getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_A_TAG_IN_FOOTER));
        String episode = Arrays.stream(aTag.text().split("ep"))
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_EPISODE_NUMBER));

        return NumberUtils.isCreatable(episode) ? Integer.valueOf(episode) : null;
    }

    private static Date acquireStartDate(Element animeElement) {
        Element footer = animeElement.getElementsByTag("footer")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_FOOTER));

        Element span = footer.getElementsByTag("span")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_SPAN_TAG_IN_FOOTER));

        String aired = span.attr("title");

        String rawStartDate = Arrays.stream(aired.split("-")).findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_START_DATE)).trim();

        String startDate = Arrays.stream(rawStartDate.split("Emitowane:")).reduce((first, last) -> last)
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_START_DATE)).trim();

        if (StringUtils.isEmpty(startDate)) {
            return null;
        }

        try {
            return getDateFormat(startDate).parse(startDate);
        } catch (ParseException e) {
            throw new JsoupParserException(SeasonErrorCode.INVALID_CONVERSION_TO_START_DATE);
        }
    }

    private static Date acquireEndDate(Element animeElement) {
        Element footer = animeElement.getElementsByTag("footer")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_FOOTER));

        Element span = footer.getElementsByTag("span")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_SPAN_TAG_IN_FOOTER));

        String aired = span.attr("title");

        String endDate = Arrays.stream(aired.split("-")).reduce((first, second) -> second)
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_END_DATE)).trim();

        if (StringUtils.isEmpty(endDate) || endDate.contains("Emitowane:")) {
            return null;
        }

        try {
            return getDateFormat(endDate).parse(endDate);
        } catch (ParseException e) {
            throw new JsoupParserException(SeasonErrorCode.INVALID_CONVERSION_TO_END_DATE);
        }
    }

    private static String acquireType(Element animeElement) {
        Element footer = animeElement.getElementsByTag("footer")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_FOOTER));

        Element span = footer.getElementsByTag("span")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_SPAN_TAG_IN_FOOTER));

        return span.text();
    }

    private static List<ShindenTitleGenre> acquireGenres(Element animeElement) {
        Optional<Element> genresElement = animeElement.getElementsByClass("genres").stream().findFirst();

        return genresElement.map(element -> element.getElementsByTag("li")
                        .stream()
                        .map(li -> {
                            Element aTag = li.getElementsByTag("a")
                                    .stream()
                                    .findFirst()
                                    .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_A_TAG_IN_GENRES));

                            var shindenTitleGenre = new ShindenTitleGenre();
                            String[] hrefs = Arrays.stream(aTag.attr("href")
                                            .split("/"))
                                    .reduce((first, last) -> last)
                                    .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_A_TAG_IN_GENRES))
                                    .trim()
                                    .split("-");

                            try {
                                shindenTitleGenre.setId(Integer.valueOf(hrefs[0]));
                                shindenTitleGenre.setName(hrefs[1]);
                                shindenTitleGenre.setText(aTag.text());
                            } catch (IndexOutOfBoundsException e) {
                                throw new JsoupParserException(SeasonErrorCode.INDEX_OUT_OF_BOUND_FOR_GENRES);
                            }

                            return shindenTitleGenre;
                        }).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

    }

    private static ShindenTitleStatistics acquireStatistics(Element animeElement) {
        Element footer = animeElement.getElementsByTag("footer")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_FOOTER));

        Element watchedStats = footer.getElementsByClass("wathed-stats")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_WATCHED_STATS));

        List<String> stats = watchedStats.getElementsByTag("li")
                .stream()
                .map(stat -> {
                    String title = stat.attr("title");
                    if (StringUtils.isEmpty(title)) {
                        Element aTag = stat.getElementsByTag("a").stream().findFirst()
                                .orElseThrow(JsoupParserException.of(SeasonErrorCode.NO_FAVORITES));
                        return aTag.attr("title");
                    }
                    return title;
                })
                .collect(Collectors.toList());

        ShindenTitleStatistics shindenTitleStatistics = new ShindenTitleStatistics();
        try {

            shindenTitleStatistics.setWatching(Integer.valueOf(stats.get(0).replace("Ogląda:", "").trim()));
            shindenTitleStatistics.setCompleted(Integer.valueOf(stats.get(1).replace("Obejrzało:", "").trim()));
            shindenTitleStatistics.setPlanToWatch(Integer.valueOf(stats.get(2).replace("Planuje:", "").trim()));
            shindenTitleStatistics.setDropped(Integer.valueOf(stats.get(3).replace("Porzuciło:", "").trim()));
            shindenTitleStatistics.setOnHold(Integer.valueOf(stats.get(4).replace("Wstrzymało:", "").trim()));
            shindenTitleStatistics.setFavorites(Integer.valueOf(stats.get(5)
                    .replace("Dodaj do ulubionych<br />W ulubionych ma:", "")
                    .replaceAll("\"", "")
                    .replace(" ", "").trim())
            );
        } catch (IndexOutOfBoundsException e) {
            throw new JsoupParserException(SeasonErrorCode.INDEX_OUT_OF_BOUND_FOR_STATS);
        }

        return shindenTitleStatistics;
    }

    private static Double convertToRateValue(String value) {
        String fixedLocale = replaceComma(value);
        return NumberUtils.isCreatable(fixedLocale) ? Double.valueOf(fixedLocale) : null;
    }

    private static SimpleDateFormat getDateFormat(String value) {
        String[] split = value.split("\\.");
        if (split.length == 3) {
            return dayMonthYearFormat;
        } else if (split.length == 2) {
            return monthYearFormat;
        }
        return yearFormat;
    }

    private static String replaceComma(String value) {
        return value.trim().replace(",", ".");
    }
}
