package web.bookie.data;

import web.bookie.domain.Book;

import java.io.IOException;
import java.util.List;

public interface BookRepository {

    public Book save(Book book) throws IOException;

    public List<Book> findAll();

    public Book findByUUID(String id);

}
