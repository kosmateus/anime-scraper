package pl.anime.scraper.domain.api.shinden;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.anime.scraper.utils.exception.ErrorCode;

@AllArgsConstructor
@Getter
enum GenericErrorCodes implements ErrorCode {

    INVALID_LIMIT_PARAMETER("SG_001");

    private final String code;

}
