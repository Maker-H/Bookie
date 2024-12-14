package scrap.config;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.apache.hc.core5.http.ClassicHttpRequest;
import scrap.http.HttpMethod;
import scrap.http.HttpResponseWrapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
public class WatchaCommentConfig extends BaseRequestConfig<JsonNode> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "pedia.watcha.com";
//      "next_uri" : "/api/contents/byLKj8M/comments?filter=all&order=popular&page=2&size=8",
    private String HTTP_ENDPOINT = "/api/contents/%s/comments?page=%d&size=%d";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        initializeHttpHost();
        initializeJsonNodeResponseHandler();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaCommentConfig(String bookCode, int page, int size) {
        this.HTTP_ENDPOINT = String.format(HTTP_ENDPOINT, bookCode, page, size);
        initializeWatchaHttpMethod();
    }

    private void initializeBookResponseHandler() {

    }


}
