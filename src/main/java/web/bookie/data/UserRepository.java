package web.bookie.data;

import org.springframework.data.repository.CrudRepository;
import web.bookie.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

//    public User save(User user);

    List<User> findAll();

    Optional<User> findByUuid(long uuid);
}
