package web.bookie.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.bookie.util.api.ApiResponseBuilder;
import web.bookie.util.api.ApiSuccessResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final ApiResponseBuilder responseBuilder;

    @GetMapping(value = "/ping")
    public ApiSuccessResponse ping() {
        return responseBuilder.sendSuccess("ping success");
    }
}
