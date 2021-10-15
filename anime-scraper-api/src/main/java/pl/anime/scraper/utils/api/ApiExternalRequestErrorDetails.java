package pl.anime.scraper.utils.api;

import lombok.Builder;
import lombok.Getter;
import pl.anime.scraper.utils.http.ErrorDetails;

@Getter
@Builder(builderClassName = "Builder", setterPrefix = "with")
public class ApiExternalRequestErrorDetails {

    private final String code;
    private final String message;
    private final String errorName;
    private final int httpStatus;

    public static ApiExternalRequestErrorDetails of(ErrorDetails errorDetails, int httpStatus) {
        return ApiExternalRequestErrorDetails.builder()
                .withCode(errorDetails.getCode())
                .withErrorName(errorDetails.getErrorName())
                .withHttpStatus(httpStatus)
                .withMessage(errorDetails.getMessage())
                .build();
    }
}
