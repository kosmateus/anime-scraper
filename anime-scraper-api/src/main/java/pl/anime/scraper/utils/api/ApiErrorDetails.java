package pl.anime.scraper.utils.api;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.anime.scraper.utils.exception.ErrorCode;

@Getter
@EqualsAndHashCode
public class ApiErrorDetails {

    private final String errorName;
    private final ErrorCode code;
    private final String message;
    private final ApiExternalRequestErrorDetails externalRequestErrorDetails;

    private ApiErrorDetails(Builder builder) {
        this.errorName = builder.errorName;
        this.code = builder.code;
        this.message = builder.message;
        this.externalRequestErrorDetails = builder.externalRequestErrorDetails;
    }

    public static ApiErrorDetails empty() {
        return builder().build();
    }

    public static Builder builder() {
        return new ApiErrorDetails.Builder();
    }

    public static ApiErrorDetails outdated(ErrorCode code, String message) {
        return builder().withCode(code).withMessage(message).withErrorName("API outdated").build();
    }

    public static ApiErrorDetails badRequestParameters(ErrorCode code, String message) {
        return builder().withCode(code).withMessage(message).withErrorName("Invalid request parameter")
                .build();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Builder {

        private String errorName;
        private ErrorCode code;
        private String message;
        private ApiExternalRequestErrorDetails externalRequestErrorDetails;


        private static Builder anApiErrorDetails() {
            return new Builder();
        }

        public Builder withCode(ErrorCode code) {
            this.code = code;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withErrorName(String errorName) {
            this.errorName = errorName;
            return this;
        }

        public Builder withExternalRequestErrorDetails(ApiExternalRequestErrorDetails externalRequestErrorDetails) {
            this.externalRequestErrorDetails = externalRequestErrorDetails;
            return this;
        }


        public ApiErrorDetails build() {
            return new ApiErrorDetails(this);
        }
    }
}
