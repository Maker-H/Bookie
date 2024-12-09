//package web.bookie.data;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementCreator;
//import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.stereotype.Repository;
//import web.bookie.domain.Book;
//
//import java.io.IOException;
//import java.sql.*;
//import java.time.LocalDateTime;
//import java.util.List;
//
////@Repository
//public class BookJdbcRepository{
//
////public class BookJdbcRepository implements BookRepository{
//
//    private JdbcTemplate jdbc;
//
//    @Autowired
//    public BookJdbcRepository(JdbcTemplate jdbc) {
//        this.jdbc = jdbc;
//    }
//
////    @Override
//    public Book save(Book book) {
//        book.setCreatedAt(LocalDateTime.now());
//
//        PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
//                "insert into Book (bookName, bookAuthor, bookDescription, bookImage, createdAt) values (?, ?, ?, ?, ?)",
//                Types.VARCHAR,
//                Types.VARCHAR,
//                Types.VARCHAR,
//                Types.BLOB,
//                Types.TIMESTAMP
//        ).newPreparedStatementCreator(
//                List.of(
//                        book.getBookName(),
//                        book.getBookAuthor(),
//                        book.getBookDescription(),
//                        book.getBookImage(),
//                        Timestamp.valueOf(book.getCreatedAt())
//                )
//        );
//
//        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbc.update(psc, keyHolder);
//
//        return book;
//    }
//
////    @Override
//    public List<Book> findAll() {
//        return jdbc.query(
//                "select uuid, bookName, bookAuthor, bookDescription, bookImage, createdAt from Book",
//                this::mapRowToBook
//        );
//    }
//
////    @Override
//    public Book findByUUID(String uuid) {
//        return jdbc.queryForObject(
//                "select uuid, bookName, bookAuthor, bookDescription, bookImage, createdAt from Book where uuid=?",
//                this::mapRowToBook,
//                uuid
//        );
//    }
//
//    private Book mapRowToBook(ResultSet rs, int rowNum) throws SQLException {
////        Blob blob = rs.getBlob("bookImage");
////        MultipartFile bookImage = null;
////
////        if (blob != null) {
////            try {
////                byte[] imageBytes = blob.getBytes(1, (int) blob.length());
////                bookImage = new CustomMultipartFile("bookImage", imageBytes);
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
//
//        return new Book(
//                rs.getLong("UUID"),
//                rs.getString("BOOKNAME"),
//                rs.getString("BOOKAUTHOR"),
//                rs.getString("BOOKDESCRIPTION"),
//                rs.getBytes("BOOKIMAGE"),
//                rs.getTimestamp("CREATEDAT").toLocalDateTime()
//        );
//    }
//}
