package pl.anime.scraper.utils.http;

import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class RequestData {

    private final String target;
    private final String path;
    private final Map<String, String> queryParams;
    private final MediaType mediaType;
    private final Map<String, String> headers;
    private final Map<String, String> pathParams;

    private RequestData(Builder builder) {
        this.target = builder.target;
        this.path = builder.path;
        this.queryParams = builder.queryParams;
        this.mediaType = builder.mediaType;
        this.headers = builder.headers;
        this.pathParams = builder.pathParams;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String target;
        private String path;
        private Map<String, String> queryParams;
        private MediaType mediaType;
        private Map<String, String> headers;
        private Map<String, String> pathParams;

        public Builder withTarget(String target) {
            this.target = target;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withQueryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder withMediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder withPathParams(Map<String, String> pathParams) {
            this.pathParams = pathParams;
            return this;
        }

        public RequestData build() {
            return new RequestData(this);
        }
    }
}
