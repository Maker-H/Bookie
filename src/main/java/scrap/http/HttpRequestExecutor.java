package scrap.http;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.protocol.HttpContext;
import scrap.config.BaseRequestConfig;
import scrap.util.CookieManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Stream;

public class HttpRequestExecutor {

    public static <T> T execute(HttpHost httpHost,
                                ClassicHttpRequest httpMethod,
                                AbstractHttpEntity entity,
                                HttpClientContext httpClientContext,
                                HttpClientResponseHandler<T> responseHandler) {

        httpMethod.setEntity(entity);

        CloseableHttpClient client = HttpClientProvider.getHttpClient();
        try {
            return client.execute(httpHost, httpMethod, httpClientContext, responseHandler);
        } catch (IOException e) {
            throw new IllegalStateException("HTTP 요청 실패: " + e.getMessage(), e);
        }
    }

    public static <T> T execute(HttpHost httpHost,
                                ClassicHttpRequest httpMethod,
                                HttpClientContext clientContext,
                                HttpClientResponseHandler<T> responseHandler) {

        CloseableHttpClient client = HttpClientProvider.getHttpClient();
        try {
            return client.execute(httpHost, httpMethod, clientContext, responseHandler);
        } catch (IOException e) {
            throw new IllegalStateException("HTTP 요청 실패: " + e.getMessage(), e);
        }
    }

    public static <T> T execute(BaseRequestConfig<T> requestConfig) {

        ClassicHttpRequest httpMethod = requestConfig.getHttpMethod();
        AbstractHttpEntity entity = requestConfig.getEntity();
        if (entity != null) {
            httpMethod.setEntity(entity);
        }

        HttpHost httpHost = requestConfig.getHttpHost();
        HttpClientContext clientContext = requestConfig.getClientContext();

        HttpClientResponseHandler<T> responseHandler = requestConfig.getResponseHandler();

        printLog(httpHost, httpMethod, entity, clientContext);

        CloseableHttpClient client = HttpClientProvider.getHttpClient();
        try {
            return client.execute(httpHost, httpMethod, clientContext, responseHandler);
        } catch (IOException e) {
            throw new IllegalStateException("HTTP 요청 실패: " + e.getMessage(), e);
        }

    }

    private static <T> void printLog(HttpHost httpHost,
                                     ClassicHttpRequest httpMethod,
                                     AbstractHttpEntity entity,
                                     HttpClientContext clientContext) {

        System.out.println("\n================================ HTTP REQUEST ================================");
        try {
            System.out.println("[Request Info]");
            System.out.println("   HTTP Method: " + httpMethod.getMethod());
            System.out.println("   Request URL: " + httpHost.getHostName() + httpMethod.getUri());
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while making request info log");
        }

        // 요청 헤더 출력

        // 요청 헤더 출력
        System.out.println("[Request Headers]");
        if(httpMethod.getHeaders().length != 0) {
            Arrays.stream(httpMethod.getHeaders()).forEach(header -> System.out.println("   " + header.getName() + ": " + header.getValue()));
        }

        if (clientContext != null) {
            CookieStore cookieStore = (CookieStore) clientContext.getAttribute(HttpClientContext.COOKIE_STORE);
            if (!cookieStore.getCookies().isEmpty()) {
                cookieStore.getCookies().stream()
                        .map(cookie -> new BasicHeader("Set-Cookie", cookie.toString()))
                        .forEach(header -> System.out.println("   " + header.getName() + ": " + header.getValue()));
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        if (entity != null) {
            System.out.println("[Request Body]");
            try {
                String requestBody = EntityUtils.toString(entity);

                try {

                    JsonNode jsonNode = objectMapper.readTree(requestBody);
                    String prettyString = jsonNode.toPrettyString();
                    System.out.println(prettyString);

                    int jsonEndIndex = requestBody.indexOf(prettyString.trim()) + prettyString.trim().length();
                    if (jsonEndIndex <= requestBody.length()) {
                        String extraContent = requestBody.substring(jsonEndIndex).trim();
                        if (!extraContent.isEmpty()) {
                            System.out.println("[Extra Content]");
                            System.out.println("   " + extraContent);
                        }
                    }

                } catch (JsonParseException e) {
                    System.out.println("   " + requestBody);
                }
            } catch (Exception e) {
                System.err.println("   Failed to log request body: " + e.getMessage());
            }
        }
        System.out.println("===============================================================================\n");
    }
}
