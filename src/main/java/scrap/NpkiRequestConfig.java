package scrap;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;


@Getter(value = AccessLevel.PACKAGE)
public class NpkiRequestConfig extends BaseRequestConfig<JsonNode> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "www.hometax.go.kr";
    private final String HTTP_ENDPOINT = "/wqAction.do?actionId=ATXPPZXA001R01&screenId=UTXPPABA01";
    private final int HTTP_PORT = 8080;
    private final HttpMethod HTTP_METHOD = HttpMethod.POST;

    {
        initializeHttpHost();
        initializeHttpMethod();
        initializeJsonNodeResponseHandler();
    }

    public NpkiRequestConfig(String pkcEncSsn) {
        initializeEntity(pkcEncSsn);
    }

    public NpkiRequestConfig(JsonNode pkcEncSsnResponse) {
        String pkcEncSsn = pkcEncSsnResponse.path("pkcEncSsn").asText();
        initializeEntity(pkcEncSsn);
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
            throw new RuntimeException(e);
        }
    }

}
