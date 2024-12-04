package web.bookie.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.bookie.domain.UserEntity;
import web.bookie.dto.request.UserRequestDTO;
import web.bookie.dto.response.UserResponseDTO;
import web.bookie.error.BookieError;
import web.bookie.error.BookieException;
import web.bookie.respository.UserRepository;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        UserEntity user = userRequestDTO.toEntity();
        UserEntity savedUser = userRepository.save(user);
        return savedUser.toResponseDto();
    }

    public UserResponseDTO validateUser(UserRequestDTO userRequestDTO) {
        UserEntity user = userRequestDTO.toEntity();
        Optional<UserEntity> selectedUser = userRepository.findByIdAndPassword(user);

        if (selectedUser.isEmpty()) {
            BookieError.USER_NOT_VALID.throwException();
        }

        return selectedUser.get().toResponseDto();
    }
}
