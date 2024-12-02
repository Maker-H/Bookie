package web.bookie.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import web.bookie.data.UserRepository;

@DataJpaTest
@ActiveProfiles("mariatest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    public void tsid_예제_테스트() {
        User user = new User();
        User savedUser = userRepo.save(user);

        System.out.println(savedUser.getTsid());
        System.out.println(savedUser.getCreatedOn());

    }
    //TODO: 이미 값 들어있는 상태에서 update 쳤을때의 createdOn 테스트


}