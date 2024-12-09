package web.bookie.dto.request;

import lombok.*;
import web.bookie.domain.UserEntity;

@Data
@Builder
public class UserRequestDTO implements BaseRequestDTO<UserEntity> {

    private final String id;

    private final String password;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .password(password)
                .build();
    }

    /*
    For Test
    */
    public static UserRequestDTO getInstance(String id, String password) {
        return UserRequestDTO.builder()
                .id(id)
                .password(password)
                .build();
    }
}
