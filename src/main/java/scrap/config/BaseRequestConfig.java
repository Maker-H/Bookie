package scrap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import scrap.http.HttpMethod;
import scrap.http.HttpResponseWrapper;
import scrap.util.CookieManager;

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
    protected void createResponseHandler(Function<HttpResponseWrapper, T> handlerFunction) {
        this.responseHandler = httpResponse -> {

            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper(
                    httpResponse.getCode(),
                    httpResponse.getHeaders(),
                    httpResponse.getEntity()
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

    protected  Function<HttpResponseWrapper, T> initializeStringResponseHandler() {

        Function<HttpResponseWrapper, T> stringFunction
                = responseWrapper -> (T) responseWrapper.getResponseBody();

        createResponseHandler(stringFunction);

        return stringFunction;
    }

    protected Function<HttpResponseWrapper, T> initializeJsonNodeResponseHandler() {

        Function<HttpResponseWrapper, T> jsonFunction = responseWrapper -> {
            try {
                return (T) objectMapper.readTree(responseWrapper.getResponseBody());
            } catch (IOException e) {
                throw new IllegalStateException("Failed to parse JSON response: " + e.getMessage(), e);
            }
        };

        createResponseHandler(jsonFunction);

        return jsonFunction;
    }

    protected void initializeCookieResponseHandler() {

        Function<HttpResponseWrapper, T> cookieResponse
                = responseWrapper -> (T) CookieManager.createCookieStoreFromHttpHeaders(responseWrapper.getHeaders());;

        createResponseHandler(cookieResponse);

    }

}
