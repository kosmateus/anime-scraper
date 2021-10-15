package pl.anime.scraper.domain.api.shinden.login;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
@Builder(builderClassName = "Builder", setterPrefix = "with", access = AccessLevel.PACKAGE)
class LoginDetails {

    private String username;
    private String password;
    private String authToken;
    private Map<String, String> cookies;
}
