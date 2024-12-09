package web.bookie.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.bookie.domain.UserEntity;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.dto.response.UserResponseDTO;
import web.bookie.respository.UserRepository;
import web.bookie.util.SessionManager;

import java.util.Optional;

import static web.bookie.exceptions.errors.AuthError.USER_NOT_VALID;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final SessionManager sessionManager;

    public UserResponseDTO registerUser(final UserRequestDTO userRequestDTO) {
        UserEntity user = userRequestDTO.toEntity();
        UserEntity savedUser = userRepository.save(user);
        return savedUser.toResponseDto();
    }

    //TODO: test
    public UserResponseDTO login(UserRequestDTO userRequestDTO, HttpServletRequest servletRequest) {
        UserEntity selectedUser = userRepository.findByIdAndPassword(
                userRequestDTO.getId(),
                userRequestDTO.getPassword()
        ).orElseThrow(USER_NOT_VALID::toException);


        sessionManager.startSession(servletRequest, selectedUser);

        return selectedUser.toResponseDto();
    }

    //TODO: test
    public UserResponseDTO validateUser(final UserRequestDTO userRequestDTO, HttpServletRequest servletRequest) {
        UserEntity selectedUser = userRepository.findByIdAndPassword(
                userRequestDTO.getId(),
                userRequestDTO.getPassword()
        ).orElseThrow(USER_NOT_VALID::toException);

        UserEntity userEntityFromSession = sessionManager.getUserEntityFromSession(servletRequest);

        if (!selectedUser.equals(userEntityFromSession)) {
            USER_NOT_VALID.throwException();
        }

        return selectedUser.toResponseDto();
    }

    //TODO: test
    public void logout(HttpServletRequest servletRequest) {
        sessionManager.invalidateSession(servletRequest);
    }

    //TODO: refac
    private UserEntity findUserOrThrow(final String id, final String password) {
        return userRepository.findByIdAndPassword(id, password)
                .orElseThrow();
    }
}
