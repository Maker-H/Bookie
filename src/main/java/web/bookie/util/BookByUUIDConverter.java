//package web.bookie.util;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//import web.bookie.data.BookRepository;
//import web.bookie.domain.Book;
//
//@Component
//public class BookByUUIDConverter implements Converter<String, Book> {
//
//    private BookRepository bookRepository;
//
//    public BookByUUIDConverter(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
//
//    @Override
//    public Book convert(String uuid) {
//        return bookRepository.findByUuid(Long.parseLong(uuid)).get();
//    }
//
//}
