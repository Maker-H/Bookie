package web.bookie.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity<T> {
    @Id
    @Tsid
    private String tsid;

    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;


    @PrePersist
    private void setCreatedOn() {
        if (createdOn == null) {
            createdOn = LocalDateTime.now().withNano(0);
        }
    }

    @PreUpdate
    private void setModifiedOn() {
        modifiedOn = LocalDateTime.now().withNano(0);;
    }

    public abstract T toResponseDto();

}
