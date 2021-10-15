package pl.anime.scraper.app.utils.exception;

import lombok.Getter;
import pl.anime.scraper.utils.api.ApiEmptyReason;

@Getter
public class ApiHandlerException extends RuntimeException {

    private final ApiEmptyReason emptyReason;

    public ApiHandlerException(ApiEmptyReason emptyReason) {
        this.emptyReason = emptyReason;
    }
}
