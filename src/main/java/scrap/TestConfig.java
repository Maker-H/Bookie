package scrap;

import lombok.Getter;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

@Getter
public class TestConfig extends BaseRequestConfig {
    private String HTTP_PROTOCOL = "https";
    private String HTTP_HOST = "www.hometax.go.kr";
    private String HTTP_ENDPOINT = "/wqAction.do?actionId=ATXPPZXA001R01&screenId=UTXPPABA01";
    private int HTTP_PORT = 443;
    private HttpMethod HTTP_METHOD = HttpMethod.POST;

    @Override
    HttpClientContext getHttpClientContext() {
        return null;
    }

    @Override
    public AbstractHttpEntity setEntity() {
        return new StringEntity("{}<nts<nts>nts>1562rNFE0uv0jPXUbQJjHpHtNe3QFgM9WSNf1YpxIN404");
    }

    @Override
    protected <T> HttpClientResponseHandler<T> getResponseHandler() {
        return (HttpClientResponseHandler<T>) httpResponse -> {
            int statusCode = httpResponse.getCode();
            System.out.println("Status Code: " + statusCode);

            if (statusCode >= 200 && statusCode < 300) {
                String responseContent = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(responseContent);
                return (T) responseContent;
            } else {
                throw new IllegalStateException("Unexpected status code: " + statusCode);
            }
        };
    }


}
