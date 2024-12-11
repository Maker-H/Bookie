package scrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.List;


@Getter
public class PkcEncSsnRequestConfig extends BaseRequestConfig<PkcEncSsnVO> {
    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "www.hometax.go.kr";
    private final String HTTP_ENDPOINT = "/wqAction.do?actionId=ATXPPZXA001R01&screenId=UTXPPABA01";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.POST;

    {
        initializeHttpHost();
        initializeHttpMethod();
        initializeEntity();
        initializePkcEncSsnResponseHandler();
    }

    public PkcEncSsnRequestConfig() {
    }

    public void initializeEntity() {
        setEntity(new StringEntity("{}<nts<nts>nts>1562rNFE0uv0jPXUbQJjHpHtNe3QFgM9WSNf1YpxIN404"));
    }

    private void initializePkcEncSsnResponseHandler() {

        ObjectMapper objectMapper = new ObjectMapper();

        HttpClientResponseHandler<PkcEncSsnVO> responseHandler = httpResponse -> {
            final String pckEncSsn;
            String txpp = "";
            String wmonid = "";

            int statusCode = httpResponse.getCode();
            if (statusCode >= 200 && statusCode < 300) {

                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                try {
                    pckEncSsn = objectMapper.readTree(jsonResponse).path("pkcEncSsn").asText();
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to parse JSON response: " + e.getMessage(), e);
                }

                Header[] headers = httpResponse.getHeaders("Set-Cookie");
                BasicCookieStore basicCookieStore = CookieManager.parseHeaders(headers);
                List<Cookie> cookies = basicCookieStore.getCookies();

                for (Cookie c : cookies) {

                }

                return new PkcEncSsnVO(pckEncSsn, txpp, wmonid);
            } else {
                throw new IllegalStateException("Unexpected status code: " + statusCode);
            }
        };

        setResponseHandler(responseHandler);
    }
}
