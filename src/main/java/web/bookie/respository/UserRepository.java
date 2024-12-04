package web.bookie.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.bookie.domain.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByIdAndPassword(UserEntity user);
}
