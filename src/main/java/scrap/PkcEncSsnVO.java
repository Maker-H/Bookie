package scrap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class PkcEncSsnVO {

    private final String pkcEncSsn;
    private final String TXPPsessionID;
    private final String WMONID;

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
