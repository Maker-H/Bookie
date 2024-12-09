package web.bookie.dto.request;

import lombok.*;
import web.bookie.domain.BookEntity;

import web.bookie.domain.UserEntity;
import web.bookie.error.ParseError;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

@Data
@Builder(builderMethodName = "hiddenBuilder")
public class BookRequestDTO implements BaseRequestDTO<BookEntity>{

    private final String title;

    private final String author;

    private String description;

    private String thumbnailUrl;

    private String publishDate;

    private String isbn;

    public BookEntity toEntity() {
        if (publishDate != null) {
            StringTokenizer st = new StringTokenizer(publishDate, "/");

            try {
                // '/'로 잘랐을 때 3등분이 되지 않는 경우 에러 발생
                String strYear = st.nextToken();
                String strMonth = st.nextToken();
                String strDayOfMonth = st.nextToken();

                // 규격에 맞지 않으면 에러 발생 (연도 4자리, 월 2자리, 일 2자리)
                if (strYear.length() != 4 || strMonth.length() != 2 || strDayOfMonth.length() != 2) {
                    ParseError.PUBLISHDATE_PARSING_ERROR.throwException();
                }

                // 숫자로 파싱되지 않으면 에러 발생
                Integer.parseInt(strYear);
                Integer.parseInt(strMonth);
                Integer.parseInt(strDayOfMonth);

            } catch (NoSuchElementException | NumberFormatException e2) {
                ParseError.PUBLISHDATE_PARSING_ERROR.throwException();
            }
        }

        return BookEntity.builder(title, author)
                .isbn(isbn != null ? isbn : "")
                .description(description != null ? description : "")
                .thumbnailUrl(thumbnailUrl != null ? thumbnailUrl : "")
                .publishDate(publishDate != null ? publishDate : "")
                .build();
    }

    /*
        FOR TEST
    */
    public static BookRequestDTOBuilder builder(String title, String author) {
        return new BookRequestDTOBuilder()
                .title(title)
                .author(author);
    }

    private static BookRequestDTOBuilder hiddenBuilder() {
        return new BookRequestDTOBuilder();
    }


}
