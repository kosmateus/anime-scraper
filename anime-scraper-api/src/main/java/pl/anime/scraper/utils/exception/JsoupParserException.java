package pl.anime.scraper.utils.exception;

import java.util.function.Supplier;
import lombok.Getter;

@Getter
public class JsoupParserException extends RuntimeException {

    private final ErrorCode errorCode;

    public static Supplier<JsoupParserException> of(ErrorCode errorCode){
        return ()-> new JsoupParserException(errorCode);
    }

    public JsoupParserException(ErrorCode errorCode) {
        super("Invalid parsing of web page. Most probably API needs to be updated. Contact us to fix the problem.");
        this.errorCode = errorCode;
    }
}
