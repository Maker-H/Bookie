package web.bookie.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String uuid;

    @NotBlank(message = "user id is required")
    @Size(min = 3, message = "enter more than 3 char")
    private String userId;

    @NotBlank(message = "user password is required")
    @Size(min = 3, message = "enter more than 3 char")
    private String userPwd;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public String toString() {
        return "user {uuid: " + uuid + ", id: " + userId + ", pwd: " + userPwd + "}";
   }
}
