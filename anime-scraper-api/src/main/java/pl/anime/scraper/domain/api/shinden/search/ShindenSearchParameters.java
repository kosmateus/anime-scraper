package pl.anime.scraper.domain.api.shinden.search;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenArchetype;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenDatePrecision;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenEpisodeLength;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenEpisodesNumber;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenGenres;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenGenresSearchType;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenLetter;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenOtherTags;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenPlaceAndTime;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenSeriesStatus;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenSeriesType;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenTagParam;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenTargetGroups;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenTypeOfCharacters;

@Getter(AccessLevel.PACKAGE)
public class ShindenSearchParameters {

    private final String searchText;
    private final Set<ShindenGenres> includedGenres;
    private final Set<ShindenGenres> excludedGenres;
    private final Set<ShindenTargetGroups> includedTargetGroups;
    private final Set<ShindenTargetGroups> excludedTargetGroups;
    private final Set<ShindenTypeOfCharacters> includedTypeOfCharacters;
    private final Set<ShindenTypeOfCharacters> excludedTypeOfCharacters;
    private final Set<ShindenPlaceAndTime> includedPlacesAndTimes;
    private final Set<ShindenPlaceAndTime> excludedPlacesAndTimes;
    private final Set<ShindenOtherTags> includedOtherTags;
    private final Set<ShindenOtherTags> excludedOtherTags;
    private final Set<ShindenArchetype> includedArchetypes;
    private final Set<ShindenArchetype> excludedArchetypes;
    private final Set<ShindenSeriesType> seriesTypes;
    private final Set<ShindenEpisodeLength> episodeLengths;
    private final Set<ShindenEpisodesNumber> episodesNumbers;
    private final Set<ShindenSeriesStatus> seriesStatuses;
    private final LocalDate from;
    private final LocalDate to;
    private final ShindenDatePrecision startDatePrecision;
    private final Integer fromEpisodeNumber;
    private final Integer toEpisodeNumber;
    private final boolean onlineEpisodes;
    private final boolean excludeSeriesOnMyList;
    private final boolean excludeSeriesOnMyCompletedList;
    private final ShindenGenresSearchType genresSearchType;
    private final ShindenLetter letter;
    private final Integer page;

    private ShindenSearchParameters(Builder builder) {
        this.searchText = builder.searchText;
        this.includedGenres = builder.includedGenres;
        this.excludedGenres = builder.excludedGenres;
        this.includedTargetGroups = builder.includedTargetGroups;
        this.excludedTargetGroups = builder.excludedTargetGroups;
        this.includedTypeOfCharacters = builder.includedTypeOfCharacters;
        this.excludedTypeOfCharacters = builder.excludedTypeOfCharacters;
        this.includedPlacesAndTimes = builder.includedPlacesAndTimes;
        this.excludedPlacesAndTimes = builder.excludedPlacesAndTimes;
        this.includedOtherTags = builder.includedOtherTags;
        this.excludedOtherTags = builder.excludedOtherTags;
        this.includedArchetypes = builder.includedArchetypes;
        this.excludedArchetypes = builder.excludedArchetypes;
        this.from = builder.from;
        this.to = builder.to;
        this.startDatePrecision = builder.datePrecision;
        this.seriesTypes = builder.seriesTypes;
        this.episodeLengths = builder.episodeLengths;
        this.episodesNumbers = builder.episodesNumbers;
        this.seriesStatuses = builder.seriesStatuses;
        this.fromEpisodeNumber = builder.fromEpisodeNumber;
        this.toEpisodeNumber = builder.toEpisodeNumber;
        this.onlineEpisodes = builder.onlineEpisodes;
        this.excludeSeriesOnMyList = builder.excludeSeriesOnMyList;
        this.excludeSeriesOnMyCompletedList = builder.excludeSeriesOnMyCompletedList;
        this.genresSearchType = builder.genresSearchType;
        this.letter = builder.letter;
        this.page = builder.page;
    }

    public static Builder builder() {
        return Builder.builder();
    }

    public Builder toBuilder() {
        return Builder.builder(this);
    }

