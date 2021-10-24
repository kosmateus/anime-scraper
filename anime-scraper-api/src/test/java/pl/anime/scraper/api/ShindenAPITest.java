package pl.anime.scraper.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.anime.scraper.domain.api.shinden.login.ShindenSessionLoginDetailsService;
import pl.anime.scraper.domain.api.shinden.search.ShindenSearchParameters.Builder;
import pl.anime.scraper.domain.api.shinden.search.parameters.ShindenGenres;

@ExtendWith(MockitoExtension.class)
public class ShindenAPITest {

    @Mock
    private ShindenSessionLoginDetailsService loginDetailsService;

    private ShindenAPI api;

    @BeforeEach
    public void setup() {
        api = ShindenAPI.with(loginDetailsService, 50);
    }

    @Test
    public void searchForSeries() {
        var result = api.findSeries(Builder.builder().includeGenre(ShindenGenres.ACTION).searchText("null").build());

        assertThat(result.getEmptyReason()).isNull();
        assertThat(result.getResult()).isNotNull();
    }

}
