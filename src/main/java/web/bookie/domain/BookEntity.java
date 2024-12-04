package web.bookie.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Builder
//@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookEntity extends BaseEntity<String> {

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
            mappedBy = "bookEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ReviewEntity> reviewEntities = new ArrayList<>();

    public void addReviews (ReviewEntity reviewEntity){
        if (!this.reviewEntities.contains(reviewEntity)) {
            this.reviewEntities.add(reviewEntity);
        }

        if (reviewEntity.getBookEntity() != this) {
            reviewEntity.setBookEntity(this);
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
    public String toResponseDto() {
        return null;
    }
}
