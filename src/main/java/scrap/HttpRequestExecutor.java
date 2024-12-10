package scrap;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;

public class HttpRequestExecutor {
    public static <T> T send(HttpHost httpHost,
                             ClassicHttpRequest httpRequest,
                             HttpClientContext httpClientContext,
                             HttpClientResponseHandler<T> responseHandler) {

        try (CloseableHttpClient client = hs.http.HttpClientProvider.getHttpClient()) {
            return client.execute(httpHost, httpRequest, httpClientContext, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("HTTP 요청 실패: " + e.getMessage(), e);
        }
    }

    public static <T> T send(HttpHost httpHost,
                             ClassicHttpRequest httpRequest,
                             AbstractHttpEntity entity,
                             HttpClientContext httpClientContext,
                             HttpClientResponseHandler<T> responseHandler) {

        httpRequest.setEntity(entity);

        try (CloseableHttpClient client = hs.http.HttpClientProvider.getHttpClient()) {
            return client.execute(httpHost, httpRequest, httpClientContext, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("HTTP 요청 실패: " + e.getMessage(), e);
        }
    }

}
