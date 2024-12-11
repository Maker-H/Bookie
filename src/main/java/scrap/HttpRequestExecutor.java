package scrap;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;

public class HttpRequestExecutor {

    public static <T> T execute(HttpHost httpHost,
                                ClassicHttpRequest httpMethod,
                                AbstractHttpEntity entity,
                                HttpClientContext httpClientContext,
                                HttpClientResponseHandler<T> responseHandler) {

        httpMethod.setEntity(entity);

        try (CloseableHttpClient client = HttpClientProvider.getHttpClient()) {
            return client.execute(httpHost, httpMethod, httpClientContext, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("HTTP 요청 실패: " + e.getMessage(), e);
        }
    }

    public static <T> T execute(HttpHost httpHost,
                                ClassicHttpRequest httpMethod,
                                HttpClientContext httpClientContext,
                                HttpClientResponseHandler<T> responseHandler) {

        try (CloseableHttpClient client = HttpClientProvider.getHttpClient()) {
            return client.execute(httpHost, httpMethod, httpClientContext, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
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

        try (CloseableHttpClient client = HttpClientProvider.getHttpClient()) {
            return client.execute(httpHost, httpMethod, clientContext, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("HTTP 요청 실패: " + e.getMessage(), e);
        }
    }

}
