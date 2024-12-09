package web.bookie.util.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import web.bookie.exceptions.CustomCommonException;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponseBuilder {

    public <T> ApiSuccessResponse sendSuccess(T data) {
        return new ApiSuccessResponse(data);
    }

    public ResponseEntity sendCustomError(CustomCommonException customException) {

        ApiErrorResponse errorBody = new ApiErrorResponse(customException);
        log.error(errorBody.toString());

        return ResponseEntity
                .status(customException.getStatusCode())
                .body(errorBody);
    }

    public ResponseEntity sendCheckedError(Exception exception) {

        ApiErrorResponse errorBody = new ApiErrorResponse(exception);
        log.error(errorBody.toString());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorBody);
    }

}
