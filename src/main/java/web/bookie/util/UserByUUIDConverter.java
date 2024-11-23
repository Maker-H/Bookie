package web.bookie.util;

import org.springframework.stereotype.Component;
import web.bookie.data.UserRepository;
import web.bookie.domain.User;
import org.springframework.core.convert.converter.Converter;

@Component
public class UserByUUIDConverter implements Converter<String, User> {

    private UserRepository userRepository;

    public UserByUUIDConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User convert(String uuid) {
        return userRepository.findByUUID(uuid);
    }
}
