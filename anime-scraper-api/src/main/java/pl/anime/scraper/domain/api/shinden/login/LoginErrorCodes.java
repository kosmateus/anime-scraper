package pl.anime.scraper.domain.api.shinden.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.anime.scraper.utils.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
enum LoginErrorCodes implements ErrorCode {
    NO_AUTH_TOKEN_IN_LOGIN_RESPONSE("SL_001");
    private final String code;
}
