package pl.anime.scraper.domain.api.shinden.season;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.anime.scraper.utils.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
enum SeasonErrorCode implements ErrorCode {

    NO_BOX_NEW_SERIES("SLSP_001"),
    NO_SEASONS_LIST("SLSP_002"),
    NO_GROUP_NAME("SLSP_003"),
    NO_BOX_TITLE("SLSP_004"),
    NO_A_TAG_IN_BOX_TITLE("SLSP_005"),
    NO_SECTION("SLSP_006"),
    NO_IMAGE("SLSP_007"),
    NO_RATE_STATS("SLSP_008"),
    NO_RATE_B_TAG("SLSP_009"),
    INDEX_OUT_OF_BOUND_FOR_RATE("SLSP_010"),
    NO_FOOTER("SLSP_011"),
    NO_A_TAG_IN_FOOTER("SLSP_012"),
    NO_EPISODE_NUMBER("SLSP_013"),
    NO_SPAN_TAG_IN_FOOTER("SLSP_014"),
    NO_START_DATE("SLSP_015"),
    NO_END_DATE("SLSP_016"),
    INVALID_CONVERSION_TO_END_DATE("SLSP_017"),
    INVALID_CONVERSION_TO_START_DATE("SLSP_018"),
    NO_GENRES("SLSP_019"),
    NO_A_TAG_IN_GENRES("SLSP_020"),
    INDEX_OUT_OF_BOUND_FOR_GENRES("SLSP_021"),
    NO_WATCHED_STATS("SLSP_022"),
    INDEX_OUT_OF_BOUND_FOR_STATS("SLSP_023"),
    NO_FAVORITES("SLSP_024"),
    NO_DESCRIPTION("SLSP_025"),
    BAD_RESPONSE_FOR_MAIN_PAGE("SLSP_026"),
    BAD_RESPONSE_FOR_SEASON_PAGE("SLSP_027");

    private final String code;
}
