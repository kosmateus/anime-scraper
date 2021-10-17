package pl.anime.scraper.utils.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEmptyReason {
    private final ApiEntityState state;
    private final ApiErrorDetails errorDetails;

    public ApiEmptyReason(){
        this.state = null;
        this.errorDetails = null;
    }

    public static ApiEmptyReason ok(){
        return new ApiEmptyReason(ApiEntityState.EMPTY_OK, ApiErrorDetails.empty());
    }

    public static ApiEmptyReason apiOutdated(ApiErrorDetails details){
        return new ApiEmptyReason(ApiEntityState.API_OUTDATED, details);
    }

    public static ApiEmptyReason badRequestParameters(ApiErrorDetails details){
        return new ApiEmptyReason(ApiEntityState.BAD_REQUEST_PARAMETERS, details);
    }

    public static ApiEmptyReason badResponseFromExternalService(ApiErrorDetails details) {
        return new ApiEmptyReason(ApiEntityState.BAD_RESPONSE_FROM_EXTERNAL_SERVICE, details);
    }

    public static ApiEmptyReason forbidden(ApiErrorDetails details) {
        return new ApiEmptyReason(ApiEntityState.FORBIDDEN, details);
    }

    public static ApiEmptyReason unknown(ApiErrorDetails details) {
        return new ApiEmptyReason(ApiEntityState.UNKNOWN, details);
    }
}
