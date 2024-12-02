package web.bookie.data;

import org.springframework.data.repository.CrudRepository;
import web.bookie.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByUuid(Long uuid);

    @Override
    List<Book> findAll();

}
