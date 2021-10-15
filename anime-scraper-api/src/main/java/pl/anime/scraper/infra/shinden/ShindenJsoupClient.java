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
                () -> Jsoup.connect(ShindenConstants.SHINDEN_URL + "/series/" + seriesId + "/episodes")
                        .cookies(cookies)
                        .method(Method.GET)
        );
    }
}
