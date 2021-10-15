package pl.anime.scraper.utils.http;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseHandler<T> {

    private T entity;
    private Map<String, List<String>> httpHeaders;
    private Map<String, String> cookies;
    private int httpStatus;
    private EmptyReason emptyReason;


    private ResponseHandler(T entity, Map<String, List<String>> httpHeaders, int httpStatus) {
        this.entity = entity;
        this.httpHeaders = httpHeaders;
        this.httpStatus = httpStatus;
        this.cookies = Collections.emptyMap();
    }

    private ResponseHandler(T entity, Map<String, List<String>> httpHeaders, Map<String, String> cookies,
            int httpStatus) {
        this.entity = entity;
        this.httpHeaders = httpHeaders;
        this.httpStatus = httpStatus;
        this.cookies = cookies;
    }

    public ResponseHandler(Map<String, List<String>> httpHeaders, int httpStatus, EmptyReason emptyReason) {
        this.httpHeaders = httpHeaders;
        this.httpStatus = httpStatus;
        this.emptyReason = emptyReason;
        this.cookies = Collections.emptyMap();
    }

    public static <T> ResponseHandler<T> ofNullable(T entity, int httpStatus, Map<String, List<String>> httpHeaders,
            ErrorDetails errorDetails) {
        return entity != null && httpHeaders != null ? of(entity, httpStatus, httpHeaders)
                : emptyByNotFound(httpHeaders, errorDetails);
    }

    public static <T> ResponseHandler<T> of(T entity, int httpStatus, Map<String, List<String>> httpHeaders) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        return new ResponseHandler<>(entity, httpHeaders, httpStatus);
    }

    public static <T> ResponseHandler<T> of(T entity, int httpStatus, Map<String, String> httpHeaders,
            Map<String, String> cookies) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        var headers = httpHeaders.entrySet()
                .stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> List.of(entry.getValue())));
        return new ResponseHandler<>(entity, headers, cookies, httpStatus);
    }

    public static <T> ResponseHandler<T> emptyByNotFound(Map<String, List<String>> httpHeaders,
            ErrorDetails errorDetails) {
        return empty(HttpStatus.SC_NOT_FOUND, httpHeaders, EmptyReason.notFound(errorDetails));
    }

    public static <T> ResponseHandler<T> empty(int httpStatus, Map<String, List<String>> headers,
            EmptyReason reason) {
        return new ResponseHandler<>(headers, httpStatus, reason);
    }

    public static <T> ResponseHandler<T> emptyOk(int httpStatus, Map<String, List<String>> httpHeaders) {
        return empty(httpStatus, httpHeaders, EmptyReason.ok());
    }

    public boolean isOk() {
        return isPresent() || EmptyReason.ok().equals(this.emptyReason);
    }

    public boolean isPresent() {
        return entity != null;
    }

    public T getEntity() {
        if (isPresent()) {
            return entity;
        }
        throw new NoSuchElementException("Entity was not provided in response");
    }

    public T orElse(T other) {
        return this.isPresent() ? this.entity : other;
    }

    public T orElseHandleEmptyReason(Function<EmptyReason, T> mapper) {
        return this.isPresent() ? this.entity : mapper.apply(this.emptyReason);
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Map<String, List<String>> getHttpHeaders() {
        return httpHeaders;
    }

    public Map<String, String> getFlatHttpHeaders() {
        return httpHeaders.entrySet()
                .stream()
                .collect(Collectors.toMap(
                                Entry::getKey,
                                entry -> String.join("; ", entry.getValue())
                        )
                );
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Optional<String> getCookie(String cookieName) {
        return Optional.ofNullable(cookies.get(cookieName));
    }

    public Optional<List<String>> getHeader(String headerName) {
        return Optional.ofNullable(httpHeaders.get(headerName));
    }

    public Optional<String> getFlatHeader(String headerName) {
        var header = getHeader(headerName);
        return header.isEmpty() ? Optional.empty() : Optional.of(String.join(", ", header.get()));
    }

    public EmptyReason getEmptyReason() {
        return emptyReason;
    }
}
