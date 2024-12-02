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
class CommonColumnTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    public void tsid_예제_테스트() {
        User user = new User();
        User savedUser = userRepo.save(user);

        System.out.println(savedUser.getTsid());
        System.out.println(savedUser.getCreatedOn());

    }

    @Test
    public void createdOn_테스트() {

    }

    @Test
    public void modifiedOn_테스트() {

    }


}