package pl.anime.scraper.utils.jsoup;

import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import pl.anime.scraper.config.CommonConstants;
import pl.anime.scraper.infra.common.HttpStatus;
import pl.anime.scraper.infra.utils.HttpStatusExceptionRetrieval;
import pl.anime.scraper.utils.http.EmptyReason;
import pl.anime.scraper.utils.http.ErrorDetails;
import pl.anime.scraper.utils.http.ResponseHandler;

public class JsoupUtils {

    public static ResponseHandler<Document> executeConnection(Supplier<Connection> connection) {
        try {
            var response = connection.get().execute();
            return ResponseHandler.of(response.parse(), response.statusCode(), response.headers(), response.cookies());
        } catch (IOException e) {
            var httpStatus = HttpStatusExceptionRetrieval.getHttpStatus(e).orElse(HttpStatus.BAD_REQUEST).value();
            return ResponseHandler.empty(httpStatus, Map.of(),
                    EmptyReason.fromHttpStatus(httpStatus,
                            ErrorDetails.builder().
                                    withErrorName(CommonConstants.INVALID_RESPONSE_FROM_EXTERNAL_SERVICE)
                                    .withCode("JSOUP_EX")
                                    .withMessage(e.getMessage())
                                    .build()
                    )
            );
        }
    }
}
