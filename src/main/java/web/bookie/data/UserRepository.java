package web.bookie.data;

import org.springframework.data.jpa.repository.JpaRepository;
import web.bookie.domain.User;

public interface UserRepository extends JpaRepository<User, String> {

}
