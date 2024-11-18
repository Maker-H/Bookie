package web.bookie.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class Book {

    @NotBlank
    private String bookName;

    @NotBlank
    private String bookAuthor;

    private String bookDescription;

    private MultipartFile bookImage;

    @Override
    public String toString() {

        boolean hasImage = (bookImage != null);

        return "[book] name: " + bookName + ", " +
                "author: " + bookAuthor + ", " +
                "hasImage: " + hasImage + ", " +
                "description: " + bookDescription;
    }
}
