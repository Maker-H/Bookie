package web.bookie.domain;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class Review {

    private String uuid;

    private User user;
    private Book book;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @NotBlank(message = "have to enter review")
    private String review;

    @Override
    public String toString() {
        return "Review { \n" +
                user + "\n" +
                book + "\n" +
                "review=" + review + "}" ;
    }
}