    String getSearchQueryParams() {
        StringBuilder sb = new StringBuilder("type=contains");
        sb.append("&search=");
        if (searchText != null) {
            sb.append(searchText);
        }

        sb.append("&genres=");

        addIncluded(includedGenres, sb);
        addExcluded(excludedGenres, sb);

        addIncluded(includedTargetGroups, sb);
        addExcluded(excludedTargetGroups, sb);

        addIncluded(includedTypeOfCharacters, sb);
        addExcluded(excludedTypeOfCharacters, sb);

        addIncluded(includedPlacesAndTimes, sb);
        addExcluded(excludedPlacesAndTimes, sb);

        addIncluded(includedOtherTags, sb);
        addExcluded(excludedOtherTags, sb);

        sb.append("&genres-type=");
        sb.append(genresSearchType.getValue());

        addFrom(sb);
        addTo(sb);

        sb.append("&start_date_precision=");
        sb.append(startDatePrecision.getValue());

        seriesTypes.forEach(st -> sb.append("&series_type[]=").append(st.getValue()));

        seriesStatuses.forEach(ss -> sb.append("&series_status[]=").append(ss.getValue()));

        episodeLengths.forEach(el -> sb.append("&series_length[]=").append(el.getValue()));

        episodesNumbers.forEach(en -> sb.append("&series_number[]=").append(en.getValue()));

        if (fromEpisodeNumber != null) {
            sb.append("&series_number_from=");
            sb.append(fromEpisodeNumber);
        }
        if (toEpisodeNumber != null) {
            sb.append("&series_number_to=");
            sb.append(toEpisodeNumber);
        }

        if (onlineEpisodes) {
            sb.append("&one_online=true");
        }

        if (excludeSeriesOnMyList) {
            sb.append("&not_on_list=true");
        }

        if (excludeSeriesOnMyCompletedList) {
            sb.append("not_saw=true");
        }

        if (letter != null) {
            sb.append("&letter=");
            sb.append(letter.getValue());
        }

        if (page != null) {
            sb.append("&page=");
            sb.append(page);
        }

        return sb.toString();
    }

    private <T extends ShindenTagParam> void addIncluded(Set<T> included, StringBuilder sb) {
        included.forEach(in -> sb.append(";i").append(in.getValue()));
    }

    private <T extends ShindenTagParam> void addExcluded(Set<T> included, StringBuilder sb) {
        included.forEach(in -> sb.append(";e").append(in.getValue()));
    }

    private void addFrom(StringBuilder sb) {
        if (from != null) {
            sb.append("&year_from=");
            sb.append(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(from));
        }
    }

    private void addTo(StringBuilder sb) {
        if (to != null) {
            sb.append("&year_to=");
            sb.append(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(to));
        }
    }


    public static final class Builder {

        private final Set<ShindenGenres> includedGenres;
        private final Set<ShindenGenres> excludedGenres;
        private final Set<ShindenTargetGroups> includedTargetGroups;
        private final Set<ShindenTargetGroups> excludedTargetGroups;
        private final Set<ShindenTypeOfCharacters> includedTypeOfCharacters;
        private final Set<ShindenTypeOfCharacters> excludedTypeOfCharacters;
        private final Set<ShindenPlaceAndTime> includedPlacesAndTimes;
        private final Set<ShindenPlaceAndTime> excludedPlacesAndTimes;
        private final Set<ShindenOtherTags> includedOtherTags;
        private final Set<ShindenOtherTags> excludedOtherTags;
        private final Set<ShindenArchetype> includedArchetypes;
        private final Set<ShindenArchetype> excludedArchetypes;
        private final Set<ShindenSeriesStatus> seriesStatuses;
        private Set<ShindenSeriesType> seriesTypes;
        private Set<ShindenEpisodeLength> episodeLengths;
        private Set<ShindenEpisodesNumber> episodesNumbers;
        private Integer fromEpisodeNumber;
        private Integer toEpisodeNumber;
        private String searchText;
        private LocalDate from;
        private LocalDate to;
        private ShindenDatePrecision datePrecision;
        private boolean onlineEpisodes;
        private boolean excludeSeriesOnMyList;
        private boolean excludeSeriesOnMyCompletedList;
        private ShindenGenresSearchType genresSearchType;
        private ShindenLetter letter;
        private Integer page;

