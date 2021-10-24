package pl.anime.scraper.domain.api.shinden.series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.anime.scraper.config.ShindenConstants;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenAccessKey;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenCharacter;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenRelatedSeries;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeries;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesCharacter;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesCharactersAndCrew;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesInfo;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesPerson;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesStatistics;
import pl.anime.scraper.domain.api.shinden.series.json.ShindenSeriesTag;
import pl.anime.scraper.domain.api.shinden.utils.ShindenUtils;
import pl.anime.scraper.utils.collections.MapUtils;
import pl.anime.scraper.utils.exception.JsoupParserException;

class ShindenSeriesParser {

    private static final String GENRES = "Gatunki:";
    private static final String TARGET_GROUP = "Grupy docelowe:";
    private static final String TYPE_OF_CHARACTERS = "Rodzaje postaci:";
    private static final String PLACES = "Miejsce i czas:";
    private static final String ARCHETYPE = "Pierwowz\u00F3r:";
    private static final String OTHER_TAGS = "Pozosta\u0142e tagi:";
    private static final String TYPE = "Typ:";
    private static final String STATUS = "Status:";
    private static final String START_DATE = "Data emisji:";
    private static final String END_DATE = "Koniec emisji:";
    private static final String EPISODES_NUMBER = "Liczba odcink\u00F3w:";
    private static final String STUDIO = "Studio:";
    private static final String EPISODE_LENGTH = "D\u0142ugo\u015B\u0107 odcinka:";
    private static final String MPAA = "MPAA:";
    private static final String WATCHING = "Ogl\u0105da";
    private static final String COMPLETED = "Obejrza\u0142o";
    private static final String SKIP = "Pomin\u0119\u0142o";
    private static final String ON_HOLD = "Wstrzyma\u0142o";
    private static final String DROPPED = "Porzuci\u0142o";
    private static final String PLAN_TO_WATCH = "Planuje";
    private static final String FAVOURITES = "Lubi";

    private static final SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM.yyyy");
    private static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");


    static ShindenSeries parseToShindenSeries(Document document) {
        var shindenSeries = new ShindenSeries();
        shindenSeries.setTitle(acquireTitle(document));
        shindenSeries.setAlternateTitles(acquireAlternateTitles(document));
        shindenSeries.setSmallImage(acquireSmallImage(document));
        shindenSeries.setImage(acquireImage(document));
        shindenSeries.setDescription(acquireDescription(document));
        shindenSeries.setInfo(acquireInfo(document));
        shindenSeries.setStatistics(acquireStatistics(document));
        shindenSeries.setCharacters(acquireCharacters(document));
        shindenSeries.setCrew(acquireCrew(document));
        shindenSeries.setRelatedSeries(acquireRelatedSeries(document));
        return shindenSeries;
    }

    static ShindenSeriesCharactersAndCrew parseToShindenSeriesCharactersAndCrew(Document document) {
        var shindenSeriesCharactersAndCrew = new ShindenSeriesCharactersAndCrew();
        shindenSeriesCharactersAndCrew.setCharacters(acquireCharacters(document));
        shindenSeriesCharactersAndCrew.setCrew(acquireCrew(document));
        return shindenSeriesCharactersAndCrew;
    }

