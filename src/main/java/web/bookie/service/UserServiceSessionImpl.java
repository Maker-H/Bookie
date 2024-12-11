package web.bookie.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.bookie.domain.BaseEntity;
import web.bookie.domain.UserEntity;
import web.bookie.dto.request.BaseRequestDTO;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.dto.response.UserResponseDTO;
import web.bookie.respository.UserRepository;
import web.bookie.util.SessionManager;

import static web.bookie.exceptions.errors.AuthError.USER_NOT_VALID;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceSessionImpl implements UserService<UserRequestDTO, UserResponseDTO>{

    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    @Override
    public UserResponseDTO registerUser(final UserRequestDTO userRequestDTO) {
        UserEntity entity = userRequestDTO.toEntity();
        UserEntity savedUser = userRepository.save(entity);
        return savedUser.toResponseDto();
    }

    public UserResponseDTO login(UserRequestDTO userRequestDTO, HttpServletRequest servletRequest) {
        UserEntity selectedUser = findUserOrThrow(
                userRequestDTO.getId(),
                userRequestDTO.getPassword()
        );

        sessionManager.startSession(servletRequest, selectedUser);

        return selectedUser.toResponseDto();
    }

    public UserResponseDTO validateUser(final UserRequestDTO userRequestDTO, HttpServletRequest servletRequest) {
        UserEntity selectedUser = findUserOrThrow(
                userRequestDTO.getId(),
                userRequestDTO.getPassword()
        );

        UserEntity userEntityFromSession = sessionManager.getUserEntityFromSession(servletRequest);

        if (!selectedUser.equals(userEntityFromSession)) {
            USER_NOT_VALID.throwException();
        }

        return selectedUser.toResponseDto();
    }

    public void logout(HttpServletRequest servletRequest) {
        sessionManager.invalidateSession(servletRequest);
    }

    private UserEntity findUserOrThrow(final String id, final String password) {
        return userRepository.findByIdAndPassword(id, password)
                .orElseThrow(USER_NOT_VALID :: toException);
    }
}
