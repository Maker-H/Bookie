package web.bookie.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Review {

    @NotNull(message = "have to choose one user")
    private User user;

    @NotNull(message = "have to choose one book")
    private Book book;

    @NotBlank(message = "have to enter review")
    private String review;

    @Override
    public String toString() {
        return "review {" + "\n" +
                user.toString() + "\n" +
                book.toString() + "\n" +
                review + "}";
    }
}
