package web.bookie.error;

import org.springframework.http.HttpStatus;

public enum BookieError {

    USER_NOT_VALID(HttpStatus.BAD_REQUEST, 100, "user is not valid");

    private final HttpStatus statusCode;
    private final int errorCode;
    private final String errorMsg;

    private BookieError(HttpStatus statusCode, int errorCode, String errorMsg) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public void throwException() throws BookieException {
        throw new BookieException(statusCode, this.name(), errorCode, errorMsg);
    }

}
