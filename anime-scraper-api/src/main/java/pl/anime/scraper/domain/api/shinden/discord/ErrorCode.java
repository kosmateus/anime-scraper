package pl.anime.scraper.domain.api.shinden.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum ErrorCode implements pl.anime.scraper.utils.exception.ErrorCode {
    INVALID_RESPONSE_FROM_SHINDEN_DISCORD("SD_001");

    private final String code;
}
