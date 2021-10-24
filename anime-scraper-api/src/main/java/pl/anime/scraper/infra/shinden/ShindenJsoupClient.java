package pl.anime.scraper.infra.shinden;

import java.util.Map;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.anime.scraper.config.ShindenConstants;
import pl.anime.scraper.utils.http.ResponseHandler;
import pl.anime.scraper.utils.jsoup.JsoupUtils;

public class ShindenJsoupClient {

    public static ResponseHandler<Document> postLoginUser(Map<String, String> loginData) {
        return JsoupUtils.executeConnection(() -> Jsoup.connect(ShindenConstants.SHINDEN_URL + "/main/0/login")
                .headers(ShindenConstants.LOGIN_HEADERS)
                .data(loginData)
                .method(Method.POST)
        );
    }

    public static ResponseHandler<Document> postLoginUserMainPage(Map<String, String> cookies) {
        return JsoupUtils.executeConnection(() -> Jsoup.connect(ShindenConstants.SHINDEN_URL)
                .headers(ShindenConstants.LOGIN_HEADERS)
                .cookies(cookies)
                .method(Method.POST)
        );
    }

    public static ResponseHandler<Document> getUserPage(Map<String, String> cookies) {
        return JsoupUtils.executeConnection(() -> Jsoup.connect(ShindenConstants.SHINDEN_URL + "/user")
                .cookies(cookies)
                .method(Method.GET)
        );
    }


    public static ResponseHandler<Document> getUserPage(Map<String, String> cookies, boolean followRedirects) {
        return JsoupUtils.executeConnection(() -> Jsoup.connect(ShindenConstants.SHINDEN_URL + "/user")
                .followRedirects(followRedirects)
                .cookies(cookies)
                .method(Method.GET)
        );
    }

    public static ResponseHandler<Document> getMainPage() {
        return JsoupUtils.executeConnection(() -> Jsoup.connect(ShindenConstants.SHINDEN_URL).method(Method.GET));
    }

    public static ResponseHandler<Document> getSeasonPage(Integer year, String season) {
        return JsoupUtils.executeConnection(
                () -> Jsoup.connect(ShindenConstants.SHINDEN_URL + String.format("/season/%s/%s", year, season))
                        .method(Method.GET));
    }

    public static ResponseHandler<Document> getSeriesPage(String seriesId) {
        return JsoupUtils.executeConnection(
                () -> Jsoup.connect(ShindenConstants.SHINDEN_URL + "/series/" + seriesId)
        );
    }

    public static ResponseHandler<Document> getSeriesCharactersPage(String seriesId) {
        return JsoupUtils.executeConnection(
                () -> Jsoup.connect(ShindenConstants.SHINDEN_URL + "/series/" + seriesId + "/characters")
                        .method(Method.GET));
    }

    public static ResponseHandler<Document> getSeriesEpisodesPage(String seriesId, Map<String, String> cookies) {
        return JsoupUtils.executeConnection(
                () -> Jsoup.connect(ShindenConstants.SHINDEN_URL + "/series/" + seriesId + "/all-episodes")
                        .cookies(cookies)
                        .header("accept-language", "en-US,en;q=0.9,pl-PL;q=0.8,pl;q=0.7,jp-JP;q=0.6,jp;q=0.5")
                        .method(Method.GET)
        );
    }


    public static ResponseHandler<Document> getSeriesEpisodeSourcesPage(String seriesId, Long episodeId,
            Map<String, String> cookies) {
        return JsoupUtils.executeConnection(
                () -> Jsoup.connect(ShindenConstants.SHINDEN_URL + "/episode/" + seriesId + "/view/" + episodeId)
                        .cookies(cookies)
                        .header("accept-language", "en-US,en;q=0.9,pl-PL;q=0.8,pl;q=0.7,jp-JP;q=0.6,jp;q=0.5")
                        .method(Method.GET)
        );
    }

    public static ResponseHandler<Document> getPlayerLoad(Long episodeId, String token, Map<String, String> cookies) {
        return JsoupUtils.executeConnection(
                () -> Jsoup.connect(
                                String.format(ShindenConstants.SHINDEN_API_URL, episodeId, "player_load", token)
                        )
                        .cookies(cookies)
                        .headers(ShindenConstants.SOURCE_API_HEADERS)
                        .method(Method.GET)
        );
    }

    public static ResponseHandler<Document> getPlayerShow(Long episodeId, String token, Map<String, String> cookies) {
        return JsoupUtils.executeConnection(
                () -> Jsoup.connect(
                                String.format(ShindenConstants.SHINDEN_API_URL + "&width=508&height=-1", episodeId,
                                        "player_show", token)
                        )
                        .headers(ShindenConstants.SOURCE_API_HEADERS)
                        .cookies(cookies)
                        .method(Method.GET)
        );
    }

    public static ResponseHandler<Document> getSearchPage(String queryParams, Map<String, String> cookies) {
        return JsoupUtils.executeConnection(
                () -> Jsoup.connect(ShindenConstants.SHINDEN_URL + "/series?" + queryParams)
                        .cookies(cookies)
                        .header("accept-language", "en-US,en;q=0.9,pl-PL;q=0.8,pl;q=0.7,jp-JP;q=0.6,jp;q=0.5")
                        .method(Method.GET)
        );
    }
}
