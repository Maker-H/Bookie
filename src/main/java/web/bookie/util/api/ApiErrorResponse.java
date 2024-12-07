package web.bookie.util.api;

import lombok.Getter;
import lombok.ToString;
import web.bookie.error.CustomCommonException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Getter
@ToString
public class ApiErrorResponse {

    private String errorType;
    private String errorName;
    private String errorMessage;
    private int errorCode;
    private LocalDateTime time;

    public ApiErrorResponse(Exception e) {
        this.errorType = e.getClass().getName();
        this.errorMessage = e.getMessage();
        this.time = LocalDateTime.now();
    }

    public ApiErrorResponse(CustomCommonException e) {
        this.errorType = e.getErrorType();
        this.errorName = e.getErrorName();
        this.errorMessage = e.getErrorMessage();
        this.errorCode = e.getErrorCode();
        this.time = LocalDateTime.now();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApiErrorResponse otherResponse = (ApiErrorResponse) o;

        boolean isTimeEqual;
        if (this.time != null && otherResponse.time != null) {
            LocalDateTime thisTime = this.time.truncatedTo(ChronoUnit.MINUTES);
            LocalDateTime otherTime = otherResponse.time.truncatedTo(ChronoUnit.MINUTES);

            isTimeEqual = thisTime.equals(otherTime);
        } else {
            isTimeEqual = (this.time == otherResponse.time);
        }

        return  Objects.equals(this.errorName, otherResponse.errorName) &&
                Objects.equals(this.errorMessage, otherResponse.errorMessage) &&
                this.errorCode == otherResponse.errorCode &&
                isTimeEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                errorName,
                errorMessage,
                errorCode,
                time != null ? time.truncatedTo(ChronoUnit.MINUTES) : null
        );
    }

}
