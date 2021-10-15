package pl.anime.scraper.utils.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaType {
    APPLICATION_JSON("application/json");
    private final String value;
}
