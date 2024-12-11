package scrap;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;


@Getter(value = AccessLevel.PACKAGE)
public class NpkiRequestConfig extends BaseRequestConfig<JsonNode> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "www.hometax.go.kr";
    private final String HTTP_ENDPOINT = "/wqAction.do?actionId=ATXPPZXA001R01&screenId=UTXPPABA01";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.POST;

    {
        initializeHttpHost();
        initializeHttpMethod();
        initializeJsonNodeResponseHandler();
    }

    public NpkiRequestConfig() {
        final PkcEncSsnVO pkcEncSsnVO = HttpRequestExecutor.execute(new PkcEncSsnRequestConfig());
        initializeEntity(pkcEncSsnVO.getPkcEncSsn());
        initializeClientContext(pkcEncSsnVO);
    }

    private void initializeClientContext(PkcEncSsnVO pkcEncSsnVO) {

        BasicCookieStore cookieStore = new BasicCookieStore();

        Map<String, Cookie> pkcEncCookieMap = pkcEncSsnVO.getCookieMap();
        for (Cookie c : pkcEncCookieMap.values()) {
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
                                new BasicNameValuePair("cert", certManager.getPublicKeyPem()),
                                new BasicNameValuePair("logSgnt", Base64.getEncoder().encodeToString(logSignature.getBytes())),
                                new BasicNameValuePair("pkcLgnClCd", "04"),
                                new BasicNameValuePair("pkcLoginYnImpv", "Y"),
                                new BasicNameValuePair("randomEnc", certManager.getRValue())
                        )
                    )
            );

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("error when making request entity in npkirequest");
        }
    }

}
