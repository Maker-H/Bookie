package web.bookie.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.bookie.domain.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, String> {


}
