package pl.anime.scraper.domain.api.shinden.search;

import pl.anime.scraper.domain.api.shinden.login.ShindenSessionLoginDetailsService;
import pl.anime.scraper.domain.api.shinden.search.json.ShindenSearchResult;
import pl.anime.scraper.infra.shinden.ShindenJsoupClient;
import pl.anime.scraper.utils.api.APIHandler;
import pl.anime.scraper.utils.parser.ApiParserUtil;

public class ShindenSearchService {

    public static APIHandler<ShindenSearchResult> searchSeries(ShindenSearchParameters searchParameters,
            ShindenSessionLoginDetailsService loginDetailsService) {
        return ApiParserUtil.getAndParse(
                () -> ShindenJsoupClient.getSearchPage(searchParameters.getSearchQueryParams(),
                        loginDetailsService.getCookies()),
                (document) -> ShindenSearchParser.parseToSearchResult(document, searchParameters),
                SearchErrorCodes.BAD_RESPONSE_FOR_SEARCH_PAGE
        );
    }
}
