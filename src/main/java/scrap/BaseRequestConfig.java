package scrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.*;
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

    protected void createBaseResponseHandler(Function<ClassicHttpResponse, T> handlerFunction) {
        this.responseHandler = httpResponse -> {
            int statusCode = httpResponse.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                return handlerFunction.apply(httpResponse);
            } else {
                throw new IllegalStateException("Unexpected status code: " + statusCode);
            }
        };
    }

    protected void initializeStringResponseHandler() {

        Function<ClassicHttpResponse, T> stringFunction = httpResponse -> {
            try {
                return (T) EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException | ParseException e) {
                throw new IllegalStateException("Failed to parse String response: " + e.getMessage(), e);
            }
        };

        createBaseResponseHandler(stringFunction);

    }


    protected void initializeJsonNodeResponseHandler() {

        ObjectMapper objectMapper = new ObjectMapper();

        Function<ClassicHttpResponse, T> jsonFunction = httpResponse -> {
            try {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return (T) objectMapper.readTree(jsonResponse);
            } catch (IOException | ParseException e) {
                throw new IllegalStateException("Failed to parse JSON response: " + e.getMessage(), e);
            }
        };

        createBaseResponseHandler(jsonFunction);

    }

    protected void initializeCookieResponseHandler() {

        Function<ClassicHttpResponse, T> cookieResponse = httpResponse -> {
            Header[] headers = httpResponse.getHeaders("Set-Cookie");
            BasicCookieStore basicCookieStore = CookieManager.parseHeaders(headers);

            return (T) basicCookieStore;
        };

        createBaseResponseHandler(cookieResponse);

    }

}
