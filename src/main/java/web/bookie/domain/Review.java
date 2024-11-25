package web.bookie.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@Entity(name = "REVIEW")
public class Review {

    @Id
    @Column(name = "UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uuid;

    @Column(name = "CREATEDAT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "MODIFIEDAT")
    private LocalDateTime modifiedAt;

    @NotBlank(message = "have to enter review")
    private String review;

    @ManyToOne
    @JoinColumn(name = "USER_UUID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOOK_UUID", nullable = false)
    private Book book;

    public void setUser(User user) {
        this.user = user;

        if (!user.getReviews().contains(this)) {
            user.getReviews().add(this);
        }
    }

    public void setBook(Book book) {
        this.book = book;

        if (!book.getReviews().contains(this)) {
            book.getReviews().add(this);
        }
    }

    @PrePersist
    private void createdAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "Review { \n" +
                user + "\n" +
                book + "\n" +
                "review=" + review + "\n"+
                "}" ;
    }
}
