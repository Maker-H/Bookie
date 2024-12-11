package scrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;


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

    protected void initializeStringResponseHandler() {

        @SuppressWarnings("unchecked")
        HttpClientResponseHandler<T> responseHandler = httpResponse -> {
            int statusCode = httpResponse.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                return (T) EntityUtils.toString(httpResponse.getEntity());
            } else {
                throw new IllegalStateException("Unexpected status code: " + statusCode);
            }
        };

        this.responseHandler = responseHandler;
    }

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static CookieStore basicCookieStore = new BasicCookieStore();

    protected void initializeJsonNodeResponseHandler() {

        @SuppressWarnings("unchecked")
        HttpClientResponseHandler<T> responseHandler = httpResponse -> {
            int statusCode = httpResponse.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                try {
                    return (T) objectMapper.readTree(jsonResponse);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to parse JSON response: " + e.getMessage(), e);
                }
            } else {
                throw new IllegalStateException("Unexpected status code: " + statusCode);
            }
        };

        this.responseHandler = responseHandler;
    }

    protected void initializeCookieResponseHandler() {

        HttpClientResponseHandler<T> responseHandler = httpResponse -> {
            int statusCode = httpResponse.getCode();
            if (statusCode >= 200 && statusCode < 300) {

                Header[] headers = httpResponse.getHeaders("Set-Cookie");

                for (Header header : headers) {
                    String cookieValue = header.getValue();

                    BasicClientCookie cookie = CookieManager.parseString(cookieValue);
                    basicCookieStore.addCookie(cookie);
                }

                return (T) basicCookieStore;

            } else {
                throw new IllegalStateException("Unexpected status code: " + statusCode);
            }
        };

        this.responseHandler = responseHandler;
    }

}
