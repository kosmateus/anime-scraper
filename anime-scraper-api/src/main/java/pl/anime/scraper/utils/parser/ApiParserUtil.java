package pl.anime.scraper.utils.parser;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jsoup.nodes.Document;
import pl.anime.scraper.config.CommonConstants;
import pl.anime.scraper.utils.api.APIHandler;
import pl.anime.scraper.utils.api.ApiEmptyReason;
import pl.anime.scraper.utils.api.ApiErrorDetails;
import pl.anime.scraper.utils.api.ApiExternalRequestErrorDetails;
import pl.anime.scraper.utils.exception.ErrorCode;
import pl.anime.scraper.utils.exception.JsoupParserException;
import pl.anime.scraper.utils.http.ResponseHandler;

public class ApiParserUtil {

    public static <T> APIHandler<T> getAndParse(Supplier<ResponseHandler<Document>> documentProvided,
            Function<Document, T> parser,
            ErrorCode errorCodeForGettingDocument) {
        var documentResponseHandler = documentProvided.get();

        if (documentResponseHandler.isPresent()) {
            try {

                var parseResult = parser.apply(documentResponseHandler.getEntity());
                if (parseResult instanceof Collection) {
                    if (((Collection<?>) parseResult).isEmpty()) {
                        return APIHandler.emptyOk();
                    }
                }
                return APIHandler.of(parseResult);
            } catch (JsoupParserException e) {
                return APIHandler.empty(
                        ApiEmptyReason.apiOutdated(
                                ApiErrorDetails.outdated(e.getErrorCode(), e.getMessage())
                        ));
            } catch (Exception e) {
                return APIHandler.empty(
                        ApiEmptyReason.unknown(
                                ApiErrorDetails.builder().withErrorName("Unknown error").withMessage(e.getMessage())
                                        .build()
                        ));
            }

        }
        return APIHandler.empty(ApiEmptyReason.badResponseFromExternalService(
                        ApiErrorDetails.builder()
                                .withCode(errorCodeForGettingDocument)
                                .withErrorName(CommonConstants.INVALID_RESPONSE_FROM_EXTERNAL_SERVICE)
                                .withExternalRequestErrorDetails(
                                        ApiExternalRequestErrorDetails.of(
                                                documentResponseHandler.getEmptyReason().getErrorDetails(),
                                                documentResponseHandler.getHttpStatus()
                                        )
                                ).build()
                )
        );
    }

    public static <T> APIHandler<T> getAndParse(Supplier<Boolean> isLogin,
            Supplier<ResponseHandler<Document>> documentProvided,
            Function<Document, T> parser,
            ErrorCode errorCodeForGettingDocument,
            ErrorCode errorCodeForUserUnauthorized) {

        if (Boolean.TRUE.equals(isLogin.get())) {
            return getAndParse(documentProvided, parser, errorCodeForGettingDocument);
        }
        return APIHandler.empty(
                ApiEmptyReason.forbidden(
                        ApiErrorDetails.builder()
                                .withErrorName("Resource is available only for authenticated users")
                                .withMessage("Please login first to access protected resource")
                                .withCode(errorCodeForUserUnauthorized)
                                .build()
                )
        );
    }
}
