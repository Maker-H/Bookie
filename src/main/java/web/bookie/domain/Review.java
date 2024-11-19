package web.bookie.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Review {

    @NotNull(message = "have to choose one user")
    private User reviewUser;

    @NotNull(message = "have to choose one book")
    private Book book;

    @NotBlank(message = "have to enter review")
    private String review;

    @Override
    public String toString() {
        return "Review{" +
                "user=" + (reviewUser != null ? reviewUser.getId() : "null") +
                ", book=" + (book != null ? book.getBookName() : "null") +
                ", review='" + review + '\'' +
                '}';
    }
}
