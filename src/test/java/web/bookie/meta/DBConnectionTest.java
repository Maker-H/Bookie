package web.bookie.meta;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;


@JdbcTest
@ActiveProfiles("mariatest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DBConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void simpletest_프로파일_커넥션테스트() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Assertions.assertThat(connection).isNotNull();

            DatabaseMetaData metaData = connection.getMetaData();
            Assertions.assertThat(metaData.getUserName()).isEqualTo("bookie_dev");

            System.out.println("DB URL: " + metaData.getURL());
            System.out.println("DB Username: " + metaData.getUserName());

        }
    }
}