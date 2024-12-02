package web.bookie.data;

import org.springframework.data.jpa.repository.JpaRepository;
import web.bookie.domain.Book;

public interface BookRepository extends JpaRepository<Book, String> {


}
