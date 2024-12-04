package web.bookie.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@ToString
@Entity(name = "APPUSER")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    private String id;

    private String password;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ReviewEntity> reviewEntities = new ArrayList<>();

    public void addReviews (ReviewEntity reviewEntity){
        if (!this.reviewEntities.contains(reviewEntity)) {
            this.reviewEntities.add(reviewEntity);
        }

        if (reviewEntity.getUserEntity() != this) {
            reviewEntity.setUserEntity(this);
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
