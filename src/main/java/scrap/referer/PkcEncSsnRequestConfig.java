//package scrap.referer;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.Getter;
//import org.apache.hc.client5.http.cookie.BasicCookieStore;
//import org.apache.hc.client5.http.cookie.Cookie;
//import org.apache.hc.core5.http.io.entity.StringEntity;
//import scrap.config.BaseRequestConfig;
//import scrap.http.HttpMethod;
//import scrap.http.HttpResponseWrapper;
//import scrap.referer.PkcEncSsnResponse;
//import scrap.util.CookieManager;
//
//import java.util.Map;
//import java.util.function.Function;
//
//
//@Getter
//public class PkcEncSsnRequestConfig extends BaseRequestConfig<PkcEncSsnResponse> {
//    private final String HTTP_PROTOCOL = "https";
//    private final String HTTP_HOST = "www.hometax.go.kr";
//    private final String HTTP_ENDPOINT = "/wqAction.do?actionId=ATXPPZXA001R01&screenId=UTXPPABA01";
//    private final int HTTP_PORT = 443;
//    private final HttpMethod HTTP_METHOD = HttpMethod.POST;
//
//    {
//        initializeHttpHost();
//        initializeHttpMethod();
//        initializeEntity();
//        initializePkcEncSsnResponseHandler();
//    }
//
//    public PkcEncSsnRequestConfig() {
//    }
//
//    public void initializeEntity() {
//        setEntity(new StringEntity("{}<nts<nts>nts>1562rNFE0uv0jPXUbQJjHpHtNe3QFgM9WSNf1YpxIN404"));
//    }
//
//    private void initializePkcEncSsnResponseHandler() {
//
//        Function<HttpResponseWrapper, PkcEncSsnResponse> pkcFunction = responseWrapper -> {
//
//            String pckEncSsn;
//            try {
//                pckEncSsn = objectMapper.readTree(responseWrapper.getResponseBody()).path(HomeTax.pkcEncSsn.name()).asText();
//
//                if (pckEncSsn.isEmpty()){
//                    throw new IllegalArgumentException("cannot get pkcEncSsn value whlie parsing response body in pkcEncSsn config");
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException("error while parsing pkcencssn");
//            }
//
//            BasicCookieStore pkcCookieStore =CookieManager.createCookieStoreFromHttpHeaders(responseWrapper.getHeaders());
//
//            final String txpp = CookieManager.findCookieValue(pkcCookieStore, HomeTax.TXPPsessionID.name());
//            final String wmonid = CookieManager.findCookieValue(pkcCookieStore, HomeTax.WMONID.name());
//
//            Map<String, Cookie> cookieMap = CookieManager.extractCookiesToMap(pkcCookieStore);
//
//            return new PkcEncSsnResponse(pckEncSsn, txpp, wmonid, cookieMap);
//
//        };
//
//        createResponseHandler(pkcFunction);
//    }
//}
