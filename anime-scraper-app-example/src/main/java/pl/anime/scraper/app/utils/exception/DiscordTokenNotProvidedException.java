package pl.anime.scraper.app.utils.exception;

public class DiscordTokenNotProvidedException extends RuntimeException{

    public DiscordTokenNotProvidedException(String message) {
        super(message);
    }
}
