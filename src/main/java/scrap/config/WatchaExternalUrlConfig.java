package scrap.config;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.NameValuePair;
import scrap.http.HttpMethod;
import scrap.http.HttpResponseWrapper;
import scrap.util.CookieManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
public class WatchaExternalUrlConfig extends BaseRequestConfig<String> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "redirect.watcha.com";
    private String HTTP_ENDPOINT = "/galaxy/%s";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        initializeHttpHost();
        initializeExternalResponseHandler();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaExternalUrlConfig(String externalBookCode) {
        this.HTTP_ENDPOINT = String.format(HTTP_ENDPOINT, externalBookCode);
        initializeWatchaHttpMethod();
    }

    protected void initializeExternalResponseHandler() {

        Function<HttpResponseWrapper, String> LocationHeaderResponse = responseWrapper ->
                Arrays.stream(responseWrapper.getHeaders())
                        .filter(header -> "location".equals(header.getName()))
                        .map(NameValuePair::getValue)
                        .findFirst()
                        .orElse("");

        createResponseHandler(LocationHeaderResponse);

    }


}
