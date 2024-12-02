//package web.bookie.data;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementCreator;
//import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import web.bookie.domain.User;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.sql.Types;
//import java.time.LocalDateTime;
//import java.util.List;
//
////@Repository
////public class JdbcUserRepository implements UserRepository {
//public class UserJdbcRepository {
//
//    private JdbcTemplate jdbc;
//
//    @Autowired
//    public UserJdbcRepository(JdbcTemplate jdbc) {
//        this.jdbc = jdbc;
//    }
//
////    @Override
//    public User save(User user) {
//        user.setCreatedAt(LocalDateTime.now());
//
//        PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
//                "insert into AppUser (userId, userPwd, createdAt) values (?, ?, ?)",
//                Types.VARCHAR,
//                Types.VARCHAR,
//                Types.TIMESTAMP
//        ).newPreparedStatementCreator(
//                List.of(
//                        user.getUserId(),
//                        user.getUserPwd(),
//                        Timestamp.valueOf(user.getCreatedAt())
//                )
//        );
//
//        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbc.update(psc, keyHolder);
//
//        return user;
//    }
//
////    @Override
//    public List<User> findAll() {
//        return jdbc.query(
//                "select uuid, userId, userPwd, createdAt from AppUser",
//                this::mapRowToUser
//        );
//    }
//
////    @Override
//    public User findByUUID(String uuid) {
//        return jdbc.queryForObject(
//                "select uuid, userId, userPwd, createdAt from AppUser where uuid=?",
//                this::mapRowToUser,
//                uuid
//        );
//    }
//
//    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
//        return new User(
//                rs.getLong("uuid"),
//                rs.getString("userId"),
//                rs.getString("userPwd"),
//                rs.getTimestamp("createdAt").toLocalDateTime()
//        );
//    }
//}
