package web.bookie.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import web.bookie.util.api.ApiErrorResponse;
import web.bookie.util.api.ApiResponseBuilder;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    ApiResponseBuilder apiResponseBuilder;

    @ExceptionHandler(BookieException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomException(CustomCommonException ex) {
        return apiResponseBuilder.sendCustomError(ex);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiErrorResponse> handleSQLException(SQLException ex) {
        return apiResponseBuilder.sendCheckedError(ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        return apiResponseBuilder.sendCheckedError(ex);
    }

}

