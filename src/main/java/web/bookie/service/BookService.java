package web.bookie.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web.bookie.domain.BookEntity;
import web.bookie.dto.request.BookRequestDTO;
import web.bookie.dto.response.BookResponseDTO;
import web.bookie.respository.BookRepository;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public BookResponseDTO register(BookRequestDTO bookRequestDTO) {
        BookEntity bookEntity = bookRequestDTO.toEntity();
        BookEntity savedBook = bookRepository.save(bookEntity);
        return savedBook.toResponseDto();
    }
}
