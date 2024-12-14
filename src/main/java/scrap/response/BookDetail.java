package scrap.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class BookDetail {
    private final String bookCode;
    private final String bookTitle;
    private final String booSubtitle;
    private final String index;
    private final String year;

    private final String hdThumbnailUrl;
    private final String xlargeThumbnailUrl;
    private final String largeThumbnailUrl;
    private final String mediumThumbnailUrl;
    private final String smallThumbnailUrl;

    private final List<String> authors;
    private final List<String> nations;
    private final List<String> genres;
    private final List<String> externalUrls;

    private final String bookDescription;
    private final String publisherDescription;
    private final String authorDescription;

    private final String ratingsAvg;
    private final String ratingsCount;
    private final String wishesCount;
}
