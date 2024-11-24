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

    @ManyToOne
    @JoinColumn(name = "USER_UUID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOOK_UUID", nullable = false)
    private Book book;

    @Column(name = "CREATEDAT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "MODIFIEDAT")
    private LocalDateTime modifiedAt;

    @NotBlank(message = "have to enter review")
    private String review;

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
