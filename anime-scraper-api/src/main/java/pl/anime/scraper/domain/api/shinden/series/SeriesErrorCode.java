package pl.anime.scraper.domain.api.shinden.series;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.anime.scraper.utils.exception.ErrorCode;

@RequiredArgsConstructor
@Getter
enum SeriesErrorCode implements ErrorCode {
    NO_PAGE_TITLE("SS_001"),
    NO_TITLE_COVER("SS_002"),
    NO_IMAGE_A_TAG("SS_003"),
    NO_IMAGE_IMG_TAG("SS_004"),
    NO_DESCRIPTION_P_TAG("SS_005"),
    NO_INFO_TABLE("SS_006"),
    NO_A_TAG_IN_TAGS("SS_007"),
    NO_TITLE_SMALL_INFO("SS_008"),
    NO_DL_IN_TITLE_SMALL_INFO("SS_009"),
    INVALID_PARSE_TO_DATE("SS_010"),
    NO_TITLE_STATS("SS_011"),
    NO_DL_IN_TITLE_STATS("SS_012"),
    NO_CH_ST_LIST("SS_013"),
    NO_ITEM_L_IN_CH_ST_ITEM("SS_014"),
    NO_ITEM_R_IN_CH_ST_ITEM("SS_015"),
    NO_A_TAG_IN_ITEM_L("SS_016"),
    NO_IMG_TAG_IN_ITEM_L("SS_017"),
    NO_P_TXT_IN_ITEM_L("SS_018"),
    NO_P_TXT_A_TAG_IN_ITEM_L("SS_019"),
    NO_CHARACTER_ROLE("SS_020"),
    NO_PERSON_LIST("SS_021"),
    NO_PERSON_SPAN_TAG("SS_022"),
    NO_PERSON_IMAGE("SS_023"),
    NO_PERSON_A_TAG("SS_024"),
    NO_PERSON_P_TAG("SS_025"),
    NO_FIGURE_LIST("SS_026"),
    NO_RELATED_SERIES_TITLE("SS_027"),
    NO_RELATED_SERIES_IMAGE("SS_028"),
    BAD_RESPONSE_FOR_SERIES_PAGE("SS_029"),
    BAD_RESPONSE_FOR_SERIES_CHARACTERS_PAGE("SS_030"),
    BAD_RESPONSE_FOR_SERIES_EPISODES_OVERVIEW("SS_031"),
    USER_NOT_AUTHENTICATED_FOR_EPISODES_OVERVIEW("SS_032");

    private final String code;
}
