package scrap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
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
import java.util.function.Consumer;
import java.util.function.Function;


public abstract class BaseRequestConfig<T> {

    @Getter @Setter private String implClassName;

    protected abstract String getHTTP_PROTOCOL();
    protected abstract String getHTTP_HOST();
    protected abstract String getHTTP_ENDPOINT();
    protected abstract int getHTTP_PORT();
    protected abstract HttpMethod getHTTP_METHOD();

    @Getter private HttpHost httpHost;
    @Getter private ClassicHttpRequest httpMethod;
    @Getter private AbstractHttpEntity entity;
    @Getter private HttpClientContext clientContext;
    @Getter private HttpClientResponseHandler<T> responseHandler;

    ObjectMapper objectMapper = new ObjectMapper();

    protected void initializeHttpHost() {
        httpHost = new HttpHost(getHTTP_PROTOCOL(), getHTTP_HOST(), getHTTP_PORT());
    }

    protected void initializeCookieClientContext(CookieStore cookieStore) {

        HttpClientContext httpClientContext = new HttpClientContext();
        httpClientContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        this.clientContext = httpClientContext;
    }

    /* ===================================== HttpMethod 관련 메서드 =====================================*/

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

    protected void initializeHttpMethod(Function<ClassicHttpRequest, ClassicHttpRequest> function) {
        try {
            Class<?> methodClass = Class.forName(getHTTP_METHOD().getClassName());
            ClassicHttpRequest httpMethod =
                    (ClassicHttpRequest) methodClass
                            .getConstructor(String.class)
                            .newInstance(getHTTP_ENDPOINT());
            this.httpMethod = function.apply(httpMethod);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create HTTP method instance: " + e.getMessage(), e);
        }
    }

    protected void initializeWatchaHttpMethod() {

        Function<ClassicHttpRequest, ClassicHttpRequest> headerFunction = httpMethod -> {

            httpMethod.addHeader("X-Frograms-App-Code", "Galaxy");
            httpMethod.addHeader("X-Frograms-Client", "Galaxy-Web-App");
            httpMethod.addHeader("X-Frograms-Galaxy-Language", "ko");
            httpMethod.addHeader("X-Frograms-Galaxy-Region", "KR");
            httpMethod.addHeader("X-Frograms-Version", "2.1.0");

            return httpMethod;
        };

        initializeHttpMethod(headerFunction);
    }
    /* ===================================== Response Handler 관련 메서드 =====================================*/

    // Response Handler 템플릿
    protected void createResponseHandler(Function<HttpResponseWrapper, T> handlerFunction) {
        this.responseHandler = httpResponse -> {

            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper(
                    httpResponse.getCode(),
                    httpResponse.getHeaders(),
                    httpResponse.getEntity()
            );

            httpResponseWrapper.printLog();

            int statusCode = httpResponseWrapper.getCode();
            if ((statusCode >= 200 && statusCode < 300) || statusCode == 302) {
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
