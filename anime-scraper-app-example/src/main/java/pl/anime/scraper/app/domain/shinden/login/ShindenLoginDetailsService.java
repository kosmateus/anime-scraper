package pl.anime.scraper.app.domain.shinden.login;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pl.anime.scraper.domain.api.shinden.login.ShindenSessionLoginDetailsService;

@Getter
@Setter
@Component
@SessionScope
public class ShindenLoginDetailsService implements ShindenSessionLoginDetailsService {

    private String username;
    private String password;
    private String authToken;
    private Map<String, String> cookies;
}
