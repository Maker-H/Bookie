package web.bookie.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import web.bookie.exceptions.CustomCommonException;

public class BookieException extends RuntimeException implements CustomCommonException {

    private final String errorType;
    private final String errorName;
    private final HttpStatus statusCode;
    private final int errorCode;

    public BookieException(HttpStatus statusCode, String errorType, String errorName, int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorType = errorType;
        this.errorName = errorName;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    @Override
    public String getErrorType() { return errorType; }

    @Override
    public String getErrorName() {
        return errorName;
    }

    @Override
    public HttpStatus getStatusCode() {
        return statusCode;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.getMessage();
    }
}
