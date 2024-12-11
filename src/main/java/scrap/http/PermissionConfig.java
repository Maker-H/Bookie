package scrap.http;

import com.fasterxml.jackson.databind.JsonNode;
import hs.http.HttpMethod;
import lombok.Getter;
import org.apache.hc.core5.http.io.entity.StringEntity;

@Getter
public class PermissionConfig extends BaseRequestConfig<JsonNode> {
    private String HTTP_PROTOCOL = "https";
    private String HTTP_HOST = "www.hometax.go.kr";
    private String HTTP_ENDPOINT = "/permission.do?screenId=index_pp";
    private int HTTP_PORT = 443;
    private HttpMethod HTTP_METHOD = HttpMethod.POST;

    {
        initializeHttpHost();
        initializeHttpMethod();
        initializeEntity();
        initializeJsonNodeResponseHandler();
    }

    public void initializeEntity() {
        setEntity(new StringEntity("{}<nts<nts>nts>1562rNFE0uv0jPXUbQJjHpHtNe3QFgM9WSNf1YpxIN404"));
    }


}
