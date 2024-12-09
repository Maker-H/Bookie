package web.bookie.dto.request;

import lombok.*;
import web.bookie.domain.UserEntity;

@Data
public class UserRequestDTO implements BaseRequestDTO<UserEntity> {

    private final String id;

    private final String password;

    public UserEntity toEntity() {
        return UserEntity.builder(id, password).build();
    }


    /*
        FOR TEST
    */
    public static UserRequestDTO getInstance(String id, String password) {
        return new UserRequestDTO(id, password);
    }

}
