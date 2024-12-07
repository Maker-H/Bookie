package web.bookie.util.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import web.bookie.error.AuthError;
import web.bookie.error.BookieException;
import web.bookie.error.CustomCommonException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApiResponseBuilderTest.TestConfig.class)
class ApiResponseBuilderTest {

    @Autowired
    ApiResponseBuilder apiResponseBuilder;

    @TestConfiguration
    @ComponentScan(basePackages = {"web.bookie.util.api", "web.bookie.error"})
    static class TestConfig {}

    @Test
    void sendSuccess() {
        ApiSuccessResponse actualResponse = apiResponseBuilder.sendSuccess("this?");
        ApiSuccessResponse expectedResponse = new ApiSuccessResponse("this?");

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void sendError_체크예외_확인() {
        IllegalAccessException checkException = new IllegalAccessException("this exception");

        ResponseEntity actualResponse = apiResponseBuilder.sendCheckedError(checkException);
        ApiErrorResponse actualBody = (ApiErrorResponse) actualResponse.getBody();
        ApiErrorResponse expectedBody = new ApiErrorResponse(checkException);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
        Assertions.assertEquals(expectedBody, actualBody);
    }

    @Test
    // GlobalExceptionHandlerTest에서 확인
    void sendError_커스텀예외_확인() {

//        AuthError.USER_NOT_VALID.throwException();
//
//        ResponseEntity actualResponse = apiResponseBuilder.sendCustomError(bookieException);
//        ApiErrorResponse actualBody = (ApiErrorResponse) actualResponse.getBody();
//        ApiErrorResponse expectedBody = new ApiErrorResponse((CustomCommonException) bookieException);
//
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
//        Assertions.assertEquals(expectedBody, actualBody);
    }

}