package web.bookie.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private String uuid;

    @NotBlank
    private String bookName;

    @NotBlank
    private String bookAuthor;

    private String bookDescription;

    private MultipartFile bookImage;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Book(String bookName, String bookAuthor) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
    }

    @Override
    public String toString() {

        boolean hasImage = (bookImage != null);

        return "book {" +
                "uuid: " + uuid + ", " +
                "name: " + bookName + ", " +
                "author: " + bookAuthor + ", " +
                "hasImage: " + hasImage + ", " +
                "description: " + bookDescription +
                "}";
    }
}
