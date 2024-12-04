package web.bookie.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity extends BaseEntity {

    private String content;

    private double rate;

    @ManyToOne
    @JoinColumn(name = "APPUSER_TSID", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "BOOK_TSID", nullable = false)
    private BookEntity bookEntity;

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;

        if (!userEntity.getReviewEntities().contains(this)) {
            userEntity.getReviewEntities().add(this);
        }
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;

        if (!bookEntity.getReviewEntities().contains(this)) {
            bookEntity.getReviewEntities().add(this);
        }
    }


    @Override
    public String toString() {
        return "Review { \n" +
                userEntity + "\n" +
                bookEntity + "\n" +
                "tsid=" + this.getTsid() + "\n" +
                "content=" + content + "\n"+
                "}" ;
    }
}