        private Builder() {
            includedGenres = new HashSet<>();
            excludedGenres = new HashSet<>();
            includedTargetGroups = new HashSet<>();
            excludedTargetGroups = new HashSet<>();
            includedTypeOfCharacters = new HashSet<>();
            excludedTypeOfCharacters = new HashSet<>();
            includedPlacesAndTimes = new HashSet<>();
            excludedPlacesAndTimes = new HashSet<>();
            includedOtherTags = new HashSet<>();
            excludedOtherTags = new HashSet<>();
            includedArchetypes = new HashSet<>();
            excludedArchetypes = new HashSet<>();
            from = null;
            to = null;
            datePrecision = ShindenDatePrecision.DAY_MONTH_YEAR;
            seriesTypes = new HashSet<>(
                    List.of(
                            ShindenSeriesType.TV,
                            ShindenSeriesType.ONA,
                            ShindenSeriesType.OVA,
                            ShindenSeriesType.MOVIE,
                            ShindenSeriesType.SPECIAL,
                            ShindenSeriesType.MUSIC
                    )
            );

            episodeLengths = new HashSet<>(
                    List.of(
                            ShindenEpisodeLength.LESS_THAN_7_MINUTES,
                            ShindenEpisodeLength.FROM_7_TO_18_MINUTES,
                            ShindenEpisodeLength.FROM_19_TO_27_MINUTES,
                            ShindenEpisodeLength.FROM_28_TO_48_MINUTES,
                            ShindenEpisodeLength.MORE_THAN_48_MINUTES
                    )
            );

            episodesNumbers = new HashSet<>(
                    List.of(
                            ShindenEpisodesNumber.ONLY_ONE_EPISODE,
                            ShindenEpisodesNumber.FROM_2_TO_14_EPISODES,
                            ShindenEpisodesNumber.FROM_15_TO_28_EPISODES,
                            ShindenEpisodesNumber.FROM_29_TO_100_EPISODES,
                            ShindenEpisodesNumber.MORE_THAN_100_EPISODES
                    )
            );

            seriesStatuses = new HashSet<>(
                    List.of(
                            ShindenSeriesStatus.DECLARATION,
                            ShindenSeriesStatus.ANNOUNCEMENT,
                            ShindenSeriesStatus.CURRENTLY_AIRING,
                            ShindenSeriesStatus.FINISHED
                    )
            );

            fromEpisodeNumber = null;
            toEpisodeNumber = null;

            genresSearchType = ShindenGenresSearchType.HAS_TO_HAVE_ALL;

            letter = null;
            page = null;
        }

        private Builder(ShindenSearchParameters parameters) {
            includedGenres = parameters.getIncludedGenres();
            excludedGenres = parameters.getExcludedGenres();
            includedTargetGroups = parameters.getIncludedTargetGroups();
            excludedTargetGroups = parameters.getExcludedTargetGroups();
            includedTypeOfCharacters = parameters.getIncludedTypeOfCharacters();
            excludedTypeOfCharacters = parameters.getExcludedTypeOfCharacters();
            includedPlacesAndTimes = parameters.getIncludedPlacesAndTimes();
            excludedPlacesAndTimes = parameters.getExcludedPlacesAndTimes();
            includedOtherTags = parameters.getIncludedOtherTags();
            excludedOtherTags = parameters.getExcludedOtherTags();
            includedArchetypes = parameters.getIncludedArchetypes();
            excludedArchetypes = parameters.getExcludedArchetypes();
            seriesStatuses = parameters.getSeriesStatuses();
            seriesTypes = parameters.getSeriesTypes();
            episodeLengths = parameters.getEpisodeLengths();
            episodesNumbers = parameters.getEpisodesNumbers();
            fromEpisodeNumber = parameters.getFromEpisodeNumber();
            toEpisodeNumber = parameters.getToEpisodeNumber();
            searchText = parameters.getSearchText();
            from = parameters.getFrom();
            to = parameters.getTo();
            datePrecision = parameters.getStartDatePrecision();
            onlineEpisodes = parameters.isOnlineEpisodes();
            excludeSeriesOnMyList = parameters.isExcludeSeriesOnMyList();
            excludeSeriesOnMyCompletedList = parameters.isExcludeSeriesOnMyCompletedList();
            genresSearchType = parameters.getGenresSearchType();
            letter = parameters.getLetter();
            page = parameters.getPage();
        }

