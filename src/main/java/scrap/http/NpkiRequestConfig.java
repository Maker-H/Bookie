package scrap.http;

import hs.http.HttpMethod;
import hs.http.HttpRequestExecutor;
import hs.http.HttpResponseWrapper;
import hs.type.HomeTax;
import hs.util.CeritificateManager;
import hs.util.CookieManager;
import hs.vo.NpkiVO;
import hs.vo.PkcEncSsnVO;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.function.Function;


@Getter(value = AccessLevel.PACKAGE)
public class NpkiRequestConfig extends BaseRequestConfig<NpkiVO> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "www.hometax.go.kr";
    private final String HTTP_ENDPOINT = "/wqAction.do?actionId=ATXPPZXA001R01&screenId=UTXPPABA01";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.POST;

    {
        initializeHttpHost();
        initializeHttpMethod();
        initializeStringResponseHandler();
    }

    private NpkiRequestConfig() {}

    private NpkiRequestConfig(PkcEncSsnVO pkcEncSsnVO) {
        initializeEntity(pkcEncSsnVO.getPkcEncSsn());
        initializeClientContext(pkcEncSsnVO.getCookieMap());
    }

    public static NpkiRequestConfig createWithPkcEncSsn() {
        PkcEncSsnVO pkcEncSsnVO = HttpRequestExecutor.execute(new PkcEncSsnRequestConfig());
        return new NpkiRequestConfig(pkcEncSsnVO);
    }

    public static NpkiRequestConfig createWithExistingPkcEncSsn(PkcEncSsnVO pkcEncSsnVO) {
        return new NpkiRequestConfig(pkcEncSsnVO);
    }

    protected void initializeNpkiResponseHandler() {

        Function<HttpResponseWrapper, NpkiVO> npkiResponse = responseWrapper -> {
            Header[] headers = responseWrapper.getHeaders("Set-Cookie");
            BasicCookieStore npkiCookieStore = CookieManager.parseHeaders(headers);

            final String txpp = CookieManager.getCookieValue(npkiCookieStore, HomeTax.TXPPsessionID.name());
            final String wmonid = CookieManager.getCookieValue(npkiCookieStore, HomeTax.WMONID.name());

            Map<String, Cookie> cookieMap = CookieManager.getCookieMap(npkiCookieStore);

            return new NpkiVO(txpp, wmonid, cookieMap);
        };

        createBaseResponseHandler(npkiResponse);

    }

    private void initializeClientContext(Map<String, Cookie> pckCookieMap) {

        BasicCookieStore cookieStore = new BasicCookieStore();

        for (Cookie c : pckCookieMap.values()) {
            cookieStore.addCookie(c);
        }

        BasicClientCookie cookie = new BasicClientCookie("NTS_LOGIN_SYSTEM_CODE_P", "TXPP");
        CookieManager.addHomeTaxCookie(cookie);
        cookieStore.addCookie(cookie);


        // HttpClientContext에 쿠키 저장소 설정
        HttpClientContext httpClientContext = new HttpClientContext();
        httpClientContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        setClientContext(httpClientContext);

    }

    private void initializeEntity(String pkcEncSsn) {
        CeritificateManager certManager = CeritificateManager.getInstance();

        try {

            String signedData = certManager.generateAndVerifySignature(pkcEncSsn);
            String logSignature = certManager.getLogSignature(pkcEncSsn, signedData);

            setEntity(new UrlEncodedFormEntity(
                        Arrays.asList(
                                new BasicNameValuePair("logSgnt", Base64.getEncoder().encodeToString(logSignature.getBytes())),
                                new BasicNameValuePair("randomEnc", certManager.getRValue()),
                                new BasicNameValuePair("cert", certManager.getPublicKeyPem()),
                                new BasicNameValuePair("pkcLoginYnImpv", "Y"),
                                new BasicNameValuePair("pkcLgnClCd", "04")
                        )
                    )
            );

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("error when making request entity in npki request");
        }
    }

}
