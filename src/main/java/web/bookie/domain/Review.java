package web.bookie.domain;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Review {

    private String userId;
    private String userPwd;

    private String bookName;
    private String bookAuthor;

    @NotBlank(message = "have to enter review")
    private String review;

    @Override
    public String toString() {
        return "Review {" +
                "user=" + userId +
                ", book=" + bookName +
                ", review=" + review ;
    }
}
