package pl.anime.scraper.app.view.shinden.episodes;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.anime.scraper.api.ShindenAPI;
import pl.anime.scraper.app.config.ShindenRestContestants;
import pl.anime.scraper.app.utils.exception.ApiHandlerException;
import pl.anime.scraper.app.utils.exception.DiscordTokenNotProvidedException;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedEpisode;
import pl.anime.scraper.domain.api.shinden.dto.episodes.ShindenRecentlyAddedSeriesEpisodes;
import pl.anime.scraper.utils.api.APIHandler;

@RestController
@RequestMapping(ShindenRestContestants.LATEST_EPISODES)
@RequiredArgsConstructor
public class ShindenLatestEpisodesController {

    private static final String EXCEPTION_MESSAGE =
            "Latest episodes endpoint requires to provide token to the account which has an access to the shinden discord. "
                    + "Invitation url to Shinden discord: https://discord.com/invite/xyH5uS6, "
                    + "How to get the discord token: "
                    + "https://pcstrike.com/how-to-get-discord-token/https://pcstrike.com/how-to-get-discord-token/";

    private final ShindenAPI shindenAPI;
    private final HttpServletRequest httpServletRequest;

    @GetMapping("/latest")
    public ResponseEntity<List<ShindenRecentlyAddedEpisode>> getLatestMessages(
            @RequestParam(value = "limit", defaultValue = "50", required = false) int limit) {

        var latestAddedEpisodes = getLatestEpisodes(limit);

        if (latestAddedEpisodes.isPresent()) {
            return ResponseEntity.ok(latestAddedEpisodes.getResult());
        } else if (latestAddedEpisodes.isOk()) {
            return ResponseEntity.noContent().build();
        }
        throw new ApiHandlerException(latestAddedEpisodes.getEmptyReason());
    }

    @GetMapping(value = "/latest", params = "grouped=true")
    public ResponseEntity<List<ShindenRecentlyAddedSeriesEpisodes>> getLatestEpisodesGroupByTvSeries(
            @RequestParam(value = "limit", defaultValue = "50", required = false) int limit) {
        var latestAddedEpisodes = getGroupedLatestEpisodes(limit);

        if (latestAddedEpisodes.isPresent()) {
            return ResponseEntity.ok(latestAddedEpisodes.getResult());
        } else if (latestAddedEpisodes.isOk()) {
            return ResponseEntity.noContent().build();
        }
        throw new ApiHandlerException(latestAddedEpisodes.getEmptyReason());
    }

    private APIHandler<List<ShindenRecentlyAddedEpisode>> getLatestEpisodes(int limit) {
        var authorizationToken = httpServletRequest.getHeader("authorization");

        if (!StringUtils.hasText(authorizationToken)) {
            throw new DiscordTokenNotProvidedException(EXCEPTION_MESSAGE);
        }
        return shindenAPI.getRecentlyAddedEpisodes(authorizationToken, limit);
    }

    private APIHandler<List<ShindenRecentlyAddedSeriesEpisodes>> getGroupedLatestEpisodes(int limit) {
        var authorizationToken = httpServletRequest.getHeader("authorization");

        if (!StringUtils.hasText(authorizationToken)) {
            throw new DiscordTokenNotProvidedException(EXCEPTION_MESSAGE);
        }
        return shindenAPI.getRecentlyAddedEpisodesGroupedBySeries(authorizationToken, limit);
    }


}
