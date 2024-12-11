package scrap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hc.client5.http.cookie.Cookie;

import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public class PkcEncSsnVO {


    private final String pkcEncSsn;
    private final String TXPPsessionID;
    private final String WMONID;
    private final Map<String, Cookie> cookieMap;


//    public enum Pkc {
//        PKC_ENC_SSN("pkcEncSsn"),
//        TXPP("TXPPsessionID"),
//        WMONID("WMONID");
//
//        @Getter private final String realName;
//
//        // Enum 생성자
//        Pck(String realName) {
//            this.realName = realName;
//        }
//    }

}
