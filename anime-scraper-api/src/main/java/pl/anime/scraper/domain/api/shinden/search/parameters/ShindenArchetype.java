package pl.anime.scraper.domain.api.shinden.search.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShindenArchetype implements ShindenTagParam {
    ANIME(2314),
    COMPUTER_GAME(193),
    OTHER_GAMES(2323),
    OTHER(2410),
    CARD_GAME(2016),
    BOOK(2029),
    LIGHT_NOVEL(1976),
    MANGA(1956),
    MANGA_4_KOMA(1996),
    NOVEL(2127),
    ORIGINAL(1966),
    VISUAL_NOVEL(1990),
    WEB_MANGA(2025);

    private final Integer value;
}
