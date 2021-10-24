package pl.anime.scraper.domain.api.shinden.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.anime.scraper.utils.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
enum SearchErrorCodes implements ErrorCode {

    BAD_RESPONSE_FOR_SEARCH_PAGE("SSR_001"),
    NO_CURRENT_PAGE_IN_PAGINATION("SSR_002"),
    NO_LAST_PAGE_IN_PAGINATION("SSR_003"),
    NO_IMAGE_URL("SSR_004"),
    NO_TITLE("SSR_005"),
    NO_GENRES("SSR_006"),
    NO_SERIES_TYPE("SSR_007"),
    NO_EPISODES_NUMBER("SSR_008"),
    NO_EPISODES_LENGTH("SSR_009"),
    NO_RATE("SSR_010"),
    NO_OVERALL_RATE("SSR_011"),
    NO_PLOT_RATE("SSR_012"),
    NO_GRAPHICS_RATE("SSR_013"),
    NO_MUSIC_RATE("SSR_014"),
    NO_CHARACTERS_RATE("SSR_015"),
    NO_STATUS("SSR_016"),
    NO_RATE_TOP("SSR_017"),
    NO_ACCESS_KEY("SSR_017");

    private final String code;

}
