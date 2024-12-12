package scrap.http;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import scrap.config.BaseRequestConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

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
        httpMethod.setEntity(entity);

        HttpHost httpHost = requestConfig.getHttpHost();
        HttpClientContext clientContext = requestConfig.getClientContext();

        HttpClientResponseHandler<T> responseHandler = requestConfig.getResponseHandler();

        printLog(httpHost, httpMethod, entity, clientContext, responseHandler);

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
                                     HttpClientContext httpClientContext,
                                     HttpClientResponseHandler<T> responseHandler) {

        System.out.println("\n================================ HTTP REQUEST ================================");
        try {
            System.out.println("[Request Info]");
            System.out.println("   HTTP Method: " + httpMethod.getMethod());
            System.out.println("   Request URL: " + httpHost.getHostName() + httpMethod.getUri());
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while making request info log");
        }

        // 요청 헤더 출력
        if(httpMethod.getHeaders().length != 0) {
            System.out.println("[Request Headers]");
            Arrays.stream(httpMethod.getHeaders())
                    .forEach(header -> System.out.println("   " + header.getName() + ": " + header.getValue()));
        }

        if(entity !=null) {
            try {
                String requestBody = EntityUtils.toString(entity);
                System.out.println("[Request Body]");
                System.out.println("   " + requestBody);
            } catch (Exception e) {
                System.err.println("error while making response info log");
            }
        }
        System.out.println("===============================================================================\n");
    }
}
