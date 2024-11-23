package web.bookie.data;

import web.bookie.domain.Review;

public interface ReviewRepository {
    public Review save(Review review);
}
