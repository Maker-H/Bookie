package web.bookie.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity(name = "APPUSER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends CommonColumn {

    private String id;

    private String password;

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


    public String toString() {
        return "user {tsid: " + this.getTsid() + ", id: " + id + ", pwd: " + password + "}";
    }
}
