package web.bookie.exceptions.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import web.bookie.exceptions.BookieException;

@Getter
public enum ParseError {

    PUBLISHDATE_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 100, "error while parsing date in BookRequestDTO, format: 2024/01/01");

    private final HttpStatus statusCode;
    private final int errorCode;
    private final String errorMsg;

    private ParseError(HttpStatus statusCode, int errorCode, String errorMsg) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public void throwException() throws BookieException {
        throw new BookieException(statusCode, this.getClass().getSimpleName(), this.name(), errorCode, errorMsg);
    }

    public BookieException toException() {
        return new BookieException(statusCode, this.getClass().getSimpleName(), this.name(), errorCode, errorMsg);
    }

}
