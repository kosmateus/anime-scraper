package pl.anime.scraper.infra.utils;

import java.io.IOException;
import java.util.Optional;
import org.jsoup.HttpStatusException;
import pl.anime.scraper.infra.common.HttpStatus;

public class HttpStatusExceptionRetrieval {

    public static Optional<HttpStatus> getHttpStatus(IOException e) {
        if (e instanceof HttpStatusException) {
            return Optional.of(HttpStatus.valueOf(((HttpStatusException) e).getStatusCode()));
        }
        return Optional.empty();
    }
}
