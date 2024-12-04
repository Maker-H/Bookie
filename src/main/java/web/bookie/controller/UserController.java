package web.bookie.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.dto.response.UserResponseDTO;
import web.bookie.service.UserService;
import web.bookie.util.api.ApiResponseBuilder;
import web.bookie.util.api.ApiSuccessResponse;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/auth", consumes = "application/json", produces = "application/json")
public class UserController {

    private final ApiResponseBuilder builder;

    private final UserService userService;

    @PostMapping(value = "/register")
    public ApiSuccessResponse registerUser(@RequestBody UserRequestDTO request) {

        UserResponseDTO userResponseDTO = userService.registerUser(request);
        return builder.sendSuccess(userResponseDTO);

    }

//    @PostMapping(value = "/validate")
//    public ApiSuccessResponse validateUser() {
//    }



}
