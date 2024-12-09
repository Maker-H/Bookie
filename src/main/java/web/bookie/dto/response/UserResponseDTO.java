package web.bookie.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;
import web.bookie.dto.request.UserRequestDTO;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDTO {

    private String userTsid;

    public static UserResponseDTO getInstance(String userTsid) {
        return new UserResponseDTO(userTsid);
    }

}
