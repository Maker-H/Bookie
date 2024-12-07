package web.bookie.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthError {

    USER_NOT_VALID(HttpStatus.BAD_REQUEST, 100, "user is not valid");

    private final HttpStatus statusCode;
    private final int errorCode;
    private final String errorMsg;

    private AuthError(HttpStatus statusCode, int errorCode, String errorMsg) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public void throwException() throws BookieException {
        throw new BookieException(statusCode, this.getClass().getSimpleName(), this.name(), errorCode, errorMsg);
    }

}
