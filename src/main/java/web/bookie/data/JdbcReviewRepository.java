package web.bookie.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import web.bookie.domain.Review;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcReviewRepository implements ReviewRepository{

    private JdbcTemplate jdbc;

    public JdbcReviewRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Review save(Review review) {
        review.setCreatedAt(LocalDateTime.now());

        PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
                "insert into Review (appUser, book, review, createdAt) values (?, ?, ?, ?)",
                Types.BIGINT,
                Types.BIGINT,
                Types.VARCHAR,
                Types.TIMESTAMP
        ).newPreparedStatementCreator(
                List.of(
                        review.getUser().getUuid(),
                        review.getBook().getUuid(),
                        review.getReview(),
                        Timestamp.valueOf(review.getCreatedAt())
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);

        return review;
    }
}
