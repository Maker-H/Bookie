package web.bookie.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.bookie.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

}
