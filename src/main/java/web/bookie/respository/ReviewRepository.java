package web.bookie.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.bookie.domain.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, String> {

}
