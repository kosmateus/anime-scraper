package pl.anime.scraper.domain.api.shinden.login;


import org.apache.commons.lang3.StringUtils;
import pl.anime.scraper.domain.api.shinden.login.json.ShindenLoginResult;
import pl.anime.scraper.domain.api.shinden.login.json.ShindenLoginStatus;
import pl.anime.scraper.infra.shinden.ShindenJsoupClient;
import pl.anime.scraper.utils.login.LoginUtils;

public class ShindenLoginService {

    public static ShindenLoginResult loginUser(LoginRequestData loginRequest,
            ShindenSessionLoginDetailsService sessionLoginDetails) {
        var firstLoginResponse = ShindenJsoupClient.postLoginUser(loginRequest.getMap());
        if (firstLoginResponse.isPresent() && firstLoginResponse.getCookie("jwtCookie").isEmpty()) {
            return new ShindenLoginResult(ShindenLoginStatus.FAILED, "Please check your credentials and try again.");
        }

        var cookies = firstLoginResponse.getCookies();
        var mainPageDocument = ShindenJsoupClient.postLoginUserMainPage(cookies);
        var loginDetails = ShindenLoginParser.createLoginDetails(mainPageDocument.getEntity(), loginRequest, cookies);

        saveLoginDetails(sessionLoginDetails, loginDetails);

        return new ShindenLoginResult(ShindenLoginStatus.SUCCESS,
                String.format("Successfully stored login data in %s for user %s",
                        ShindenSessionLoginDetailsService.class.getSimpleName(), loginDetails.getUsername()));
    }

    public static ShindenLoginResult checkLogin(ShindenSessionLoginDetailsService sessionLoginDetails) {
        if (LoginUtils.isLogin(sessionLoginDetails)) {
            var userPage = ShindenJsoupClient.getUserPage(sessionLoginDetails.getCookies(), false);

            var locationHeader = userPage.getFlatHeader("location");
            if (locationHeader.isEmpty() || !StringUtils.containsIgnoreCase(locationHeader.get(),
                    sessionLoginDetails.getUsername())) {
                return new ShindenLoginResult(ShindenLoginStatus.FAILED, "User is not log in anymore, please sign in.");
            }
            return new ShindenLoginResult(ShindenLoginStatus.SUCCESS,
                    String.format("User %s is still sign in", sessionLoginDetails.getUsername()));
        }
        return new ShindenLoginResult(ShindenLoginStatus.FAILED, "No user is login currently.");
    }

    private static void saveLoginDetails(ShindenSessionLoginDetailsService sessionLoginDetails,
            LoginDetails loginDetails) {
        sessionLoginDetails.setAuthToken(loginDetails.getAuthToken());
        sessionLoginDetails.setPassword(loginDetails.getPassword());
        sessionLoginDetails.setCookies(loginDetails.getCookies());
        sessionLoginDetails.setUsername(loginDetails.getUsername());
    }
}
