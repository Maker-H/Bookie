package web.bookie.error;

import org.springframework.http.HttpStatus;

public interface CustomCommonException {
    String getErrorName();

    HttpStatus getStatusCode();

    int getErrorCode();

    String getErrorMessage();
}
