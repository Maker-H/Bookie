package scrap.config;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.apache.hc.core5.http.ClassicHttpRequest;
import scrap.http.HttpMethod;
import scrap.http.HttpRequestExecutor;
import scrap.http.HttpResponseWrapper;
import scrap.response.BookDetail;

import java.awt.print.Book;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
public class WatchaBookConfig extends BaseRequestConfig<BookDetail> {

    private String HTTP_PROTOCOL = "https";
    private String HTTP_HOST = "pedia.watcha.com";
    private String HTTP_ENDPOINT = "/api/contents/";
    private int HTTP_PORT = 443;
    private HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        initializeHttpHost();
        initializeBookHandler();
        initializeWatchaHttpMethod();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaBookConfig(String bookCode) {
        HTTP_ENDPOINT = HTTP_ENDPOINT + bookCode;
    }


    private void initializeBookHandler() {

        Function<HttpResponseWrapper, BookDetail> bookDetailFunction = responseWrapper -> {

            JsonNode resultNode = responseWrapper.getJsonNode().path("result");

            List<String> authors = Optional.ofNullable(resultNode.path("author_names"))
                    .filter(JsonNode::isArray)
                    .map(arrayNode -> StreamSupport.stream(arrayNode.spliterator(), false)
                            .map(JsonNode::asText)
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

            List<String> nations = Optional.ofNullable(resultNode.path("nations"))
                    .filter(JsonNode::isArray)
                    .map(arrayNode -> StreamSupport.stream(arrayNode.spliterator(), false)
                            .map(node -> node.path("name").asText())
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

            List<String> genres = Optional.ofNullable(resultNode.path("genres"))
                    .filter(JsonNode::isArray)
                    .map(arrayNode -> StreamSupport.stream(arrayNode.spliterator(), false)
                            .map(JsonNode::asText)
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());


            List<String> externalUrls = Optional.ofNullable(resultNode.path("external_services"))
                    .filter(JsonNode::isArray)
                    .map(arrayNode -> StreamSupport.stream(arrayNode.spliterator(), false)
                            .map(node -> {
                                String href = node.path("href").asText();
                                return HttpRequestExecutor.execute(new WatchaExternalUrlConfig(href));
                            })
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());

            return new BookDetail(
                    resultNode.path("code").asText(),
                    resultNode.path("title").asText(),
                    resultNode.path("subtitle").asText(),
                    resultNode.path("content").asText(),
                    resultNode.path("year").asText(),
                    resultNode.path("poster").path("hd").asText(),
                    resultNode.path("poster").path("xlarge").asText(),
                    resultNode.path("poster").path("large").asText(),
                    resultNode.path("poster").path("medium").asText(),
                    resultNode.path("poster").path("small").asText(),
                    authors,
                    nations,
                    genres,
                    externalUrls,
                    resultNode.path("description").asText(),
                    resultNode.path("publisher_description").asText(),
                    resultNode.path("author_description").asText(),
                    resultNode.path("ratings_agv").asText(),
                    resultNode.path("ratings_count").asText(),
                    resultNode.path("wishes_count").asText()
            );
        };

        createResponseHandler(bookDetailFunction);
    }


}
