package scrap.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import hs.http.HttpMethod;
import hs.http.HttpResponseWrapper;
import hs.type.HomeTax;
import hs.util.CookieManager;
import hs.vo.PkcEncSsnVO;
import lombok.Getter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.util.Map;
import java.util.function.Function;


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

        Function<HttpResponseWrapper, PkcEncSsnVO> pkcFunction = responseWrapper -> {


            String pckEncSsn = null;
            try {
                pckEncSsn = objectMapper.readTree(responseWrapper.getResponseBody()).path(HomeTax.pkcEncSsn.name()).asText();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("error while parsing pkcencssn");
            }

            Header[] headers = responseWrapper.getHeaders("Set-Cookie");
            BasicCookieStore pkcCookieStore = CookieManager.parseHeaders(headers);

            final String txpp = CookieManager.getCookieValue(pkcCookieStore, HomeTax.TXPPsessionID.name());
            final String wmonid = CookieManager.getCookieValue(pkcCookieStore, HomeTax.WMONID.name());

            Map<String, Cookie> cookieMap = CookieManager.getCookieMap(pkcCookieStore);

            return new PkcEncSsnVO(pckEncSsn, txpp, wmonid, cookieMap);

        };

        createBaseResponseHandler(pkcFunction);
    }
}
