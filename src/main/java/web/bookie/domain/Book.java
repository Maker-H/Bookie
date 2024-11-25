package web.bookie.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@NoArgsConstructor
@Entity(name = "BOOK")
public class Book {

    @Id
    @Column(name = "UUID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;

    @NotBlank
    @Column(name = "BOOKNAME", nullable = false)
    private String bookName;

    @NotBlank
    @Column(name = "BOOKAUTHOR", nullable = false)
    private String bookAuthor;

    @Column(name = "BOOKDESCRIPTION")
    private String bookDescription;

    @Lob
    @Column(name = "BOOKIMAGE")
    private byte[] bookImage;

    @Column(name = "CREATEDAT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "MODIFIEDAT")
    private LocalDateTime modifiedAt;

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

    @PrePersist
    private void createdAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Book(long uuid, String bookName, String bookAuthor, String bookDescription, byte[] bookImage, LocalDateTime createdAt) {
        this.uuid = uuid;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookDescription = bookDescription;
        this.bookImage = bookImage;
        this.createdAt = createdAt;
    }

    public Book(String bookName, String bookAuthor) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
    }

    @Override
    public String toString() {

        boolean hasImage = (bookImage != null);

        return "Book {" +
                "uuid: " + uuid + ", " +
                "name: " + bookName + ", " +
                "author: " + bookAuthor + ", " +
                "hasImage: " + hasImage + ", " +
                "description: " + bookDescription +
                "}";
    }
}
