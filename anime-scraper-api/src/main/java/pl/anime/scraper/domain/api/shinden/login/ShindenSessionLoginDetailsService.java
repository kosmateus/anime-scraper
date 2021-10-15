package pl.anime.scraper.domain.api.shinden.login;

import java.util.Map;

public interface ShindenSessionLoginDetailsService {

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getAuthToken();

    void setAuthToken(String authToken);

    Map<String, String> getCookies();

    void setCookies(Map<String, String> cookies);
}
