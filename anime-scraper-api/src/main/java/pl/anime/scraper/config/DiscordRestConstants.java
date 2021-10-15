package pl.anime.scraper.config;

public class DiscordRestConstants {

    public static final String DISCORD_API_VERSION = "v9";
    public static final String DISCORD_API_URL = "https://discord.com/api/" + DISCORD_API_VERSION;
    public static final String CHANNEL_ID_PARAM = "channelId";
    public static final String MESSAGES = "/channels/{" + CHANNEL_ID_PARAM + "}/messages";

}
