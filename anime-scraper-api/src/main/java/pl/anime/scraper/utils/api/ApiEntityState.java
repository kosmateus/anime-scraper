package pl.anime.scraper.utils.api;

public enum ApiEntityState {
    EMPTY_OK,
    API_OUTDATED,
    UNKNOWN,
    BAD_REQUEST_PARAMETERS,
    BAD_RESPONSE_FROM_EXTERNAL_SERVICE,
    FORBIDDEN
}
