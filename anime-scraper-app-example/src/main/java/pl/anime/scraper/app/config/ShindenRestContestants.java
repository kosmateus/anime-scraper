package pl.anime.scraper.app.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShindenRestContestants {

    public static final String SHINDEN = "/shinden";
    public static final String LOGIN = SHINDEN + "/login";
    public static final String LATEST_EPISODES = SHINDEN + "/episodes";
    public static final String SEASONS = SHINDEN + "/seasons";
    public static final String SERIES = SHINDEN + "/series";
 }
