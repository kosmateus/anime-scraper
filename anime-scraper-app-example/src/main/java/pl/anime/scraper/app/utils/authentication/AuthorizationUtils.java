package pl.anime.scraper.app.utils.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthorizationUtils {

    public static UserCredentials decodeAuthorizationHeader(String encodedAuthorization) {
        String base64Credentials = encodedAuthorization.substring("Basic".length()).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(base64Credentials);
        String[] userCredentials = new String(decodedCredentials, StandardCharsets.UTF_8).split(":");
        String username = userCredentials[0];
        String password = userCredentials[1];
        return new UserCredentials(username, password);
    }

}
