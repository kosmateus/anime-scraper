package pl.anime.scraper.app.utils.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import pl.anime.scraper.utils.api.ApiEmptyReason;
import pl.anime.scraper.utils.api.ApiEntityState;

@ControllerAdvice
public class CustomExceptionHandler implements ProblemHandling {

    public static final String INVALID__REQUEST_PARAMETER = "Invalid request parameter";
    private static final String DISCORD_TOKEN_NOT_PROVIDED = "Discord token was not provided in authorization header";

    @ExceptionHandler(DiscordTokenNotProvidedException.class)
    public ResponseEntity<Problem> handleDiscordTokenNotProvidedException(DiscordTokenNotProvidedException exception,
            NativeWebRequest request) {

        var problem = Problem.builder()
                .withTitle(DISCORD_TOKEN_NOT_PROVIDED)
                .withDetail(exception.getMessage())
                .with("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .withStatus(Status.FORBIDDEN)
                .build();

        return this.create(exception, problem, request);
    }

    @ExceptionHandler(ApiHandlerException.class)
    public ResponseEntity<Problem> handleApiHandlerException(ApiHandlerException exception, NativeWebRequest request) {
        var emptyReason = exception.getEmptyReason();
        var errorDetails = emptyReason.getErrorDetails();
        var externalRequestErrorDetails = errorDetails.getExternalRequestErrorDetails();
        var problemBuilder = Problem.builder()
                .withTitle(errorDetails.getErrorName())
                .withDetail(errorDetails.getMessage())
                .with("errorCode", errorDetails.getCode().getCode())
                .with("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .withStatus(getStatusForApiHandlerException(emptyReason));

        if (externalRequestErrorDetails != null) {
            problemBuilder.with(
                    "externalRequestErrorDetails",
                    Map.of(
                            "errorName", externalRequestErrorDetails.getErrorName(),
                            "status", externalRequestErrorDetails.getHttpStatus(),
                            "message", externalRequestErrorDetails.getMessage(),
                            "code", externalRequestErrorDetails.getCode()
                    )
            );
        }

        var build = problemBuilder.build();
        return this.create(exception, build, request);
    }

    private Status getStatusForApiHandlerException(ApiEmptyReason apiEmptyReason) {
        var errorName = apiEmptyReason.getErrorDetails().getErrorName();
        var externalRequestErrorDetails = apiEmptyReason.getErrorDetails().getExternalRequestErrorDetails();
        var state = apiEmptyReason.getState();
        if (INVALID__REQUEST_PARAMETER.equalsIgnoreCase(errorName)) {
            return Status.BAD_REQUEST;
        } else if (state == ApiEntityState.FORBIDDEN) {
            return Status.FORBIDDEN;
        } else {
            return externalRequestErrorDetails != null ? Status.BAD_GATEWAY : Status.INTERNAL_SERVER_ERROR;
        }
    }

}