        public static Builder builder() {
            return new Builder();
        }

        private static Builder builder(ShindenSearchParameters parameters) {
            return new Builder(parameters);
        }

        public Builder searchText(String searchText) {
            this.searchText = searchText;
            return this;
        }

        public Builder includeGenre(ShindenGenres genre) {
            includedGenres.add(genre);
            excludedGenres.remove(genre);
            return this;
        }

        public Builder includeGenres(ShindenGenres... genres) {
            includedGenres.addAll(Arrays.asList(genres));
            Arrays.asList(genres).forEach(includedGenres::remove);
            return this;
        }

        public Builder excludeGenre(ShindenGenres genre) {
            excludedGenres.add(genre);
            includedGenres.remove(genre);
            return this;
        }

        public Builder excludeGenres(ShindenGenres... genres) {
            excludedGenres.addAll(Arrays.asList(genres));
            Arrays.asList(genres).forEach(includedGenres::remove);
            return this;
        }

        public Builder includeTargetGroup(ShindenTargetGroups targetGroup) {
            includedTargetGroups.add(targetGroup);
            excludedTargetGroups.remove(targetGroup);
            return this;
        }

        public Builder includeTargetGroups(ShindenTargetGroups... targetGroups) {
            includedTargetGroups.addAll(Arrays.asList(targetGroups));
            Arrays.asList(targetGroups).forEach(excludedTargetGroups::remove);
            return this;
        }

        public Builder excludeTargetGroup(ShindenTargetGroups targetGroups) {
            excludedTargetGroups.add(targetGroups);
            includedTargetGroups.remove(targetGroups);
            return this;
        }

        public Builder excludeTargetGroups(ShindenTargetGroups... targetGroups) {
            excludedTargetGroups.addAll(Arrays.asList(targetGroups));
            Arrays.asList(targetGroups).forEach(includedTargetGroups::remove);
            return this;
        }

        public Builder includeTypeOfCharacter(ShindenTypeOfCharacters typeOfCharacter) {
            includedTypeOfCharacters.add(typeOfCharacter);
            excludedTypeOfCharacters.remove(typeOfCharacter);
            return this;
        }

        public Builder includeTypeOfCharacters(ShindenTypeOfCharacters... typeOfCharacters) {
            includedTypeOfCharacters.addAll(Arrays.asList(typeOfCharacters));
            Arrays.asList(typeOfCharacters).forEach(excludedTypeOfCharacters::remove);
            return this;
        }

        public Builder excludeTypeOfCharacter(ShindenTypeOfCharacters typeOfCharacter) {
            excludedTypeOfCharacters.add(typeOfCharacter);
            includedTypeOfCharacters.remove(typeOfCharacter);
            return this;
        }

        public Builder excludeTypeOfCharacters(ShindenTypeOfCharacters... typeOfCharacters) {
            excludedTypeOfCharacters.addAll(Arrays.asList(typeOfCharacters));
            Arrays.asList(typeOfCharacters).forEach(includedTypeOfCharacters::remove);
            return this;
        }

        public Builder includePlaceAndTime(ShindenPlaceAndTime placeAndTime) {
            includedPlacesAndTimes.add(placeAndTime);
            excludedPlacesAndTimes.remove(placeAndTime);
            return this;
        }

        public Builder exludePlaceAndTime(ShindenPlaceAndTime placeAndTime) {
            excludedPlacesAndTimes.add(placeAndTime);
            includedPlacesAndTimes.remove(placeAndTime);
            return this;
        }

        public Builder includePlacesAndTimes(ShindenPlaceAndTime... placesAndTimes) {
            includedPlacesAndTimes.addAll(Arrays.asList(placesAndTimes));
            Arrays.asList(placesAndTimes).forEach(excludedPlacesAndTimes::remove);
            return this;
        }

