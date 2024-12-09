package web.bookie.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import web.bookie.dto.request.UserRequestDTO;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookResponseDTO {

    private String bookTsid;

    public static BookResponseDTO getInstance(String bookTsid) {
        return new BookResponseDTO(bookTsid);
    }

}
