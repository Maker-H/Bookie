package web.bookie.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import web.bookie.data.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommonColumnTest {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void tsid_예제_테스트() {
        User user = new User();
        User savedUser = userRepo.save(user);

        System.out.println(savedUser.getTsid());
        System.out.println(savedUser.getCreatedOn());

    }

    @Test
    public void createdOn_테스트() {
        User user = new User();
        User saved = userRepo.save(user);
        LocalDateTime savedCreatedOn = saved.getCreatedOn();
        String savedTsid = saved.getTsid();

        System.out.println("user tsid:" + savedTsid);
        System.out.println("user createdOn:" + savedCreatedOn);

        entityManager.flush();
        entityManager.clear();

        Optional<User> selectUser = userRepo.findById(savedTsid);
        Assertions.assertEquals(savedCreatedOn, selectUser.get().getCreatedOn());
    }

    @Test @Commit
    public void modifiedOn_테스트() {
        User user = new User();
        User saved = userRepo.save(user);
        LocalDateTime savedCreatedOn = saved.getCreatedOn();
        String savedTsid = saved.getTsid();

        System.out.println("saved tsid: " + savedTsid);

        entityManager.flush();
        entityManager.clear();

        User updateUser = userRepo.findById(savedTsid).get();
        updateUser.setPassword("123123");
        System.out.println("update tsid: " + savedTsid);

        entityManager.flush();
        entityManager.clear();

        User selectUser = userRepo.findById(savedTsid).get();
        LocalDateTime modifiedOn = selectUser.getModifiedOn();

        System.out.println("select tsid: " + savedTsid);
        System.out.println("user modifiedOn: " + modifiedOn);

        Assertions.assertEquals(LocalDateTime.now().withNano(0), modifiedOn);
    }


}