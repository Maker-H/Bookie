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
public class Review extends CommonColumn {

    private String content;

    private double rate;

    @ManyToOne
    @JoinColumn(name = "USER_TSID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOOK_TSID", nullable = false)
    private Book book;

    public void setUser(User user) {
        this.user = user;

        if (!user.getReviews().contains(this)) {
            user.getReviews().add(this);
        }
    }

    public void setBook(Book book) {
        this.book = book;

        if (!book.getReviews().contains(this)) {
            book.getReviews().add(this);
        }
    }


    @Override
    public String toString() {
        return "Review { \n" +
                user + "\n" +
                book + "\n" +
                "tsid=" + this.getTsid() + "\n" +
                "content=" + content + "\n"+
                "}" ;
    }
}
