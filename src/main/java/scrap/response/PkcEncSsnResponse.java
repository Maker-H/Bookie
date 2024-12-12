package scrap.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hc.client5.http.cookie.Cookie;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public class PkcEncSsnResponse {

    private final String pkcEncSsn;
    private final String TXPPsessionID;
    private final String WMONID;
    private final Map<String, Cookie> cookies;

}
