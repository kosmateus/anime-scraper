package pl.anime.scraper.utils.login;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import pl.anime.scraper.domain.api.shinden.login.ShindenSessionLoginDetailsService;

public class LoginUtils {

    public static boolean isLogin(ShindenSessionLoginDetailsService sessionLoginDetailsService){
        return StringUtils.isNotBlank(sessionLoginDetailsService.getAuthToken());
    }

    public static Map<String, String> getCookies(ShindenSessionLoginDetailsService sessionLoginDetailsService){
        return sessionLoginDetailsService.getCookies();
    }

}
