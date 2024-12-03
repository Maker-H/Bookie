package web.bookie.util.api;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import web.bookie.error.CustomCommonException;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
public class ApiErrorResponse {

    private String errorName;
    private String errorMessage;
    private int errorCode;
    private LocalDateTime time;

    public ApiErrorResponse(Exception e) {
        this.errorName = e.getClass().getName();
        this.errorMessage = e.getMessage();
        this.time = LocalDateTime.now();
    }

    public ApiErrorResponse(CustomCommonException e) {
        this.errorName = e.getErrorName();
        this.errorMessage = e.getErrorMessage();
        this.errorCode = e.getErrorCode();
        this.time = LocalDateTime.now();
    }

}