        public Builder exludePlacesAndTimes(ShindenPlaceAndTime... placesAndTimes) {
            excludedPlacesAndTimes.addAll(Arrays.asList(placesAndTimes));
            Arrays.asList(placesAndTimes).forEach(includedPlacesAndTimes::remove);
            return this;
        }

        public Builder includeOtherTag(ShindenOtherTags otherTag) {
            includedOtherTags.add(otherTag);
            excludedOtherTags.remove(otherTag);
            return this;
        }

        public Builder excludeOtherTag(ShindenOtherTags otherTag) {
            excludedOtherTags.add(otherTag);
            includedOtherTags.remove(otherTag);
            return this;
        }

        public Builder includeOtherTags(ShindenOtherTags... otherTags) {
            includedOtherTags.addAll(Arrays.asList(otherTags));
            Arrays.asList(otherTags).forEach(excludedOtherTags::remove);
            return this;
        }

        public Builder excludeOtherTags(ShindenOtherTags... otherTags) {
            excludedOtherTags.addAll(Arrays.asList(otherTags));
            Arrays.asList(otherTags).forEach(includedOtherTags::remove);
            return this;
        }

        public Builder includeArchetype(ShindenArchetype archetype) {
            includedArchetypes.add(archetype);
            excludedArchetypes.remove(archetype);
            return this;
        }

        public Builder excludeArchetype(ShindenArchetype archetype) {
            excludedArchetypes.add(archetype);
            includedArchetypes.remove(archetype);
            return this;
        }

        public Builder includeArchetypes(ShindenArchetype... archetypes) {
            includedArchetypes.addAll(Arrays.asList(archetypes));
            Arrays.asList(archetypes).forEach(excludedArchetypes::remove);
            return this;
        }

        public Builder excludeArchetypes(ShindenArchetype... archetypes) {
            excludedArchetypes.addAll(Arrays.asList(archetypes));
            Arrays.asList(archetypes).forEach(includedArchetypes::remove);
            return this;
        }

        public Builder from(int day, int month, int year) {
            from = LocalDate.of(year, month, day);
            return this;
        }

        public Builder from(int month, int year) {
            from = LocalDate.of(year, month, 1);
            return this;
        }

        public Builder from(int year) {
            from = LocalDate.of(year, 1, 1);
            return this;
        }

        public Builder to(int day, int month, int year) {
            to = LocalDate.of(year, month, day);
            return this;
        }

        public Builder to(int month, int year) {
            to = LocalDate.of(year, month, 1);
            return this;
        }

        public Builder to(int year) {
            to = LocalDate.of(year, 1, 1);
            return this;
        }

        public Builder datePrecision(ShindenDatePrecision precision) {
            this.datePrecision = precision;
            return this;
        }

        public Builder includeSeriesType(ShindenSeriesType type) {
            this.seriesTypes.add(type);
            return this;
        }

        public Builder includeSeriesTypes(ShindenSeriesType... types) {
            this.seriesTypes.addAll(List.of(types));
            return this;
        }

        public Builder excludeSeriesType(ShindenSeriesType type) {
            this.seriesTypes.remove(type);
            return this;
        }

        public Builder excludeSeriesTypes(ShindenSeriesType... types) {
            List.of(types).forEach(this.seriesTypes::remove);
            return this;
        }

        public Builder includeSeriesStatus(ShindenSeriesStatus status) {
            this.seriesStatuses.add(status);
            return this;
        }

        public Builder includeSeriesStatuses(ShindenSeriesStatus... statuses) {
            this.seriesStatuses.addAll(List.of(statuses));
            return this;
        }

        public Builder excludeSeriesStatus(ShindenSeriesStatus status) {
            this.seriesStatuses.remove(status);
            return this;
        }

        public Builder excludeSeriesStatuses(ShindenSeriesStatus... statuses) {
            List.of(statuses).forEach(this.seriesStatuses::remove);
            return this;
        }

        public Builder includeEpisodeLength(ShindenEpisodeLength episodeLength) {
            this.episodeLengths.add(episodeLength);
            return this;
        }

        public Builder includeEpisodeLengths(ShindenEpisodeLength... episodeLengths) {
            this.episodeLengths.addAll(List.of(episodeLengths));
            return this;
        }

