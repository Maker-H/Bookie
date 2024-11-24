package web.bookie.data;

import org.springframework.data.repository.CrudRepository;
import web.bookie.domain.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
//    Review save(Review review);
}
