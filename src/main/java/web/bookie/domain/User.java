package web.bookie.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class User {

    @NotBlank(message = "user id is required")
    @Size(min = 3, message = "enter more than 3 char")
    private String id;

    @NotBlank(message = "user password is required")
    @Size(min = 3, message = "enter more than 3 char")
    private String pwd;

    public String toString() {
       return "[user] id: " + id + ", pwd: " + pwd;
   }
}
