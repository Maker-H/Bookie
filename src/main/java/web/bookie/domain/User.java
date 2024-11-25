package web.bookie.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity(name = "APPUSER")
public class User {

    @Id
    @Column(name = "UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uuid;

    @NotBlank(message = "user id is required")
    @Size(min = 3, message = "enter more than 3 char")
    @Column(name = "USERID", nullable = false)
    private String userId;

    @NotBlank(message = "user password is required")
    @Size(min = 3, message = "enter more than 3 char")
    @Column(name = "USERPWD", nullable = false)
    private String userPwd;

    @Column(name = "CREATEDAT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "MODIFIEDAT")
    private LocalDateTime modifiedAt;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Review> reviews = new ArrayList<>();

    public void addReviews (Review review){
        if (!this.reviews.contains(review)) {
            this.reviews.add(review);
        }

        if (review.getUser() != this) {
            review.setUser(this);
        }
    }

    public User(long uuid, String userId, String userPwd, LocalDateTime createdAt) {
        this.uuid = uuid;
        this.userId = userId;
        this.userPwd = userPwd;
        this.createdAt = createdAt;
    }

    @PrePersist
    private void createdAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public String toString() {
        return "user {uuid: " + uuid + ", id: " + userId + ", pwd: " + userPwd + "}";
   }
}
