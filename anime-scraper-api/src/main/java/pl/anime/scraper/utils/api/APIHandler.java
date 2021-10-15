package pl.anime.scraper.utils.api;

import java.util.NoSuchElementException;
import java.util.function.Function;
import pl.anime.scraper.utils.http.EmptyReason;

public class APIHandler<T> {

    private T result;
    private ApiEmptyReason emptyReason;


    private APIHandler(T result) {
        this.result = result;
    }

    public APIHandler(ApiEmptyReason emptyReason) {
        this.emptyReason = emptyReason;
    }

    public static <T> APIHandler<T> ofNullable(T result, ApiErrorDetails errorDetails) {
        return result != null ? of(result) : emptyByApiOutdated(errorDetails);
    }

    public static <T> APIHandler<T> of(T result) {
        if (result == null) {
            throw new IllegalArgumentException("Result cannot be null");
        }
        return new APIHandler<>(result);
    }

    public static <T> APIHandler<T> emptyByApiOutdated(ApiErrorDetails details) {
        return empty(ApiEmptyReason.apiOutdated(details));
    }

    public static <T> APIHandler<T> empty(ApiEmptyReason emptyReason) {
        return new APIHandler<>(emptyReason);
    }

    public static <T> APIHandler<T> emptyOk() {
        return empty(ApiEmptyReason.ok());
    }

    public boolean isOk() {
        return isPresent() || ApiEmptyReason.ok().equals(emptyReason);
    }

    public boolean isPresent() {
        return result != null;
    }

    /**
     * @return result of api execution
     * @throws NoSuchElementException when result is not provided.
     */
    public T getResult() {
        if (isPresent()) {
            return result;
        }
        throw new NoSuchElementException("Result was not provided");
    }

    public T orElse(T other) {
        return isPresent() ? this.result : other;
    }

    public T orElseHandleEmptyReason(Function<ApiEmptyReason, T> mapper) {
        return this.isPresent() ? this.result : mapper.apply(this.emptyReason);
    }

    public ApiEmptyReason getEmptyReason() {
        return emptyReason;
    }
}
