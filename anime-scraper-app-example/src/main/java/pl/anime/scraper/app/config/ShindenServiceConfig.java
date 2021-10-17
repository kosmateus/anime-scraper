package pl.anime.scraper.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.anime.scraper.api.ShindenAPI;
import pl.anime.scraper.app.domain.shinden.login.ShindenLoginDetailsService;

@Configuration
@RequiredArgsConstructor
public class ShindenServiceConfig {

    private final ShindenLoginDetailsService shindenLoginDetailsService;

    @Bean
    public ShindenAPI shindenService() {
        return ShindenAPI.with(shindenLoginDetailsService, 50);
    }

}
