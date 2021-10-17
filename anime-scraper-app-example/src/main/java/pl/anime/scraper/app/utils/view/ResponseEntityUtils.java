package pl.anime.scraper.app.utils.view;

import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import pl.anime.scraper.app.utils.exception.ApiHandlerException;
import pl.anime.scraper.utils.api.APIHandler;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseEntityUtils {

    public static <T> ResponseEntity<T> ofApi(Supplier<APIHandler<T>> apiHandler) {
        var handler = apiHandler.get();
        if (handler.isPresent()) {
            return ResponseEntity.ok(handler.getResult());
        } else if (handler.isOk()) {
            return ResponseEntity.noContent().build();
        }
        throw new ApiHandlerException(handler.getEmptyReason());
    }
}
