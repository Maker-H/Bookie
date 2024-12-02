package web.bookie.data;

import org.springframework.data.jpa.repository.JpaRepository;
import web.bookie.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, String> {

}
