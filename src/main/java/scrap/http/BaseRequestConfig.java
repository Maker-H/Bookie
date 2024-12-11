package scrap.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import hs.http.HttpMethod;
import hs.http.HttpResponseWrapper;
import hs.util.CookieManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.function.Function;


public abstract class BaseRequestConfig<T> {

    abstract String getHTTP_PROTOCOL();
    abstract String getHTTP_HOST();
    abstract String getHTTP_ENDPOINT();
    abstract int getHTTP_PORT();
    abstract HttpMethod getHTTP_METHOD();

    @Getter private HttpHost httpHost;
    @Getter private ClassicHttpRequest httpMethod;
    @Getter @Setter private AbstractHttpEntity entity;
    @Getter @Setter private HttpClientContext clientContext;
    @Getter @Setter private HttpClientResponseHandler<T> responseHandler;

    ObjectMapper objectMapper = new ObjectMapper();

    protected void initializeHttpHost() {
        httpHost = new HttpHost(getHTTP_PROTOCOL(), getHTTP_HOST(), getHTTP_PORT());
    }

    protected void initializeHttpMethod() {
        try {
            Class<?> methodClass = Class.forName(getHTTP_METHOD().getClassName());
            httpMethod = (ClassicHttpRequest)
                    methodClass.getConstructor(String.class)
                    .newInstance(getHTTP_ENDPOINT());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create HTTP method instance: " + e.getMessage(), e);
        }
    }

    protected void createBaseResponseHandler(Function<HttpResponseWrapper, T> handlerFunction) {
        this.responseHandler = httpResponse -> {

            // 로그 출력: 상태 코드
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper(
                    httpResponse.getCode(),
                    httpResponse.getHeaders(),
                    EntityUtils.toString(httpResponse.getEntity())
            );

            httpResponseWrapper.printLog();

            int statusCode = httpResponseWrapper.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                return handlerFunction.apply(httpResponseWrapper);
            } else {
                throw new IllegalStateException("Unexpected status code: " + statusCode);
            }
        };
    }

    protected void initializeStringResponseHandler() {

        Function<HttpResponseWrapper, T> stringFunction = responseWrapper -> (T) responseWrapper.getResponseBody();

        createBaseResponseHandler(stringFunction);

    }

    protected void initializeJsonNodeResponseHandler() {

        Function<HttpResponseWrapper, T> jsonFunction = responseWrapper -> {
            try {
                String jsonResponse = responseWrapper.getResponseBody();
                return (T) objectMapper.readTree(jsonResponse);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to parse JSON response: " + e.getMessage(), e);
            }
        };

        createBaseResponseHandler(jsonFunction);

    }

    protected void initializeCookieResponseHandler() {

        Function<HttpResponseWrapper, T> cookieResponse = responseWrapper -> {
            Header[] headers = responseWrapper.getHeaders("Set-Cookie");
            BasicCookieStore basicCookieStore = CookieManager.parseHeaders(headers);

            return (T) basicCookieStore;
        };

        createBaseResponseHandler(cookieResponse);

    }

}
