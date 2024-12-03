package web.bookie.error;

import org.springframework.http.HttpStatus;

public class BookieException extends RuntimeException implements CustomCommonException{

    private final String errorName;
    private final HttpStatus statusCode;
    private final int errorCode;

    public BookieException(HttpStatus statusCode, String errorName, int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorName = errorName;
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

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
