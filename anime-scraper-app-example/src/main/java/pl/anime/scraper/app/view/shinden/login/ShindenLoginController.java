package pl.anime.scraper.app.view.shinden.login;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.anime.scraper.api.ShindenAPI;
import pl.anime.scraper.app.config.ShindenRestContestants;
import pl.anime.scraper.app.utils.authentication.AuthorizationUtils;
import pl.anime.scraper.domain.api.shinden.login.LoginRequestData;
import pl.anime.scraper.domain.api.shinden.login.json.ShindenLoginResult;

@RestController
@RequiredArgsConstructor
@RequestMapping(ShindenRestContestants.LOGIN)
public class ShindenLoginController {

    private final ShindenAPI shindenAPI;
    private final HttpServletRequest servletRequest;

    @PostMapping
    public ResponseEntity<ShindenLoginResult> loginUser() {
        var userCredentials = AuthorizationUtils.decodeAuthorizationHeader(
                servletRequest.getHeader(HttpHeaders.AUTHORIZATION));
        var loginRequestData = LoginRequestData.builder().withUsername(userCredentials.getUsername())
                .withPassword(userCredentials.getPassword()).withRemember(true).build();

        var shindenLoginResult = shindenAPI.loginUser(loginRequestData);

        return shindenLoginResult.isSuccess() ?
                ResponseEntity.ok(shindenLoginResult)
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body(shindenLoginResult);

    }

    @GetMapping("/status")
    public ResponseEntity<ShindenLoginResult> checkLoginStatus() {
        var shindenLoginResult = shindenAPI.checkLoginStatus();
        return shindenLoginResult.isSuccess() ?
                ResponseEntity.ok(shindenLoginResult)
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body(shindenLoginResult);
    }
}
