package web.bookie.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import web.bookie.respository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BaseEntityTest {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void tsid_예제_테스트() {
        UserEntity userEntity = new UserEntity();
        UserEntity savedUserEntity = userRepo.save(userEntity);

        System.out.println(savedUserEntity.getTsid());
        System.out.println(savedUserEntity.getCreatedOn());

    }

    @Test
    public void createdOn_테스트() {
        UserEntity userEntity = new UserEntity();
        UserEntity saved = userRepo.save(userEntity);
        LocalDateTime savedCreatedOn = saved.getCreatedOn();
        String savedTsid = saved.getTsid();

        System.out.println("user tsid:" + savedTsid);
        System.out.println("user createdOn:" + savedCreatedOn);

        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> selectUser = userRepo.findById(savedTsid);
        Assertions.assertEquals(savedCreatedOn, selectUser.get().getCreatedOn());
    }

    @Test @Commit
    public void modifiedOn_테스트() {
        UserEntity userEntity = new UserEntity();
        UserEntity saved = userRepo.save(userEntity);
        LocalDateTime savedCreatedOn = saved.getCreatedOn();
        String savedTsid = saved.getTsid();

        System.out.println("saved tsid: " + savedTsid);

        entityManager.flush();
        entityManager.clear();

        UserEntity updateUserEntity = userRepo.findById(savedTsid).get();
        updateUserEntity.setPassword("123123");
        System.out.println("update tsid: " + savedTsid);

        entityManager.flush();
        entityManager.clear();

        UserEntity selectUserEntity = userRepo.findById(savedTsid).get();
        LocalDateTime modifiedOn = selectUserEntity.getModifiedOn();

        System.out.println("select tsid: " + savedTsid);
        System.out.println("user modifiedOn: " + modifiedOn);

        Assertions.assertEquals(LocalDateTime.now().withNano(0), modifiedOn);
    }


}