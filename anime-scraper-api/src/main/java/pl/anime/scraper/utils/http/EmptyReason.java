package pl.anime.scraper.utils.http;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmptyReason {

    private final EntityState state;
    private final ErrorDetails errorDetails;

    public EmptyReason() {
        this.state = null;
        this.errorDetails = null;
    }

    public static EmptyReason ok() {
        return new EmptyReason(EntityState.EMPTY_OK, ErrorDetails.empty());
    }

    public static EmptyReason fromHttpStatus(int httpStatus, ErrorDetails errorDetails) {
        switch (httpStatus) {
            case HttpStatus.SC_NOT_FOUND:
                return notFound(errorDetails);
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                return internalError(errorDetails);
            case HttpStatus.SC_UNAUTHORIZED:
                return unauthorized(errorDetails);
            case HttpStatus.SC_BAD_GATEWAY:
                return badGateway(errorDetails);
            case HttpStatus.SC_BAD_REQUEST:
                return badRequest(errorDetails);
            default:
                return errorResponse(errorDetails);
        }
    }

    public static EmptyReason notFound(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.NOT_FOUND, errorDetails);
    }

    public static EmptyReason internalError(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.INTERNAL_ERROR, errorDetails);
    }

    public static EmptyReason unauthorized(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.UNAUTHORIZED, errorDetails);
    }

    public static EmptyReason badGateway(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.BAD_GATEWAY, errorDetails);
    }

    public static EmptyReason badRequest(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.BAD_REQUEST, errorDetails);
    }

    public static EmptyReason errorResponse(ErrorDetails errorDetails) {
        return new EmptyReason(EntityState.GENERIC_ERROR, errorDetails);
    }
}
