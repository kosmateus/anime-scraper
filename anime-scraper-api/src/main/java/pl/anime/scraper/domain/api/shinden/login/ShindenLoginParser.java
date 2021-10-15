package pl.anime.scraper.domain.api.shinden.login;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import pl.anime.scraper.utils.exception.JsoupParserException;

class ShindenLoginParser {

    static LoginDetails createLoginDetails(Document document, LoginRequestData loginRequestData,
            Map<String, String> cookies) {
        return LoginDetails.builder()
                .withUsername(loginRequestData.getUsername())
                .withPassword(loginRequestData.getPassword())
                .withCookies(cookies)
                .withAuthToken(retrieveAuthToken(document))
                .build();
    }

    private static String retrieveAuthToken(Document document) {
        Matcher matcher = getMatcher(document);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new JsoupParserException(LoginErrorCodes.NO_AUTH_TOKEN_IN_LOGIN_RESPONSE);
    }

    private static Matcher getMatcher(Document document) {
        String htmlDocument = document.html();
        Pattern regex = Pattern.compile("_Storage\\.basic.*=.*'(.*?)'");
        return regex.matcher(htmlDocument);
    }
}
