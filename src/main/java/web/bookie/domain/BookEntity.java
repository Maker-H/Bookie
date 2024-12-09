package web.bookie.domain;

import jakarta.persistence.*;
import lombok.*;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.dto.response.BookResponseDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Builder(builderMethodName = "hiddenBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookEntity extends BaseEntity<BookResponseDTO> {

    private String title;

    private String author;

    private String isbn;

    @Lob private String description;

    private String thumbnailUrl;

    private String publishDate;

    @OneToMany(
            mappedBy = "bookEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<ReviewEntity> reviewEntities = new ArrayList<>();

    public void addReviews (ReviewEntity reviewEntity){
        if (!this.reviewEntities.contains(reviewEntity)) {
            this.reviewEntities.add(reviewEntity);
        }

        if (reviewEntity.getBookEntity() != this) {
            reviewEntity.setBookEntity(this);
        }

    }

    @Override
    public BookResponseDTO toResponseDto() {
        return BookResponseDTO.getInstance(getTsid());
    }

    public static BookEntityBuilder builder(String title, String author) {
        return new BookEntityBuilder().title(title).author(author);
    }

    private static BookEntityBuilder hiddenBuilder() {
        return new BookEntityBuilder();
    }
}