    private static List<ShindenSeriesCharacter> acquireCharacters(Document document) {
        var charactersListElement = document.getElementsByClass("ch-st-list")
                .stream()
                .findFirst();

        if (charactersListElement.isEmpty()) {
            return null;
        }

        var characters = charactersListElement.get().getElementsByClass("ch-st-item");

        return characters.stream()
                .map(character -> {
                    var itemL = character.getElementsByClass("item-l")
                            .stream()
                            .findFirst()
                            .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_ITEM_L_IN_CH_ST_ITEM));
                    var itemsR = character.getElementsByClass("item-r");

                    var shindenSeriesCharacter = new ShindenSeriesCharacter();
                    shindenSeriesCharacter.setCharacter(createShindenCharacterFromItem(itemL, false));
                    shindenSeriesCharacter.setCast(createCharacterCastFromItemR(itemsR));
                    return shindenSeriesCharacter;
                })
                .collect(Collectors.toList());
    }

    private static List<ShindenSeriesPerson> acquireCrew(Document document) {
        var personListElement = document.getElementsByClass("person-list")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_PERSON_LIST));

        return personListElement.getElementsByClass("person-character-item")
                .stream()
                .map(item -> {
                    var personElement = item.getElementsByTag("span")
                            .stream()
                            .findFirst()
                            .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_PERSON_SPAN_TAG));

                    var imgElement = personElement.getElementsByTag("img")
                            .stream()
                            .findFirst()
                            .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_PERSON_IMAGE));

                    var aElement = personElement.getElementsByTag("a")
                            .stream()
                            .findFirst()
                            .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_PERSON_A_TAG));

                    var pElement = personElement.getElementsByTag("p")
                            .stream()
                            .findFirst()
                            .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_PERSON_P_TAG));

                    var personName = aElement.text();
                    var personNameSplit = personName.split(",");
                    var isOnlyName = personNameSplit.length == 1;
                    var idAndName = ShindenUtils.acquireIdAndNameFromHref(aElement.attr("href"));
                    var shindenSeriesPerson = new ShindenSeriesPerson();
                    shindenSeriesPerson.setKey(new ShindenAccessKey(idAndName.getId(), idAndName.getName()));
                    shindenSeriesPerson.setName(isOnlyName ? personName : null);
                    shindenSeriesPerson.setFirstName(
                            isOnlyName ? null : String.join(",", Arrays.stream(personNameSplit)
                                    .map(String::trim)
                                    .collect(Collectors.toList())
                                    .subList(1, personNameSplit.length)));
                    shindenSeriesPerson.setLastName(isOnlyName ? null : personNameSplit[0]);
                    shindenSeriesPerson.setRole(pElement.text().trim());
                    shindenSeriesPerson.setSmallImage(ShindenConstants.SHINDEN_URL + imgElement.attr("src"));
                    shindenSeriesPerson.setImage(ShindenConstants.SHINDEN_URL + imgElement.attr("src")
                            .replace("36x48", "225x350")
                    );
                    return shindenSeriesPerson;
                })
                .collect(Collectors.toList());
    }

    private static ShindenCharacter createShindenCharacterFromItem(Element item, boolean isItemR) {
        var aTagElement = item.getElementsByTag("a")
                .stream()
                .reduce((first, last) -> isItemR ? last : first)

                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_A_TAG_IN_ITEM_L));
        var imageElement = aTagElement.getElementsByTag("img")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_IMG_TAG_IN_ITEM_L));

        var pTxtElement = item.getElementsByClass("p-txt")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_P_TXT_IN_ITEM_L));
        var pTxtATagElement = pTxtElement.getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_P_TXT_A_TAG_IN_ITEM_L));
        var characterRole = pTxtElement.getElementsByTag("p")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_CHARACTER_ROLE));
        var characterName = pTxtATagElement.text();
        var splitCharacterName = characterName.split(",");
        var isOnlyName = splitCharacterName.length == 1;
        var idAndName = ShindenUtils.acquireIdAndNameFromHref(aTagElement.attr("href"));
        var shindenCharacter = new ShindenCharacter();
        shindenCharacter.setKey(new ShindenAccessKey(idAndName.getId(), idAndName.getName()));
        shindenCharacter.setSmallImage(ShindenConstants.SHINDEN_URL + imageElement.attr("src"));
        shindenCharacter.setImage(ShindenConstants.SHINDEN_URL + imageElement.attr("src").replace("36x48", "225x350"));
        shindenCharacter.setName(isOnlyName ? characterName.trim() : null);
        shindenCharacter.setFirstName(isOnlyName ? null : String.join(",", Arrays.stream(splitCharacterName)
                .map(String::trim)
                .collect(Collectors.toList())
                .subList(1, splitCharacterName.length)));
        shindenCharacter.setLastName(isOnlyName ? null : splitCharacterName[0].trim());
        shindenCharacter.setRole(characterRole.text());
        return shindenCharacter;
    }

    private static Map<String, ShindenSeriesPerson> createCharacterCastFromItemR(Elements itemsR) {
        var indexIterator = IndexIterator.startFrom(0);
        return itemsR.stream()
                .map(itemR -> {
                    var character = createShindenCharacterFromItem(itemR, true);

                    var shindenSeriesPerson = new ShindenSeriesPerson();
                    shindenSeriesPerson.setKey(character.getKey());
                    shindenSeriesPerson.setName(character.getName());
                    shindenSeriesPerson.setFirstName(character.getFirstName());
                    shindenSeriesPerson.setLastName(character.getLastName());
                    shindenSeriesPerson.setImage(character.getImage());
                    shindenSeriesPerson.setSmallImage(character.getSmallImage());
                    if (StringUtils.isBlank(character.getRole())) {
                        return Map.entry("undefined_language_" + indexIterator.next(), shindenSeriesPerson);
                    }
                    return Map.entry(character.getRole(), shindenSeriesPerson);
                })
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
    }


    private static List<ShindenRelatedSeries> acquireRelatedSeries(Document document) {
        var figureListElement = document.getElementsByClass("figure-list")
                .stream()
                .findFirst();

        if (figureListElement.isEmpty()) {
            return null;
        }

        var relation_t2tElements = figureListElement.get().getElementsByClass("relation_t2t");

        return relation_t2tElements.stream()
                .map(relationElement -> {
                    var figcaption = relationElement.getElementsByTag("figcaption");
                    var aTagTitle = figcaption.get(0).getElementsByTag("a")
                            .stream()
                            .findFirst()
                            .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_RELATED_SERIES_TITLE));

                    var seriesType = figcaption.get(1).text().trim();
                    var relationType = figcaption.get(2).text().replace("- ", "").trim();
                    var imgElement = relationElement.getElementsByTag("img")
                            .stream().findFirst()
                            .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_RELATED_SERIES_IMAGE));

                    var idAndName = ShindenUtils.acquireIdAndNameFromHref(aTagTitle.attr("href"));
                    var shindenRelatedSeries = new ShindenRelatedSeries();
                    shindenRelatedSeries.setKey(new ShindenAccessKey(idAndName.getId(), idAndName.getName()));
                    shindenRelatedSeries.setSeriesType(seriesType);
                    shindenRelatedSeries.setSmallImage(ShindenConstants.SHINDEN_URL + imgElement.attr("src"));
                    shindenRelatedSeries.setImage(
                            ShindenConstants.SHINDEN_URL + imgElement.attr("src").replace("100x100", "225x350"));
                    shindenRelatedSeries.setRelationType(relationType);
                    shindenRelatedSeries.setTitle(aTagTitle.attr("title").trim());
                    return shindenRelatedSeries;

                })
                .collect(Collectors.toList());
    }

    private static String acquireTitle(Document document) {
        var pageTitle = document.getElementsByClass("page-title")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_PAGE_TITLE));

        return pageTitle.text().replace("Anime:", "").trim();
    }

    private static List<String> acquireAlternateTitles(Document document) {
        var alternateTitles = document.getElementsByClass("title-other").stream().findFirst();
        return alternateTitles.map(titles -> Stream.of(titles.text().split(","))
                        .map(String::trim)
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private static String acquireSmallImage(Document document) {
        var titleCoverElement = document.getElementsByClass("title-cover")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_TITLE_COVER));

        var aTag = titleCoverElement.getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_IMAGE_A_TAG));

        var imgTag = aTag.getElementsByTag("img")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_IMAGE_IMG_TAG));

        return ShindenConstants.SHINDEN_URL + imgTag.attr("src");
    }

    private static String acquireImage(Document document) {
        var titleCoverElement = document.getElementsByClass("title-cover")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_TITLE_COVER));

        var aTag = titleCoverElement.getElementsByTag("a")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_IMAGE_A_TAG));

        return ShindenConstants.SHINDEN_URL + aTag.attr("href");

    }

    private static String acquireDescription(Document document) {
        var descriptionElement = document.getElementById("description");
        return Optional.ofNullable(descriptionElement)
                .map(desc -> desc.getElementsByTag("p")
                        .stream()
                        .findFirst()
                        .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_DESCRIPTION_P_TAG))
                        .text()
                        .replaceAll("\n", ""))
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static ShindenSeriesInfo acquireInfo(Document document) {
        var infoTBodyElement = document.getElementsByClass("info-top-table-highlight")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_INFO_TABLE));
        var shindenTags = acquireInfoTags(infoTBodyElement);
        var titleSmallInfo = acquireTitleSmallInfo(document);
        var info = new ShindenSeriesInfo();
        info.setGenres(shindenTags.get(GENRES));
        info.setTargetGroup(shindenTags.get(TARGET_GROUP));
        info.setTypesOfCharacters(shindenTags.get(TYPE_OF_CHARACTERS));
        info.setPlaces(shindenTags.get(PLACES));
        info.setArchetype(shindenTags.get(ARCHETYPE));
        info.setOtherTags(shindenTags.get(OTHER_TAGS));
        info.setType((String) titleSmallInfo.get(TYPE));
        info.setStatus((String) titleSmallInfo.get(STATUS));
        info.setStartDate((Date) titleSmallInfo.get(START_DATE));
        info.setEndDate((Date) titleSmallInfo.get(END_DATE));
        info.setEpisodes((Integer) titleSmallInfo.get(EPISODES_NUMBER));
        info.setStudios((List<ShindenSeriesTag>) titleSmallInfo.get(STUDIO));
        info.setEpisodeLength((String) titleSmallInfo.get(EPISODE_LENGTH));
        info.setMpaa((String) titleSmallInfo.get(MPAA));
        return info;
    }

    private static ShindenSeriesStatistics acquireStatistics(Document document) {
        var titleStatsMap = acquireTitleStats(document);
        var statistics = new ShindenSeriesStatistics();
        statistics.setWatching(titleStatsMap.get(WATCHING));
        statistics.setCompleted(titleStatsMap.get(COMPLETED));
        statistics.setDropped(titleStatsMap.get(DROPPED));
        statistics.setFavorites(titleStatsMap.get(FAVOURITES));
        statistics.setOnHold(titleStatsMap.get(ON_HOLD));
        statistics.setSkip(titleStatsMap.get(SKIP));
        statistics.setPlanToWatch(titleStatsMap.get(PLAN_TO_WATCH));
        return statistics.areAllNull() ? null : statistics;

    }

    private static Map<String, List<ShindenSeriesTag>> acquireInfoTags(Element infoTBodyElement) {
        return infoTBodyElement.getElementsByTag("tr")
                .stream()
                .map(tr -> {
                    var tdElements = tr.getElementsByTag("td");
                    var liTags = tdElements.get(1).getElementsByTag("li");
                    var tags = liTags.stream()
                            .map(li -> {
                                var aTag = li.getElementsByTag("a")
                                        .stream()
                                        .findFirst()
                                        .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_A_TAG_IN_TAGS));
                                return ShindenUtils.createTagFromHtmlATag(aTag);
                            })
                            .collect(Collectors.toList());
                    return new SimpleEntry<>(tdElements.get(0).text().trim(), tags);
                })
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private static Map<String, Object> acquireTitleSmallInfo(Document document) {
        var titleSmallInfo = document.getElementsByClass("title-small-info")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_TITLE_SMALL_INFO));
        var dlElement = titleSmallInfo.getElementsByTag("dl")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_DL_IN_TITLE_SMALL_INFO));

        var elements = createElementsFromDlElement(dlElement);

        Map<String, Object> result = new HashMap<>();
        MapUtils.putIfNotNull(result, TYPE, acquireElementString(elements.get(TYPE)));
        MapUtils.putIfNotNull(result, STATUS, acquireElementString(elements.get(STATUS)));
        MapUtils.putIfNotNull(result, START_DATE, acquireElementDate(elements.get(START_DATE)));
        MapUtils.putIfNotNull(result, END_DATE, acquireElementDate(elements.get(END_DATE)));
        MapUtils.putIfNotNull(result, EPISODES_NUMBER, acquireElementInteger(elements.get(EPISODES_NUMBER)));
        MapUtils.putIfNotNull(result, STUDIO, acquireElementTags(elements.get(STUDIO)));
        MapUtils.putIfNotNull(result, EPISODE_LENGTH, acquireElementString(elements.get(EPISODE_LENGTH)));
        MapUtils.putIfNotNull(result, MPAA, acquireElementString(elements.get(MPAA)));

        return result;

    }

    private static Map<String, Integer> acquireTitleStats(Document document) {
        var titleStats = document.getElementsByClass("title-stats")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_TITLE_STATS));

        var dlElement = titleStats.getElementsByTag("dl")
                .stream()
                .findFirst()
                .orElseThrow(JsoupParserException.of(SeriesErrorCode.NO_DL_IN_TITLE_STATS));

        var elements = createElementsFromDlElement(dlElement);

        Map<String, Integer> result = new HashMap<>();
        MapUtils.putIfNotNull(result, WATCHING, acquireElementInteger(elements.get(WATCHING)));
        MapUtils.putIfNotNull(result, COMPLETED, acquireElementInteger(elements.get(COMPLETED)));
        MapUtils.putIfNotNull(result, SKIP, acquireElementInteger(elements.get(SKIP)));
        MapUtils.putIfNotNull(result, ON_HOLD, acquireElementInteger(elements.get(ON_HOLD)));
        MapUtils.putIfNotNull(result, DROPPED, acquireElementInteger(elements.get(DROPPED)));
        MapUtils.putIfNotNull(result, PLAN_TO_WATCH, acquireElementInteger(elements.get(DROPPED)));
        MapUtils.putIfNotNull(result, FAVOURITES, acquireElementInteger(elements.get(DROPPED)));
        return result;
    }

    private static Map<String, Element> createElementsFromDlElement(Element dlElement) {
        var dtElements = dlElement.getElementsByTag("dt");
        var ddElements = dlElement.getElementsByTag("dd");

        Map<String, Element> elements = new LinkedHashMap<>();
        for (int i = 0; i < dtElements.size(); i++) {
            elements.put(dtElements.get(i).text().trim(), ddElements.get(i));
        }
        return elements;
    }

    private static String acquireElementString(Element element) {
        return element != null ? element.text().replaceAll("\"", "").trim() : null;
    }

    private static Date acquireElementDate(Element element) {
        var date = acquireElementString(element);
        if (date == null) {
            return null;
        }
        try {
            return getDateFormat(date).parse(date);
        } catch (ParseException e) {
            throw new JsoupParserException(SeriesErrorCode.INVALID_PARSE_TO_DATE);
        }
    }

    private static Integer acquireElementInteger(Element element) {
        if (element == null) {
            return null;
        }
        return NumberUtils.isCreatable(element.text()) ? Integer.valueOf(element.text()) : null;
    }

    private static List<ShindenSeriesTag> acquireElementTags(Element element) {
        if (element == null) {
            return null;
        }
        var aTags = element.getElementsByTag("a");
        return aTags.stream()
                .map(ShindenUtils::createTagFromHtmlATag)
                .collect(Collectors.toList());
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

    @Getter(AccessLevel.PACKAGE)
    private static class IndexIterator {

        private int index;

        public IndexIterator(int index) {
            this.index = index;
        }

        public static IndexIterator startFrom(int startIndex) {
            return new IndexIterator(startIndex);
        }

        int next() {
            index += 1;
            return index;
        }
    }
}
