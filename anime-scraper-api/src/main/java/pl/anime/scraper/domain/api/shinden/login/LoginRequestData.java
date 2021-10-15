package pl.anime.scraper.domain.api.shinden.login;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
@Builder(builderClassName = "Builder", setterPrefix = "with")
public class LoginRequestData {

    private String username;
    private String password;
    private boolean remember;

    Map<String, String> getMap() {
        return Map.of(
                "username", username,
                "password", password,
                "remember", remember ? "on" : "",
                "login", ""
        );
    }

}
