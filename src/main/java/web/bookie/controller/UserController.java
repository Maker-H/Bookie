package web.bookie.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.dto.response.UserResponseDTO;
import web.bookie.service.UserService;
import web.bookie.util.api.ApiResponseBuilder;
import web.bookie.util.api.ApiSuccessResponse;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/auth", consumes = "application/json", produces = "application/json")
public class UserController {

    private final ApiResponseBuilder responseBuilder;

    private final UserService<UserRequestDTO, UserResponseDTO> userService;

    @PostMapping(value = "/register")
    public ApiSuccessResponse registerUser(@RequestBody final UserRequestDTO request) {
        UserResponseDTO userResponseDTO = userService.registerUser(request);
        return responseBuilder.sendSuccess(userResponseDTO);
    }

    @PostMapping(value = "/login")
    public ApiSuccessResponse login(@RequestBody final UserRequestDTO request, HttpServletRequest servletRequest) {
        UserResponseDTO userResponseDTO = userService.login(request, servletRequest);
        return responseBuilder.sendSuccess(userResponseDTO);
    }

    @PostMapping(value = "/validate")
    public ApiSuccessResponse validateUser(@RequestBody final UserRequestDTO request, HttpServletRequest servletRequest) {
        UserResponseDTO userResponseDTO = userService.validateUser(request, servletRequest);
        return responseBuilder.sendSuccess(userResponseDTO);
    }

    @GetMapping(value = "/logout")
    public ApiSuccessResponse logout(HttpServletRequest servletRequest) {
        userService.logout(servletRequest);
        return responseBuilder.sendSuccess("success");
    }


}
