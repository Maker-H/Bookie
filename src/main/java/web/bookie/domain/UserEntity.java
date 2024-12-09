package web.bookie.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import web.bookie.dto.response.UserResponseDTO;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder(builderMethodName = "hiddenBuilder")
//@ToString
@Entity(name = "APPUSER")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity<UserResponseDTO> {

    private String id;

    private String password;
    @OneToMany(
            mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
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

    @Override
    public UserResponseDTO toResponseDto() {
        return UserResponseDTO.getInstance(getTsid());
    }

    public static UserEntityBuilder builder(String id, String password) {
        return new UserEntityBuilder().id(id).password(password);
    }

    private static UserEntityBuilder hiddenBuilder() {
        return new UserEntityBuilder();
    }
}
