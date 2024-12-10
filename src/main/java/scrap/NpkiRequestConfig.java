package scrap;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;


@Getter(value = AccessLevel.PACKAGE)
public class NpkiRequestConfig extends BaseRequestConfig {

    private String HTTP_PROTOCOL = "https";
    private String HTTP_HOST = "www.hometax.go.kr";
    private String HTTP_ENDPOINT = "/wqAction.do?actionId=ATXPPZXA001R01&screenId=UTXPPABA01";
    private int HTTP_PORT = 8080;
    private HttpMethod HTTP_METHOD = HttpMethod.POST;

    @Override
    public UrlEncodedFormEntity setEntity(String pkcEncSsn) {
        CeritificateManager certManager = CeritificateManager.getInstance();
        try {
            String signedData = certManager.generateAndVerifySignature(pkcEncSsn);
            String logSignature = certManager.getLogSignature(pkcEncSsn, signedData);

            return new UrlEncodedFormEntity(
                    Arrays.asList(
                            new BasicNameValuePair("cert", certManager.getPublicKeyPem()),
                            new BasicNameValuePair("logSgnt", Base64.getEncoder().encodeToString(logSignature.getBytes())),
                            new BasicNameValuePair("pkcLgnClCd", "04"),
                            new BasicNameValuePair("pkcLoginYnImpv", "Y"),
                            new BasicNameValuePair("randomEnc", certManager.getRValue())
                    )
            );

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> HttpClientResponseHandler<T> getResponseHandler() {
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

    @Override
    HttpClientContext getHttpClientContext() {
        return null;
    }

}
