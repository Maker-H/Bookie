package web.bookie.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(builderMethodName = "hiddenBuilder")
public class BookResponseDTO {

    private String bookTsid;

    public static BookResponseDTO getInstance(String bookTsid) {
        return new BookResponseDTOBuilder().bookTsid(bookTsid).build();
    }

}
