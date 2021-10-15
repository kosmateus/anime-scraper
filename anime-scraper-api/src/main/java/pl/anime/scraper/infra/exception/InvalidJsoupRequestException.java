package pl.anime.scraper.infra.exception;


import lombok.Getter;
import pl.anime.scraper.infra.common.HttpStatus;

@Getter
public class InvalidJsoupRequestException extends RuntimeException {

    private final HttpStatus httpStatus;

    public InvalidJsoupRequestException(HttpStatus httpStatus, Exception e) {
        super(e);
        this.httpStatus = httpStatus;
    }
}
