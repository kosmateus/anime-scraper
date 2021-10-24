package pl.anime.scraper.domain.api.shinden.search.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShindenPlaceAndTime implements ShindenTagParam {

    ALTERNATIVE_EARTH(2328),
    NORTH_AMERICA(1789),
    APARTMENT_LIFE(2336),
    CHINA(1949),
    DUNGEON(2663),
    DYSTOPIA(2348),
    EUROPE(1745),
    FEUDAL_JAPAN(1730),
    GAME(2322),
    MEDIEVAL(2362),
    JAPAN(1740),
    CAFE(2341),
    COSMOS(10),
    CITY(1785),
    OCEAN(2363),
    TRIP(1788),
    POST_APOCALYPTIC(470),
    FUTURE(2326),
    ALTERNATIVE_WORLD(2327),
    ALL_BOYS_SCHOOL(2333),
    ALL_GIRLS_SCHOOL(2332),
    VIRTUAL_REALITY(1729),
    VILLAGE(1784),
    PRESENT_DAY(1739),
    ISLAND(2357);

    private final Integer value;
}
