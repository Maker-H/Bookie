package web.bookie.data;

import web.bookie.domain.User;

import java.util.List;

public interface UserRepository {

    public User save(User user);

    public List<User> findAll();

    User findByUUID(String uuid);
}
