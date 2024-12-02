package web.bookie.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommonColumn {

    @Id
    @Tsid
    private String tsid;

    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;

    @PrePersist
    private void createdOn() {
        if (createdOn == null) {
            createdOn = LocalDateTime.now();
        }
    }

}
