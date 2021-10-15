package pl.anime.scraper.utils.http;

import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class ErrorDetails {

    private final String code;
    private final String message;
    private final String errorName;
    private final Map<String, String> paramMap;

    private ErrorDetails(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.errorName = builder.errorName;
        this.paramMap = builder.paramMap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ErrorDetails empty(){
        return builder().build();
    }

    public static ErrorDetails mappingError(){
        return builder().withCode("500").withMessage("Couldn't map response to given class.").build();
    }

    public static final class Builder {

        private String code;
        private String message;
        private String errorName;
        private Map<String, String> paramMap;

        private Builder() {
        }

        public Builder withCode(String code) {
            this.code = code;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withErrorName(String errorName) {
            this.errorName = errorName;
            return this;
        }

        public Builder withParamMap(Map<String, String> paramMap) {
            this.paramMap = paramMap;
            return this;
        }

        public ErrorDetails build() {
            return new ErrorDetails(this);
        }
    }
}
