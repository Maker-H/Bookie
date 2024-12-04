package web.bookie.dto.request;

import lombok.Data;
import web.bookie.domain.UserEntity;

@Data
public class UserRequestDTO implements BaseRequestDto<UserEntity>{

    private String id;

    private String password;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(this.getId())
                .password(this.getPassword())
                .build();
    }
}
