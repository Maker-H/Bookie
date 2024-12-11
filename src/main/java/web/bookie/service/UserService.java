package web.bookie.service;

import jakarta.servlet.http.HttpServletRequest;
import web.bookie.dto.request.BaseRequestDTO;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.dto.response.UserResponseDTO;

public interface UserService<T extends BaseRequestDTO, R> {
    R registerUser(T request);

    R login(T request, HttpServletRequest servletRequest);

    R validateUser(T request, HttpServletRequest servletRequest);

    void logout(HttpServletRequest servletRequest);
}
