package pl.anime.scraper.utils.http;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;

public final class HttpRestClient {

    private static final HttpClient client;
    private static final Gson gson;

    static {

        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "Host");
        client = HttpClient.newHttpClient();
        gson = new Gson();
    }

    public static <T> ResponseHandler<T> get(RequestData requestData, Class<T> returnType) {
        var httpRequestBuilder = prepareHttpRequestBuilder(requestData);
        return executeRequest(() -> httpRequestBuilder.GET().build(), returnType);
    }

    public static <T> ResponseHandler<T> post(RequestData requestData, BodyPublisher bodyPublisher,
            Class<T> returnType) {
        var invocation = prepareHttpRequestBuilder(requestData);
        return executeRequest(() -> invocation.POST(bodyPublisher).build(), returnType);
    }

    public static <T> ResponseHandler<T> put(RequestData requestData, BodyPublisher bodyPublisher,
            Class<T> returnType) {
        var invocation = prepareHttpRequestBuilder(requestData);
        return executeRequest(() -> invocation.PUT(bodyPublisher).build(), returnType);
    }

    private static HttpRequest.Builder prepareHttpRequestBuilder(RequestData requestData) {
        String path = getPath(requestData);
        var uri = URI.create(path);
        var builder = HttpRequest.newBuilder()
                .uri(uri);

        for (Map.Entry<String, String> header : requestData.getHeaders().entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }

        return builder;
    }

    private static <T> ResponseHandler<T> executeRequest(Supplier<HttpRequest> executor, Class<T> clazz) {

        var request = executor.get();
        try {
            return handleResponseResult(request, clazz);
        } catch (IOException e) {
            return handleIOException(e);
        } catch (InterruptedException e) {
            return handleInterrruptedException(e);
        } catch (Exception e) {
            return handleGenericException(e);
        }

    }

    private static String getPath(RequestData requestData) {
        String path = requestData.getTarget() + requestData.getPath();
        for (Map.Entry<String, String> entry : requestData.getPathParams().entrySet()) {
            path = StringUtils.replace(path, StringUtils.replace("{0}", "0", entry.getKey()), entry.getValue());
        }

        if (!requestData.getQueryParams().isEmpty()) {
            path += "?" + createQueryParams(requestData.getQueryParams());
        }

        return path;
    }

    private static <T> ResponseHandler<T> handleResponseResult(HttpRequest request, Class<T> clazz)
            throws IOException, InterruptedException {
        var response = client.send(request, BodyHandlers.ofString());

        var body = response.body();

        var status = response.statusCode();
        var headers = response.headers();
        if (status < 400) {
            if (StringUtils.isNotBlank(body)) {
                return ResponseHandler.of(gson.fromJson(body, clazz), status, headers.map());
            }
            return ResponseHandler.emptyOk(status, headers.map());
        }

        return ResponseHandler.empty(status, headers.map(),
                EmptyReason.fromHttpStatus(status, ErrorDetails.builder().build()));
    }

    private static <T> ResponseHandler<T> handleIOException(IOException e) {
        return ResponseHandler.empty(800, null,
                EmptyReason.errorResponse(
                        ErrorDetails.builder()
                                .withMessage(e.getMessage())
                                .withErrorName("IOException")
                                .build()
                )
        );
    }

    private static <T> ResponseHandler<T> handleInterrruptedException(InterruptedException e) {
        return ResponseHandler.empty(801, null,
                EmptyReason.errorResponse(
                        ErrorDetails.builder()
                                .withMessage(e.getMessage())
                                .withErrorName("InterruptedException")
                                .build()
                )
        );
    }

    private static <T> ResponseHandler<T> handleGenericException(Exception e) {
        return ResponseHandler.empty(801, null, EmptyReason.errorResponse(
                ErrorDetails.builder().withMessage(e.getMessage())
                        .withErrorName("Unexpected exception during REST call").build()));
    }

    private static String createQueryParams(Map<String, String> queryParams) {
        return queryParams.entrySet()
                .stream()
                .collect(
                        StringBuilder::new,
                        (stringBuilder, entry) -> {
                            stringBuilder.append("&");
                            stringBuilder.append(entry.getKey());
                            stringBuilder.append("=");
                            stringBuilder.append(entry.getValue());
                        },
                        StringBuilder::append)
                .toString();
    }
}
