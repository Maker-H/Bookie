package web.bookie.dto.response;

import lombok.Builder;
import lombok.Data;
import web.bookie.domain.UserEntity;

@Data
@Builder
public class UserResponseDTO {

    private String userTsid;

    public static UserResponseDTO from(UserEntity userEntity) {
        return UserResponseDTO.builder().userTsid(userEntity.getTsid()).build();
    }
}
