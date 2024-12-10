package scrap;

import lombok.Getter;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;


public abstract class BaseRequestConfig {

    abstract String getHTTP_PROTOCOL();
    abstract String getHTTP_HOST();
    abstract String getHTTP_ENDPOINT();
    abstract int getHTTP_PORT();
    abstract HttpMethod getHTTP_METHOD();

    @Getter private HttpHost httpHost;
    @Getter private ClassicHttpRequest httpMethod;
    @Getter private AbstractHttpEntity entity;

    protected void setHttpHost() {
        httpHost = new HttpHost(getHTTP_PROTOCOL(), getHTTP_HOST(), getHTTP_PORT());
    }

    protected void setHttpMethod() {
        try {
            Class<?> methodClass = Class.forName(getHTTP_METHOD().getClassName());
            httpMethod = (ClassicHttpRequest)
                    methodClass.getConstructor(String.class)
                    .newInstance(getHTTP_ENDPOINT());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create HTTP method instance: " + e.getMessage(), e);
        }
    }

    protected AbstractHttpEntity setEntity() {
        return null;
    }

    protected UrlEncodedFormEntity setEntity(String pkcEncSsn) {
        return null;
    }

    protected <T> HttpClientResponseHandler<T> getResponseHandler() {

        HttpClientResponseHandler<String> responseHandler = httpResponse -> {
            int statusCode = httpResponse.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                return EntityUtils.toString(httpResponse.getEntity());
            } else {
                throw new IllegalStateException("Unexpected status code: " + statusCode);
            }
        };

        return (HttpClientResponseHandler<T>) responseHandler;
    }

    abstract HttpClientContext getHttpClientContext();


//    public void start() {
//        try {
//            String response = HttpRequestExecutor.send(
//                    getHttpHost(),
//                    getHttpMethod(),
//                    getHttpClientContext(),
//                    getResponseHandler()
//            );
//
//            System.out.println("Response: " + response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
