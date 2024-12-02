package web.bookie.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends CommonColumn {

    private String title;

    private String author;

    private String isbn;

    @Lob
    private String description;

    private String thumbnailUrl;

    private LocalDateTime publishDate;

//    orphanRemoval = true: 부모와 관계가 끊어진 자식 엔티티는 자동으로 삭제.
//    CascadeType.ALL: 부모 엔티티(Book)의 변경 사항이 자식 엔티티(Review)에 전파됨.
    @OneToMany(
            mappedBy = "book",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Review> reviews = new ArrayList<>();

    public void addReviews (Review review){
        if (!this.reviews.contains(review)) {
            this.reviews.add(review);
        }

        if (review.getBook() != this) {
            review.setBook(this);
        }
    }


//    public Book(long uuid, String bookName, String bookAuthor, String bookDescription, byte[] bookImage, LocalDateTime createdAt) {
//        this.uuid = uuid;
//        this.bookName = bookName;
//        this.bookAuthor = bookAuthor;
//        this.bookDescription = bookDescription;
//        this.bookImage = bookImage;
//        this.createdAt = createdAt;
//    }
//
//    public Book(String bookName, String bookAuthor) {
//        this.bookName = bookName;
//        this.bookAuthor = bookAuthor;
//    }

    @Override
    public String toString() {

        boolean hasImage = (thumbnailUrl != null);

        return "Book {" +
                "tsid: " + this.getTsid() + ", " +
                "name: " + title + ", " +
                "author: " + author + ", " +
                "hasImage: " + hasImage + ", " +
                "description: " + description +
                "}";
    }
}