        public Builder excludeEpisodeLength(ShindenEpisodeLength episodeLength) {
            this.episodeLengths.remove(episodeLength);
            return this;
        }

        public Builder excludeEpisodeLengths(ShindenEpisodeLength... episodeLengths) {
            List.of(episodeLengths).forEach(this.episodeLengths::remove);
            return this;
        }


        public Builder includeEpisodesNumber(ShindenEpisodesNumber episodesNumber) {
            this.episodesNumbers.add(episodesNumber);
            this.fromEpisodeNumber = null;
            this.toEpisodeNumber = null;
            return this;
        }

        public Builder includeEpisodesNumbers(ShindenEpisodesNumber... episodesNumbers) {
            this.episodesNumbers.addAll(List.of(episodesNumbers));
            this.fromEpisodeNumber = null;
            this.toEpisodeNumber = null;
            return this;
        }

        public Builder exludeEpisodesNumber(ShindenEpisodesNumber episodesNumber) {
            this.episodesNumbers.remove(episodesNumber);
            this.fromEpisodeNumber = null;
            this.toEpisodeNumber = null;
            return this;
        }

        public Builder exludeEpisodesNumbers(ShindenEpisodesNumber... episodesNumbers) {
            List.of(episodesNumbers).forEach(this.episodesNumbers::remove);
            this.fromEpisodeNumber = null;
            this.toEpisodeNumber = null;
            return this;
        }

        public Builder withAllSeriesTypes() {
            this.seriesTypes = new HashSet<>(
                    List.of(
                            ShindenSeriesType.TV,
                            ShindenSeriesType.ONA,
                            ShindenSeriesType.OVA,
                            ShindenSeriesType.MOVIE,
                            ShindenSeriesType.SPECIAL,
                            ShindenSeriesType.MUSIC
                    )
            );

            return this;
        }

        public Builder withAllEpisodeLengths() {
            this.episodeLengths = new HashSet<>(
                    List.of(
                            ShindenEpisodeLength.LESS_THAN_7_MINUTES,
                            ShindenEpisodeLength.FROM_7_TO_18_MINUTES,
                            ShindenEpisodeLength.FROM_19_TO_27_MINUTES,
                            ShindenEpisodeLength.FROM_28_TO_48_MINUTES,
                            ShindenEpisodeLength.MORE_THAN_48_MINUTES
                    )
            );

            return this;
        }

        public Builder withAllEpisodesNumbers() {
            this.episodesNumbers = new HashSet<>(
                    List.of(
                            ShindenEpisodesNumber.ONLY_ONE_EPISODE,
                            ShindenEpisodesNumber.FROM_2_TO_14_EPISODES,
                            ShindenEpisodesNumber.FROM_15_TO_28_EPISODES,
                            ShindenEpisodesNumber.FROM_29_TO_100_EPISODES,
                            ShindenEpisodesNumber.MORE_THAN_100_EPISODES
                    )
            );

            this.fromEpisodeNumber = null;
            this.toEpisodeNumber = null;
            return this;
        }

        public Builder fromEpisodeNumber(int number) {
            this.episodesNumbers = new HashSet<>();
            this.fromEpisodeNumber = number;
            return this;
        }

        public Builder toEpisodeNumber(int number) {
            this.episodesNumbers = new HashSet<>();
            this.toEpisodeNumber = number;
            return this;
        }

        public Builder hasOnlineEpisodes(boolean onlineEpisodes) {
            this.onlineEpisodes = onlineEpisodes;
            return this;
        }

        public Builder excludeSeriesOnMyList(boolean exclude) {
            this.excludeSeriesOnMyList = exclude;
            return this;
        }

        public Builder excludeSeriesOnMyCompletedList(boolean exclude) {
            this.excludeSeriesOnMyCompletedList = exclude;
            return this;
        }

        public Builder genresSearchType(ShindenGenresSearchType searchType) {
            this.genresSearchType = searchType;
            return this;
        }

        public Builder letter(ShindenLetter letter) {
            this.letter = letter;
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public ShindenSearchParameters build() {
            return new ShindenSearchParameters(this);
        }

    }

}
