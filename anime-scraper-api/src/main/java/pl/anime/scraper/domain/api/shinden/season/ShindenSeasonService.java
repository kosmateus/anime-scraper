package pl.anime.scraper.domain.api.shinden.season;

import java.util.List;
import java.util.Map;
import pl.anime.scraper.domain.api.shinden.season.json.ShindenSeason;
import pl.anime.scraper.domain.api.shinden.season.json.ShindenSeasonTitle;
import pl.anime.scraper.infra.shinden.ShindenJsoupClient;
import pl.anime.scraper.utils.api.APIHandler;
import pl.anime.scraper.utils.parser.ApiParserUtil;

public class ShindenSeasonService {

    public APIHandler<List<ShindenSeason>> findLatestSeasons() {
        return ApiParserUtil.getAndParse(
                ShindenJsoupClient::getMainPage,
                ShindenLatestSeasonParser::parseToLatestSeasons,
                SeasonErrorCode.BAD_RESPONSE_FOR_MAIN_PAGE
        );
    }

    public APIHandler<Map<String, List<ShindenSeasonTitle>>> findTitlesForSeason(Integer year, String season) {
        return ApiParserUtil.getAndParse(
                () -> ShindenJsoupClient.getSeasonPage(year, season),
                ShindenLatestSeasonParser::parseToSeasonTitles,
                SeasonErrorCode.BAD_RESPONSE_FOR_SEASON_PAGE
        );
    }
}
